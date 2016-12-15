package com.meetingroom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Crash 捕捉工具类
 *
 * Created by linchaolong on 2016/9/7.
 */
public class CrashCatcher implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashCatcher";
    private static final String PATH = "H2H-SDK-Sample";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".log";

    private Context context;
    // 保留系统默认崩溃处理
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    // 自定义奔溃处理
    private Thread.UncaughtExceptionHandler targetUncaughtExceptionHandler;

    public CrashCatcher(Context app) {
        context = app;
        if(defaultUncaughtExceptionHandler == null){
            defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 注册 Crash 处理器
     *
     * @param handler
     */
    public void register(Thread.UncaughtExceptionHandler handler){
        targetUncaughtExceptionHandler = handler;
    }

    /**
     * 解除注册
     */
    public void unregister(){
        if(defaultUncaughtExceptionHandler != null){
            Thread.setDefaultUncaughtExceptionHandler(defaultUncaughtExceptionHandler);
        }
    }

    /**
     * 重启应用
     *
     * @param activityClass
     */
    public void restart(Class<? extends Activity> activityClass){
        Intent intent = new Intent( context, activityClass);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            //打印出当前调用栈信息
            throwable.printStackTrace();
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(throwable);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer();

            //如果提供了自定义异常处理器，则使用自定义处理器，否则交由系统默认处理器处理
            if (targetUncaughtExceptionHandler != null) {
                targetUncaughtExceptionHandler.uncaughtException(thread, throwable);
            }else{
                if(defaultUncaughtExceptionHandler != null){
                    defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dump异常到SD卡
     *
     * @param ex
     * @throws IOException
     */
    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡  
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.w(TAG, "sdcard unmounted, skip dump exception");
            return;
        }
        Log.w(TAG, "sdcard mounted, dump exception");

        File dir = new File(Environment.getExternalStorageDirectory(), PATH);
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }
        long current = System.currentTimeMillis();
        Date crashTime = new Date(current);
        String time = new SimpleDateFormat("_yyyy-MM-dd", Locale.getDefault()).format(crashTime);
        //以当前时间创建log文件  
        File file = new File(dir,FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            //导出发生异常的时间  
            pw.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(crashTime));

            //导出手机信息  
            dumpPhoneInfo(pw);

            //导出异常的调用栈信息
            //pw.println();
            ex.printStackTrace(pw);

            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed", e);
        }
    }

    /**
     * dump手机信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号  
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号  
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商  
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号  
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构  
        pw.print("CPU ABI: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pw.println(Arrays.toString(Build.SUPPORTED_ABIS));
        } else {
            pw.println(Build.CPU_ABI);
        }
    }

    private void uploadExceptionToServer() {
        //TODO Upload Exception Message To Your Web Server
    }

}
