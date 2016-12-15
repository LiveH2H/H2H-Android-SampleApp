package com.meetingroom.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import itutorgroup.h2h.R;

public class ToastUtils {
	private static Toast raiseHandToast;
	private static Toast mToast;
	private static Toast toast;
	private static int topMargin = 20;
	public static void showRaiseHandToast(Context context, Runnable runnable, boolean isFullScreen){
		if(raiseHandToast==null){
			raiseHandToast = new Toast(context);
			raiseHandToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0, (int) (context.getResources().getDimension(R.dimen.titleheight)+(isFullScreen?0:topMargin)));
			raiseHandToast.setDuration(Toast.LENGTH_SHORT);
			raiseHandToast.setView(getToastView(context,true,R.color.bg_raisehand_red_tips,-1,R.drawable.ic_raisehand_red,context.getString(R.string.raise_hand)));
		}
		raiseHandToast.show();
		new Handler(context.getMainLooper()).postDelayed(runnable,2000);
	}
	public static void showMeetingToast(Context context, CharSequence msg,boolean left,int bgColorId,int textColorId,int drawableId,boolean isFullScreen) {
		showMeetingToast(context,msg,left,bgColorId,textColorId,drawableId,topMargin,isFullScreen);
	}
	private static View getToastView(Context context,boolean left,int bgColorId,int textColorId,int drawableId,CharSequence msg){
		TextView mTextView = new TextView(context);
		int padding = 15;
		mTextView.setBackgroundResource(R.drawable.shape_round_button_small);
		GradientDrawable myGrad = (GradientDrawable)mTextView.getBackground();
		myGrad.setColor(context.getResources().getColor(bgColorId));
		mTextView.setPadding(padding,padding,padding,padding);
		Drawable drawable= context.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mTextView.setCompoundDrawables(left?drawable:null,null,left?null:drawable,null);//设置TextView的drawableleft
		mTextView.setCompoundDrawablePadding(10);//设置图片和text之间的间距
		mTextView.setText(msg);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setTextColor(context.getResources().getColor(textColorId==-1?R.color.black:textColorId));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		mTextView.setLayoutParams(layoutParams);
		return mTextView;
	}
	public static void showMeetingToast(Context context, CharSequence msg,boolean left,int bgColorId,int textColorId,int drawableId,int topMargin,boolean isFullScreen) {
		if(toast==null){
			toast = new Toast(context);
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0, (int) (context.getResources().getDimension(R.dimen.titleheight)+(isFullScreen?0:topMargin)));
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.setView(getToastView(context,left,bgColorId,textColorId,drawableId,msg));
		toast.show();
	}
	/**
	 * 弹出Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, CharSequence msg) {
		if (ToastUtils.mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			mToast.setText(msg);
		}
		if(Looper.myLooper() == Looper.getMainLooper()){
			mToast.show();
		}else{
			Looper.prepare();  
			mToast.show();
	        Looper.loop();  
		}
		
	}

	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}

	/**
	 * 消失Toast
	 */
	public static void cancelToast() { 
		if (ToastUtils.mToast != null) {
			if(Looper.myLooper() == Looper.getMainLooper()){
				mToast.cancel();
			}else{
				Looper.prepare();  
				mToast.cancel();
		        Looper.loop();  
			}
		}
    }
}
