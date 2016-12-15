package com.meetingroom.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.util.SystemUtil;
import com.meetingroom.bean.SettingConfig;
import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.MRUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;

import itutorgroup.h2h.BuildConfig;
import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月04日 11:34
 * 邮箱：nianbin@mosainet.com
 */
public abstract class MeetingRoomBaseActivity extends BaseActivity implements View.OnClickListener{
    protected String TAG = getClass().getSimpleName();
    /*********************************************
     * 监听网络变化
     ***********************************/
    private boolean netWorkFirstTag = true;
    private IntentFilter networkChangedFilter;
    private NetworkChangedInterface networkChangedInterface;
    public BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (netWorkFirstTag) {
                netWorkFirstTag = false;
                return;
            }
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (networkChangedInterface != null) {
                    ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isAvailable()) {
                        LogUtils.e("net changed:" + true);
                        networkChangedInterface.networkChanged(true);
                    } else {
                        LogUtils.e("net changed:" + false);
                        networkChangedInterface.networkChanged(false);
                    }
                }

            }
        }
    };
    private ANRWatchDog anrWatchDog;

    @Override
    public void onClick(View v) {

    }

    protected void initDatas(@Nullable Bundle savedInstanceState) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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
        initWatchDog();
    }

    protected abstract void initDatas();

    /****************************
     * 沉浸状态栏控制
     ******************************/

    protected abstract int setContent();

    protected abstract void initView();

    protected abstract void addListener();

    /****************************沉浸状态栏控制******************************/
    private void fitsSystemWindows() {
        if (openfitsSystemWindows()) {
            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }
    }

    protected boolean openfitsSystemWindows() {
        return false;
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

    //eventbus控制开关
    protected boolean openEventBus(){
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        unregisterNetworkChanged();
        destroyWatchDog();
        System.gc();
    }

    @Override
    protected boolean isChangeStatusBarBackgroud() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!SystemUtil.isTablet(context)){
            initSettingConfig();
        }
    }
    protected void initSettingConfig(){
        if(findViewById(R.id.rl_title)!=null){
            List<SettingConfig> configs = DataSupport.findAll(SettingConfig.class);
            SettingConfig localConfig = null;
            for(SettingConfig config : configs){
                if(TextUtils.equals(config.getMeetingId(), H2HConference.getInstance().getMeetingId())&&
                        TextUtils.equals(config.getUserId(), H2HConference.getInstance().getLocalUserName())){
                    localConfig = config;
                    break;
                }
            }
            if(localConfig==null){
                localConfig = new SettingConfig();
                localConfig.setMeetingId(H2HConference.getInstance().getMeetingId());
                localConfig.setUserId(H2HConference.getInstance().getLocalUserName());
                localConfig.setHost(MRUtils.isHost());
                localConfig.save();
            }
            findViewById(R.id.rl_title).setBackgroundColor(Color.parseColor(localConfig.getThemeColor()));
        }

    }
    protected boolean openWahtDog(){
        return false;
    }
    protected void destroyWatchDog(){
        if (openWahtDog()&& BuildConfig.DEBUG)
        {
            if(anrWatchDog!=null){
                new Handler(getMainLooper()).removeCallbacks(anrWatchDog);
                anrWatchDog=null;
            }
        }

    }

    protected void initWatchDog(){
        if (openWahtDog()&& BuildConfig.DEBUG)
        {
            if(anrWatchDog==null){
                anrWatchDog = new ANRWatchDog(5000);
                anrWatchDog.setIgnoreDebugger(true);
                anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
                    @Override
                    public void onAppNotResponding(ANRError error) {
                        Log.e("ANR-Watchdog", "Detected Application Not Responding!");
                        throw error;
                    }
                });
                anrWatchDog.setReportMainThreadOnly().start();
            }
        }


    }

    public interface NetworkChangedInterface {
        void networkChanged(boolean isAvailable);
    }
}
