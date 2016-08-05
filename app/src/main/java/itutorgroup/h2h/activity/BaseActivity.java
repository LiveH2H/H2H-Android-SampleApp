package itutorgroup.h2h.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mosai.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.utils.LogUtils;
import itutorgroup.h2h.widget.HintDialog;
import itutorgroup.h2h.widget.progress.DefaultProgressIndicator;
import itutorgroup.h2h.widget.progress.TextProgressIndicator;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 11:41
 * 邮箱：nianbin@mosainet.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    private TextProgressIndicator textProgressIndicator;
    protected Context mContext;
    private HintDialog hintDialog;
    protected DefaultProgressIndicator progressIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.e(TAG,"onCreate");
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        setContentView(setContent());
        fitsSystemWindows();
        initView();
        initDatas();
        initDatas(savedInstanceState);
        addListener();
        registerNetworkChanged();
        if(openEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        unregisterNetworkChanged();
        AppManager.getAppManager().finishActivity(this);
    }

    protected void initDatas(@Nullable Bundle savedInstanceState) {
    }

    protected abstract void initDatas();

    protected abstract int setContent();

    protected abstract void initView();

    protected abstract void addListener();

    public void showTextProgressDialog(String message) {
        if (textProgressIndicator == null)
            textProgressIndicator = TextProgressIndicator.newInstance(this);
        textProgressIndicator.showDialog(message);
    }

    public void dismissTextProgressDialog() {
        if (textProgressIndicator != null)
            textProgressIndicator.dismissDialg();
    }

    public void showProgressDialog() {
        if (progressIndicator == null) {
            progressIndicator = DefaultProgressIndicator.newInstance(mContext);

        }
        progressIndicator.show();
    }

    public void dismissProgressDialog() {
        if (progressIndicator != null && progressIndicator.isShowing()) {
            progressIndicator.dismiss();
        }
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(int resId) {
        return showHintDialog(getText(resId));
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(CharSequence text) {
        if (hintDialog == null) {
            hintDialog = new HintDialog(mContext);
        }
        hintDialog.setMessages(text);
        if (!hintDialog.isShowing()) {
            hintDialog.show();
        }
        return hintDialog;
    }

    /**
     * 隐藏提示框
     */
    public void dismissHintDialog() {
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
    }

    /**
     * 显示Toast
     */
    public void showToast(CharSequence text) {
        ToastUtils.showToast(mContext,text);
    }

    /**
     * 显示Toast
     */
    public void showToast(int resId) {
        ToastUtils.showToast(mContext,resId);
    }

    private void fitsSystemWindows() {
        if (openfitsSystemWindows()) {
            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }
    }

    private boolean openfitsSystemWindows() {
        return false;
    }

    /*********************************************
     * 监听网络变化
     ***********************************/
    private boolean netWorkFirstTag=true;
    private IntentFilter networkChangedFilter;
    private NetworkChangedInterface networkChangedInterface;
    public BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(netWorkFirstTag){
                netWorkFirstTag=false;
                return;
            }
            String action = intent.getAction();
            LogUtils.e("net changed");
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    if (networkChangedInterface != null)
                        networkChangedInterface.networkChanged(netInfo.isAvailable());
                }
            }
        }
    };

    public interface NetworkChangedInterface {
        void networkChanged(boolean isAvailable);
    }

    private void registerNetworkChanged() {
        if (this instanceof NetworkChangedInterface) {
            networkChangedInterface = (NetworkChangedInterface) this;
            networkChangedFilter = new IntentFilter();
            networkChangedFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(myNetReceiver, networkChangedFilter);
        }

    }

    private void unregisterNetworkChanged() {
        if (this instanceof NetworkChangedInterface) {
            if (networkChangedInterface != null) {
                if (myNetReceiver != null && networkChangedFilter != null) {
                    unregisterReceiver(myNetReceiver);
                }
                myNetReceiver = null;
                networkChangedFilter = null;
            }
        }
    }
    protected boolean openEventBus(){
        return false;
    }
    /**************************************监听网络变化*********************************/
}
