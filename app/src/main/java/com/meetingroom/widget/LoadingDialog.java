package com.meetingroom.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.meetingroom.utils.LogUtils;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 加载框
 * @author Rays 2016年4月12日
 *
 */
public class LoadingDialog extends Dialog {
	private TextView messages;
	private boolean checkback;
	private BackCallback backCallback;
	public LoadingDialog(Context context) {
		this(context, R.style.dialog);
	}

	public LoadingDialog(Context context, boolean checkback, BackCallback backCallback) {
		this(context, R.style.dialog);
		this.checkback = checkback;
		this.backCallback = backCallback;
	}
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		initViews(context);
	}

	private void initViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		messages = ViewUtil.findViewById(view, R.id.messages);
		this.setContentView(view);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	}
	
	public void setMessages(CharSequence text) {
		messages.setText(text);
	}

	public void setMessages(int resid) {
		messages.setText(resid);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (checkback) {
			LogUtils.e("loadingDialog-onKey");
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				LogUtils.e("loadingDialog-onKeyBack");
				if (isShowing()) {
					if (backCallback != null) {
						backCallback.back();
						return true;
					}
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public interface BackCallback {
		void back();
	}
}
