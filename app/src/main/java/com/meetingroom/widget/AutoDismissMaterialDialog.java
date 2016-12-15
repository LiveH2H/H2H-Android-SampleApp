package com.meetingroom.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;


import com.meetingroom.utils.SystemUtil;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Rays on 2016/12/1.
 */
public class AutoDismissMaterialDialog implements DialogInterface.OnDismissListener, View.OnClickListener {
    private WeakReference<Activity> activityWeakReference;
    private long countTime;
    private long timerTime;
    private long period = 1000;
    private MaterialDialog dialog;
    private OnEventListener onEventListener;
    private Timer timer;

    public AutoDismissMaterialDialog(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.dialog = new MaterialDialog(activity);
        this.dialog.setOnDismissListener(this);
    }

    public AutoDismissMaterialDialog setTitle(int resId) {
        dialog.setTitle(resId);
        return this;
    }

    public AutoDismissMaterialDialog setTitle(CharSequence title) {
        dialog.setTitle(title);
        return this;
    }

    public AutoDismissMaterialDialog setMessage(int resId) {
        dialog.setMessage(resId);
        return this;
    }

    public AutoDismissMaterialDialog setMessage(CharSequence message) {
        dialog.setMessage(message);
        return this;
    }

    public AutoDismissMaterialDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public AutoDismissMaterialDialog setPositiveButton(int resId) {
        dialog.setPositiveButton(resId, this);
        return this;
    }

    public AutoDismissMaterialDialog setPositiveButton(String text) {
        dialog.setPositiveButton(text, this);
        return this;
    }

    public AutoDismissMaterialDialog setNegativeButton(int resId) {
        dialog.setNegativeButton(resId, this);
        return this;
    }

    public AutoDismissMaterialDialog setNegativeButton(String text) {
        dialog.setPositiveButton(text, this);
        return this;
    }

    public AutoDismissMaterialDialog setCountTime(long countTime) {
        this.countTime = countTime;
        return this;
    }

    public AutoDismissMaterialDialog setOnEventListener(OnEventListener listener) {
        this.onEventListener = listener;
        return this;
    }

    public void show() {
        dialog.show();
        startTimer();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void startTimer() {
        if (countTime <= 0 || period <= 0) {
            return;
        }
        timerTime = countTime;
        cancelTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timerTime >= 0) {
                    onUpdate(timerTime);
                    timerTime -= period;
                } else {
                    dismiss();
                }
            }
        }, new Date(), period);
    }

    private void onUpdate(final long time) {
        if (onEventListener != null) {
            if (SystemUtil.isUiThread()) {
                onEventListener.onUpdate(time, AutoDismissMaterialDialog.this);
            } else {
                Activity activity = activityWeakReference.get();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onEventListener.onUpdate(time, AutoDismissMaterialDialog.this);
                        }
                    });
                }
            }
        }
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        cancelTimer();
        if (onEventListener != null) {
            onEventListener.onDismiss(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (onEventListener != null) {
            onEventListener.onClick(v.getId() == dialog.getPositiveButton().getId(), this);
        }
    }

    public interface OnEventListener {
        /**
         * 按钮点击事件
         * @param isPositiveButton 是否是确认按钮
         * @param dialog dialog
         */
        void onClick(boolean isPositiveButton, AutoDismissMaterialDialog dialog);

        /**
         * dialog消失事件
         * @param dialog dialog
         */
        void onDismiss(AutoDismissMaterialDialog dialog);

        /**
         * 更新事件
         * @param time dialog
         */
        void onUpdate(long time, AutoDismissMaterialDialog dialog);
    }
}
