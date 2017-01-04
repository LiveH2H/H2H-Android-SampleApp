package com.meetingroom.fragment.flat;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hmodel.H2HFeatures;
import com.kyleduo.switchbutton.SwitchButton;
import com.meetingroom.activity.SupportActivity;
import com.meetingroom.activity.SupportRequestActivity;
import com.meetingroom.activity.flat.FlatChangeColorThemeActivity;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.bean.SettingConfig;
import com.meetingroom.callback.MDialogmissCallback;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.Tools;
import com.meetingroom.view.RelativeDialog;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月23日 16:21
 * 邮箱：nianbin@mosainet.com
 */
public class FlatMoreSettingDialog extends RelativeDialog implements View.OnClickListener{
    private TextView tvSetTime, tvLiveSupport, tvSubmitSupportRequest, tvLiveTranscript, tvParticiipantSetting,
            tvDeviceSetting, tvThemeColor;
    private SwitchButton switchChat, switchRaisehand, switchshowTimer, switchChatOverlay;
    private RelativeLayout rlChatOverlay, rlChat, rlRaisehand;
    private ServerConfig serverConfig;
    private H2HConference conference;
    private TimePickerDialog tpdMins;
    private MDialogmissCallback mDialogmissCallback;
    public FlatMoreSettingDialog(Context context, ServerConfig serverConfig){
        super(context);
        this.serverConfig = serverConfig;
        init();
        initConfig();
        addListener();
        initData();
        if(mContext instanceof MDialogmissCallback){
            mDialogmissCallback = (MDialogmissCallback) mContext;
        }
    }
    private void initData(){
        beforeConfig();
        ViewUtil.setVisibility(tvLiveSupport, H2HFeatures.isLiveSupportEnabled() ? View.VISIBLE : View.GONE);
    }
    private void addListener() {
        tvThemeColor.setOnClickListener(this);
        tvSetTime.setOnClickListener(this);
        tvLiveSupport.setOnClickListener(this);
        tvSubmitSupportRequest.setOnClickListener(this);
        switchshowTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvSetTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        switchChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                rlChatOverlay.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                afterConfig();
                if(MRUtils.isHost()){
                    H2HConference.getInstance().toggleChatPermission(switchChat.isChecked());
                    H2HConference.getInstance().toggleRaisehandPermission(switchRaisehand.isChecked());
                }
                if(mDialogmissCallback!=null){
                    mDialogmissCallback.callback();
                }
            }
        });
    }

    private void init(){
        conference = H2HConference.getInstance();
        width = Tools.getAtyWidth(mContext)/3;
        height = Tools.getAtyHeight(mContext)/3*2;
        view = View.inflate(mContext, R.layout.activity_more_setting,null);
        tvSetTime = ViewUtil.findViewById(view,R.id.tv_set_time);
        tvLiveSupport = ViewUtil.findViewById(view,R.id.tv_live_support);
        tvSubmitSupportRequest = ViewUtil.findViewById(view,R.id.tv_submit_support_request);
        tvLiveTranscript = ViewUtil.findViewById(view,R.id.tv_live_transcript);
        switchChat = ViewUtil.findViewById(view,R.id.switchChat);
        switchRaisehand = ViewUtil.findViewById(view,R.id.switchRaisehand);
        switchshowTimer = ViewUtil.findViewById(view,R.id.switchshowTimer);
        switchChatOverlay = ViewUtil.findViewById(view,R.id.switchChatOverlay);
        tvParticiipantSetting = ViewUtil.findViewById(view,R.id.tv_particiipant_setting);
        tvDeviceSetting = ViewUtil.findViewById(view,R.id.tv_device_setting);
        tvThemeColor = ViewUtil.findViewById(view,R.id.tv_theme_color);
        rlChatOverlay = ViewUtil.findViewById(view,R.id.rl_chat_overlay);
        rlChat = ViewUtil.findViewById(view,R.id.rl_chat);
        rlRaisehand = ViewUtil.findViewById(view,R.id.rl_raisehand);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_live_support:
//                showToast("coming soon");
//                startActivity(new Intent(mContext, SupportActivity.class));
                ((Activity)mContext).startActivityForResult(new Intent(mContext, SupportActivity.class),0);
                break;
            case R.id.tv_submit_support_request:
                ((Activity)mContext).startActivityForResult(new Intent(mContext, SupportRequestActivity.class),0);
                break;
            case R.id.tv_live_transcript:
                break;
            case R.id.tv_particiipant_setting:
                break;
            case R.id.tv_device_setting:
                break;
            case R.id.tv_theme_color:
                mContext.startActivity(new Intent(mContext, FlatChangeColorThemeActivity.class));
                break;
            case R.id.tv_set_time:
                showTpdMins();
                break;
        }
    }
    private void showTpdMins() {
//        if(tpdMins==null){
//        tpdMins = new TimePickerDialog.Builder()
//                .setCallBack(new OnDateSetListener() {
//                    @Override
//                    public void onDateSet(TimePickerDialog timePickerView, long millseconds,int result) {
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTimeInMillis(millseconds);
//                        tvSetTime.setText(String.format("%d %s",(calendar.get(Calendar.MINUTE)+1)*5,mContext.getString(R.string.minute)));
//                    }
//                })
//                .setCancelStringId(mContext.getString(R.string.cancel))
//                .setSureStringId(mContext.getString(R.string.sure))
//                .setTitleStringId(mContext.getString(R.string.time_picker))
//                .setMinuteText(mContext.getString(R.string.minute))
//                .setCyclic(true)
//                .setThemeColor(mContext.getResources().getColor(R.color.gray))
//                .setType(Type.MIN)
//                .setWheelItemTextNormalColor(mContext.getResources().getColor(R.color.timetimepicker_default_text_color))
//                .setWheelItemTextSelectorColor(mContext.getResources().getColor(R.color.timepicker_toolbar_bg))
//                .setWheelItemTextSize(12)
//                .setMinInterval(PickerConfig.MinInterval.minFive)
//                .setCenter(true)
//                .setButtonColor(mContext.getResources().getColor(R.color.black))
//                .build();
//
//        }
//       tpdMins.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "min");
    }
    private void beforeConfig() {
        List<SettingConfig> configs = DataSupport.findAll(SettingConfig.class);
        SettingConfig localConfig = null;
        for (SettingConfig config : configs) {
            if (TextUtils.equals(config.getMeetingId(), H2HConference.getInstance().getMeetingId()) &&
                    TextUtils.equals(config.getUserId(), H2HConference.getInstance().getLocalUserName())) {
                localConfig = config;
                break;
            }
        }
        if (localConfig == null) {
            localConfig = new SettingConfig();
            localConfig.setMeetingId(H2HConference.getInstance().getMeetingId());
            localConfig.setUserId(H2HConference.getInstance().getLocalUserName());
            localConfig.setHost(MRUtils.isHost());
            localConfig.save();
        }
//        rlChat.setVisibility(localConfig.isHost()?View.VISIBLE:View.GONE);
//        rlRaisehand.setVisibility(localConfig.isHost()?View.VISIBLE:View.GONE);
        switchshowTimer.setChecked(localConfig.isTimer());
//        rlChat.setVisibility(MRUtils.isHost()?View.VISIBLE:View.GONE);
//        rlRaisehand.setVisibility(MRUtils.isHost()?View.VISIBLE:View.GONE);
    }

    private void afterConfig() {
        List<SettingConfig> configs = DataSupport.findAll(SettingConfig.class);
        for (SettingConfig config : configs) {
            if (TextUtils.equals(config.getMeetingId(), H2HConference.getInstance().getMeetingId()) &&
                    TextUtils.equals(config.getUserId(), H2HConference.getInstance().getLocalUserName())) {
                config.setAllowChat(switchChat.isChecked());
                config.setAllowRaiseHand(switchRaisehand.isChecked());
                config.setAllowOverlay(switchChatOverlay.isChecked());
                config.setTimer(switchshowTimer.isChecked());
                break;
            }
        }
    }
}
