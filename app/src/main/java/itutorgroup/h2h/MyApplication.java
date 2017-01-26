package itutorgroup.h2h;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.itutorgroup.h2hmodel.H2HConstant;
import com.itutorgroup.h2hmodel.H2HUserManager;
import com.meetingroom.utils.CrashCatcher;
import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.SystemUtil;

import org.litepal.LitePalApplication;

/**
 * Created by Rays on 16/5/9.
 */
public class MyApplication extends LitePalApplication {
    private static final String TAG = "MyApplication";
    public static MyApplication INSTANCE;
    private int appCount = 0;


    private static final String APPLICATION_ID = "73F427EA-0226-42FA-8A89-BA8B62F0A6D6";
    private static final String TOKEN = "i/ZwbU9epNah5Dk0WXRFtvxMWkhm";
    //private static final String APPLICATION_ID = "10F40A1B-4DBB-4590-B540-0169947B4ADF";
    //private static final String TOKEN = "zEV7/xBVCUi2zhDXR2Z19OGW1G7/";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(this.toString() + " pid=" + android.os.Process.myPid() + " isTablet=" + getResources().getBoolean(R.bool.tablet));
        INSTANCE = this;
        if (!SystemUtil.isCurProcess(this)) {
            LogUtils.i(android.os.Process.myPid() + "不是主进程");
            return;
        }
        long time = System.currentTimeMillis();
        Fresco.initialize(INSTANCE);
        MRUtils.clearDatas(INSTANCE);

        H2HConstant.BASE_URL = H2HConstant.SERVER_QA;
        // qa server: test9@qq.com / 123456
        H2HUserManager.getInstance().init(TOKEN, APPLICATION_ID);

        initCrashCatcher();
        setActivityLifecycleCallbacks();
        LogUtils.i("Application init finish, Time=" + (System.currentTimeMillis() - time));
    }

    private void initCrashCatcher() {
        // 崩溃捕捉
        new CrashCatcher(this);
    }

    private void setActivityLifecycleCallbacks() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public boolean isHome() {
        return appCount > 0;
    }

    @Override
    public void onTerminate() {

        Log.d(TAG, "onTerminate");
        super.onTerminate();

    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
