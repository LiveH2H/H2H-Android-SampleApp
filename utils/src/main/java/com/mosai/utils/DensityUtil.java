/**
 * 
 */
package com.mosai.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author hedewen creattime：2013-4-11下午8:26:14
 */
public class DensityUtil {

	public static int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static float dip2pxf(Context context, int dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return dpValue * scale + 0.5f;
	}

	public static float dip2pxf(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return dpValue * scale + 0.5f;
	}
	
	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static float sp2pxf(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return spValue * fontScale + 0.5f;
	}
	public static int getWebviewScale(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		if (width > 650) {
			return 190;
		} else if (width > 520) {
			return 520;
		} else if (width > 450) {
			return 140;
		} else if (width > 300) {
			return 120;
		} else {
			return 100;
		}
	}
}
