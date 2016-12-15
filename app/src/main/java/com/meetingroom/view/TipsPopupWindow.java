package com.meetingroom.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mosai.utils.DensityUtil;

import itutorgroup.h2h.R;

public class TipsPopupWindow extends PopupWindow {
	private Context context;
	private int backgoudId =-1;
	private String tipString;
	private Rect mRect = new Rect();
	private final int[] mLocation = new int[2];
	private int popupGravity = Gravity.NO_GRAVITY;
	private Handler handler;
	private long delayTime;
	public TipsPopupWindow(Context context,int backgoudId,String tipString,long delayTime){
		handler = new Handler(Looper.getMainLooper());
		this.backgoudId = backgoudId;
		this.tipString = tipString;
		this.context = context;
		this.delayTime = delayTime;
		init();
	}
	private void init() {
		setOutsideTouchable(false);
		setFocusable(true);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable());
		TextView textView = new TextView(context);
		textView.setText(tipString);
		textView.setBackgroundResource(backgoudId);
		textView.setGravity(Gravity.CENTER);
		setContentView(textView);
		setAnimationStyle(R.style.popwindow_anim_style);
	}

	public void show(View view) {
		view.getLocationOnScreen(mLocation);
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
				mLocation[1] + view.getHeight());
		getContentView().measure(0, 0);
		showAtLocation(view, popupGravity, mRect.right
				- getContentView().getWidth() - DensityUtil.dip2px(context, 5),
				mRect.bottom - DensityUtil.dip2px(context, 10));
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		},delayTime);
	}

}