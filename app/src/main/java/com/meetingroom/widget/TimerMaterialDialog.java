package com.meetingroom.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.format.DateFormat;
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
public class TimerMaterialDialog implements DialogInterface.OnDismissListener {
    private WeakReference<Activity> activityWeakReference;
    private long countTime;
    private long timerTime;
    private MaterialDialog dialog;
    private OnClickListener onClickListener;
    private Timer timer;
    private String message;

    public TimerMaterialDialog(Activity activity, long countTime) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.countTime = countTime;
        this.timerTime = countTime;
        this.dialog = new MaterialDialog(activity);
        this.dialog.setOnDismissListener(this);
    }

    public TimerMaterialDialog setTitle(int resId) {
        dialog.setTitle(resId);
        return this;
    }

    public TimerMaterialDialog setMessage(int resId) {
        message = getString(resId);
        setTimerMessage(timerTime);
        return this;
    }

    public TimerMaterialDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public TimerMaterialDialog setPositiveButton(int resId, final OnClickListener listener) {
        onClickListener = listener;
        dialog.setPositiveButton(resId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(dialog);
                }
            }
        });
        return this;
    }

    public void show() {
        dialog.show();

        startTimer();
    }

    private void startTimer() {
        if (timer == null) {
            timerTime = countTime;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (timerTime >= 0) {
                        setTimerMessage(timerTime);
                    } else {
                        if (onClickListener != null) {
                            onClickListener.onClick(dialog);
                        }
                        cancelTimer();
                    }
                    timerTime -= 1000;
                }
            }, new Date(), 1000);
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
    }

    private void setTimerMessage(final long time) {
        if (SystemUtil.isUiThread()) {
            dialog.setMessage(message + "\n" + DateFormat.format("mm:ss", time));
        } else {
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setMessage(message + "\n" + DateFormat.format("mm:ss", time));
                    }
                });
            }
        }
    }

    private String getString(int resId) {
        if (activityWeakReference != null) {
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                return activity.getString(resId);
            }
        }
        return "";
    }

    public interface OnClickListener {
        void onClick(MaterialDialog dialog);
    }
}
