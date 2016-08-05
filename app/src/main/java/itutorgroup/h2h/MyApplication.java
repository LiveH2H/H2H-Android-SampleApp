package itutorgroup.h2h;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.itutorgroup.h2hchat.H2HChatApplication;

import itutorgroup.h2h.common.AbnormalHandler;


/**
 * Created by Rays on 16/5/9.
 */
public class MyApplication extends H2HChatApplication {
    private static final String TAG = "MyApplication";
    public static MyApplication INSTANCE;
    private int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        INSTANCE = this;
        initAbnormalHandler();
        setActivityLifecycleCallbacks();
    }

    private void initAbnormalHandler() {
        if (BuildConfig.DEBUG) {
            AbnormalHandler crashHandler = AbnormalHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
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
        return appCount > 0 ? true : false;
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
