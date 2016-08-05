package com.mosai.utils;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * @author hedewen
 * 
 *         createtime:2013-5-23上午10:49:38
 */
public class SwitchingAnim {
	public static void back(Activity activity, Button back) {
		ClickEffect(activity, back);
		activity.finish();
		activity.overridePendingTransition(R.anim.view_push_right_in,
				R.anim.view_push_right_out);
		;
	}

	public static void forth(Activity activity, Button forth) {
		ClickEffect(activity, forth);
		activity.finish();
		activity.overridePendingTransition(R.anim.view_push_right_in,
				R.anim.view_push_right_out);
		;
	}

	public static void ClickEffect(Activity activity, View button) {
		Animation anim = AnimationUtils.loadAnimation(activity,
				R.anim.button_alpha);
		button.startAnimation(anim);
	}

	public static void backward(Activity activity) {
		activity.finish();
		activity.overridePendingTransition(R.anim.view_push_right_in,
				R.anim.view_push_right_out);
	}

	public static void forward(Activity activity) {
		activity.overridePendingTransition(R.anim.view_push_left_in,
				R.anim.view_push_left_out);
	}

	public static void up(Activity activity) {
		activity.overridePendingTransition(R.anim.view_push_up_in,
				R.anim.view_push_up_out);
	}

	public static void down(Activity activity) {
		activity.finish();
		activity.overridePendingTransition(R.anim.view_push_down_in,
				R.anim.view_push_down_out);
	}
}
