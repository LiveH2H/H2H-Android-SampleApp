package com.meetingroom.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 提示框
 *
 * @author Rays 2016年4月12日
 */
public class HintDialog extends Dialog implements View.OnClickListener {

    private TextView tvMessages;
    private TextView tvTitle;

    public HintDialog(Context context) {
        this(context, R.style.dialog);
    }

    public HintDialog(Context context, int theme) {
        super(context, theme);
        initViews(context);
    }

    private void initViews(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint, null);
        tvTitle = ViewUtil.findViewById(view, R.id.tvTitle);
        tvMessages = ViewUtil.findViewById(view, R.id.tvMessages);
        view.findViewById(R.id.btnOk).setOnClickListener(this);
        setContentView(view);

//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        layoutParams.width = (int) (metrics.widthPixels * 0.6f);
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutParams.gravity = Gravity.CENTER;
    }

    @Override
    public void onClick(View v) {
        cancel();
    }

    public void setTitle(CharSequence text) {
        tvTitle.setText(text);
    }

    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setMessages(CharSequence text) {
        tvMessages.setText(text);
    }

    public void setMessages(int resId) {
        tvMessages.setText(resId);
    }

}
