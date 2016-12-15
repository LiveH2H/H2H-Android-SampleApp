package com.meetingroom.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.ScreenAdapter;
import com.meetingroom.utils.SystemUtil;
import com.meetingroom.widget.HintDialog;
import com.meetingroom.widget.LoadingDialog;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 公共父类
 *
 * @author Rays
 */
public class BaseActivity extends AppCompatActivity {

    protected Context context;
    private Toast toast;
    private HintDialog hintDialog;
    private LoadingDialog loadingDialog;
    private long exitTime;
    private boolean isDoubleBackExit = false;
    private boolean isRotateScreen = false;
    private boolean isFinishAnim = true;

    /**
     * 是否双击退出应用
     *
     * @param doubleBackExit
     */
    public void setDoubleBackExit(boolean doubleBackExit) {
        isDoubleBackExit = doubleBackExit;
    }

    /**
     * 是否更改状态栏背景色
     *
     * @return 默认为true
     */
    protected boolean isChangeStatusBarBackgroud() {
        return true;
    }

    /**
     * 是否为dialog theme
     * @return 默认为false
     */
    protected boolean isDialogTheme() {
        return false;
    }

    public boolean isRotateScreen() {
        return isRotateScreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isRotateScreen = ScreenAdapter.beforeCreate(this);
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        context = this;
        LogUtils.d(getClass().getSimpleName() + " create");
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onSetContentView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onSetContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onSetContentView();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isDialogTheme() && SystemUtil.isTablet(this)) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = (int) (outMetrics.widthPixels * 0.6f);
            lp.height = (int) (outMetrics.heightPixels * 0.9f);
            lp.gravity = Gravity.CENTER;
            getWindow().setAttributes(lp);
        }
    }

    protected void onSetContentView() {
        if (isChangeStatusBarBackgroud()) {
            ViewUtil.initStatusBar(this);
        }
    }

    public void showHintMessages(CharSequence text) {
        showToast(text);
    }

    public void showHintMessages(@StringRes int resid) {
        showToast(resid);
    }

    /**
     * 显示Toast
     */
    public void showToast(final CharSequence text) {
        if (SystemUtil.isUiThread()) {
            showToastUiThread(text);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToastUiThread(text);
                }
            });
        }
    }

    /**
     * 显示Toast
     */
    public void showToast(@StringRes  int resId) {
        showToast(getText(resId));
    }

    private void showToastUiThread(CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog(String text) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (text != null) {
            loadingDialog.setMessages(text);
        }
        safeShowDialog(loadingDialog);
    }

    /**
     * 安全地show dialog
     *
     * @param dialog
     */
    public void safeShowDialog(final Dialog dialog){
        if (dialog == null || dialog.isShowing()) {
            return;
        }
        SystemUtil.runOnUiThread(this, new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isFinishing()){
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 显示提示框
     */
    public void showHintDialog(int resId) {
        showHintDialog(getText(resId));
    }

    /**
     * 显示提示框
     */
    public void showHintDialog(CharSequence title, CharSequence msg, DialogInterface.OnCancelListener listener) {
        if (hintDialog == null) {
            hintDialog = new HintDialog(this);
        }
        hintDialog.setTitle(title);
        hintDialog.setMessages(msg);
        hintDialog.setCanceledOnTouchOutside(false);
        hintDialog.setOnCancelListener(listener);
        safeShowDialog(hintDialog);
    }

    /**
     * 显示提示框
     */
    public void showHintDialog(CharSequence msg) {
        showHintDialog(null, msg, null);
    }

    /**
     * 隐藏提示框
     */
    public void dismissHintDialog() {
        if (hintDialog != null) {
            hintDialog.dismiss();
            hintDialog = null;
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(getClass().getSimpleName()+" destroy");
        super.onDestroy();
        dismissHintDialog();
        dismissLoadingDialog();
        toast = null;
//        AsyncHttp.getInstance().getClient().cancelRequests(context, true);
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isDoubleBackExit && keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime = System.currentTimeMillis();
            if (curTime - exitTime > 2000) {
                showToast(R.string.exit_application_hint);
                exitTime = curTime;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 结束动画
    public void finish(boolean isAnim) {
        this.isFinishAnim = isAnim;
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        if (isFinishAnim) {
            finishAnimation();
        }
    }
    // 子类可重写实现自定义结束动画
    protected void finishAnimation() {

    }

}
