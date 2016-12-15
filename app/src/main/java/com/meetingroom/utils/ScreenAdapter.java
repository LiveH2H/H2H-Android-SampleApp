package com.meetingroom.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * 屏幕适配工具类
 *
 * Created by linchaolong on 2016/6/17.
 */
public class ScreenAdapter {

    /**
     * 在Activity的onCreate方法调用super.onCreate前调用
     *
     * @param activity
     * @return 是否旋转屏幕
     */
    public static boolean beforeCreate(Activity activity) {
        // 平板优先使用layout-land下的layout，屏幕方向为横屏
        int screenOrientation = SystemUtil.isTablet(activity) ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        if (activity.getRequestedOrientation() != screenOrientation) {
            activity.setRequestedOrientation(screenOrientation);
            return true;
        }
        return false;
    }

}
