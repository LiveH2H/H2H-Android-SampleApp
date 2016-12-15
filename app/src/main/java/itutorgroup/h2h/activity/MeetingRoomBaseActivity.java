package itutorgroup.h2h.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.utils.LogUtils;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 11:41
 * 邮箱：nianbin@mosainet.com
 */
public abstract class MeetingRoomBaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected Context context;
    private ProgressDialog loadingDialog;
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.e(TAG,"onCreate");
        context = this;
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
        dismissLoadingDialog();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        unregisterNetworkChanged();
        AppManager.getAppManager().removeActivity(this);
    }

    protected void initDatas(@Nullable Bundle savedInstanceState) {
    }

    protected abstract void initDatas();

    protected abstract int setContent();

    protected abstract void initView();

    protected abstract void addListener();

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setMessage("正在加载中...");
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 显示Toast
     */
    public void showToast(final CharSequence text) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
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
    public void showToast(@StringRes int resId) {
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
    /**************************************监听网络变化*********************************/
    protected boolean openEventBus(){
        return false;
    }
}
