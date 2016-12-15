package com.meetingroom.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by Rays on 16/6/30.
 */
public class ResourcesUtil {

    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resId, context.getTheme());
        } else {
            return context.getResources().getColor(resId);
        }
    }

    public static Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resId, context.getTheme());
        } else {
            return context.getResources().getDrawable(resId);
        }
    }

    public static void setTint(Drawable drawable, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(color);
        }
    }

}
