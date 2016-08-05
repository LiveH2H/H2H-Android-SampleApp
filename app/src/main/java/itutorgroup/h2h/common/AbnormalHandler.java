package itutorgroup.h2h.common;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.mosai.utils.DateTimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.BuildConfig;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.LogUtils;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class AbnormalHandler implements UncaughtExceptionHandler {

    public static final String TAG = "AbnormalHandler";

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static AbnormalHandler INSTANCE = new AbnormalHandler();
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    /**
     * 保证只有一个CrashHandler实例
     */
    private AbnormalHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static AbnormalHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtils.e(e.getMessage());
            }
            // 退出程序
            AppManager.getAppManager().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        ex.printStackTrace();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, mContext.getString(R.string.crash), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId() == null ? "" : telephonyManager.getSubscriberId();
        String manufacturer = android.os.Build.MANUFACTURER == null ? "" : android.os.Build.MANUFACTURER;
        String board = android.os.Build.BOARD == null ? "" : android.os.Build.BOARD;
        String model = android.os.Build.MODEL == null ? "" : android.os.Build.MODEL;
        String release = android.os.Build.VERSION.RELEASE == null ? "" : android.os.Build.VERSION.RELEASE;
        String packageName = ctx.getPackageName();
        infos.put("IMSI", imsi);
        infos.put("MANUFACTURER", manufacturer);
        infos.put("BOARD", board);
        infos.put("MODEL", model);
        infos.put("RELEASE", release);
        infos.put("包名", packageName);
//		infos.put("Version", ctx.getResources().getString(R.string.version));
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     */
    private void saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        final String content = sb.toString();
        try {
            if (BuildConfig.DEBUG) {
                long timestamp = System.currentTimeMillis();
                String time = DateTimeUtil.getCurrDateTimeStr().replace(":", "_");
                String fileName = mContext.getResources().getString(R.string.app_name) + "-error-" + time + "-" + timestamp + ".log";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String path = Environment.getExternalStorageDirectory().getPath()+File.separator+"mosai"+File.separator;
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(path + fileName);
                    fos.write(sb.toString().getBytes());
                    fos.close();
                }
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }
}
