package com.meetingroom.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.itutorgroup.h2hconference.H2HConference;
import com.meetingroom.bean.SettingConfig;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.SystemUtil;
import com.meetingroom.widget.HintDialog;
import com.meetingroom.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;

import itutorgroup.h2h.R;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年06月24日 15:06
 * 邮箱：nianbin@mosainet.com
 */
public class BaseFragment extends Fragment {
    public static final int RANK_POINT = 0;
    public static final int RANK_ROW = 1;
    private static final String fragment_level = "Fragemnt_Level";
    public boolean isCreated;
    public int rank = RANK_POINT;
    protected String tag = this.getClass().getSimpleName();
    protected Context mContext;
    protected View view;
    protected boolean created;
    protected boolean isHide = true;
    private HintDialog hintDialog;
    private LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Log.e(fragment_level,tag+"onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(fragment_level,tag+"onCreate");
        if(openEventBus()){
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(fragment_level,tag+"onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!created){
            created=true;
        }
        Log.e(fragment_level,tag+"onResume");
        if(!SystemUtil.isTablet(getActivity())){
            initSettingConfig();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(fragment_level,tag+"onPause");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        dismissLoadingDialog();
        Log.e(fragment_level,tag+"onDestroy");
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

    public void showLoadingDialog() {
        if (isFinishing()) {
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    protected boolean openEventBus(){
        return false;
    }
    public boolean allowChat(){
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
            localConfig.save();
        }
        return localConfig.isAllowChat();
    }
    public void initSettingConfig(){
        if(view!=null){
            if(view.findViewById(R.id.rl_title)!=null){
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
                view.findViewById(R.id.rl_title).setBackgroundColor(Color.parseColor(localConfig.getThemeColor()));
            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHide = hidden;
        Log.e(fragment_level,tag+"onHiddenChanged:"+hidden);
        if(getActivity()!=null&&!SystemUtil.isTablet(getActivity())){
            if(!hidden){
                initSettingConfig();
            }
        }
    }

    protected boolean isFinishing() {
        return getActivity() == null || getActivity().isFinishing();
    }
}
