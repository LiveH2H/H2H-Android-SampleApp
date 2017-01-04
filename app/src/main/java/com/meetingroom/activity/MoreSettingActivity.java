package com.meetingroom.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hmodel.H2HFeatures;
import com.kyleduo.switchbutton.SwitchButton;
import com.meetingroom.bean.SettingConfig;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.utils.MRUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

public class MoreSettingActivity extends MeetingRoomBaseActivity {

    @BindView(R.id.tv_set_time)
    TextView tvSetTime;
    @BindView(R.id.tv_live_support)
    TextView tvLiveSupport;
//    @BindView(R.id.tv_submit_support_request)
//    TextView tvSubmitSupportRequest;
    @BindView(R.id.tv_live_transcript)
    TextView tvLiveTranscript;
    @BindView(R.id.switchChat)
    SwitchButton switchChat;
    @BindView(R.id.switchRaisehand)
    SwitchButton switchRaisehand;
    @BindView(R.id.switchshowTimer)
    SwitchButton switchshowTimer;
    @BindView(R.id.switchChatOverlay)
    SwitchButton switchChatOverlay;
    @BindView(R.id.tv_particiipant_setting)
    TextView tvParticiipantSetting;
    @BindView(R.id.tv_device_setting)
    TextView tvDeviceSetting;
    @BindView(R.id.tv_theme_color)
    TextView tvThemeColor;
    @BindView(R.id.rl_chat_overlay)
    RelativeLayout rlChatOverlay;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    @BindView(R.id.rl_raisehand)
    RelativeLayout rlRaisehand;

//    private TimePickerDialog tpdMins;

    @Override
    protected void initDatas() {
        beforeConfig();
        ViewUtil.setVisibility(tvLiveSupport, H2HFeatures.isLiveSupportEnabled() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int setContent() {
        return R.layout.activity_more_setting;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

//    private void showTpdMins() {
//        if(tpdMins==null){
//            tpdMins = new TimePickerDialog.Builder()
//                    .setCallBack(new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(TimePickerDialog timePickerView, long millseconds,int result) {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis(millseconds);
//                            tvSetTime.setText(String.format("%d %s",(calendar.get(Calendar.MINUTE)+1)*5,getString(R.string.minute)));
//                        }
//                    })
//                    .setCancelStringId(getString(R.string.cancel))
//                    .setSureStringId(getString(R.string.sure))
//                    .setTitleStringId(getString(R.string.time_picker))
//                    .setMinuteText(getString(R.string.minute))
//                    .setCyclic(true)
//                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
//                    .setType(Type.MIN)
//                    .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
//                    .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
//                    .setWheelItemTextSize(12)
//                    .setMinInterval(PickerConfig.MinInterval.minFive)
//                    .build();
//        }
//        tpdMins.show(getSupportFragmentManager(), "min");
//    }

    @Override
    protected void addListener() {
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
    }


    @OnClick({R.id.tv_live_support, R.id.tv_live_transcript, R.id.tv_particiipant_setting,
            R.id.tv_device_setting, R.id.tv_theme_color, R.id.tv_set_time,R.id.iv_back, R.id.tv_submit_support_request})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_live_support:
//                showToast("coming soon");
                startActivityForResult(new Intent(context, SupportActivity.class),0);
                break;
            case R.id.tv_submit_support_request:
                startActivity(new Intent(context, SupportRequestActivity.class));
                break;
            case R.id.tv_live_transcript:
                break;
            case R.id.tv_particiipant_setting:
                break;
            case R.id.tv_device_setting:
                break;
            case R.id.tv_theme_color:
                startActivity(new Intent(context, ChangeColorThemeActivity.class));
                break;
            case R.id.tv_set_time:
//                showTpdMins();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
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
    public void ok(View view){
        afterConfig();
        if(MRUtils.isHost()){
            H2HConference.getInstance().toggleChatPermission(switchChat.isChecked());
            H2HConference.getInstance().toggleRaisehandPermission(switchRaisehand.isChecked());
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null&&resultCode==RESULT_OK){
            if(data.hasExtra(MeetingConstants.supportForOther)){
                Intent intent = new Intent();
                intent.putExtra(MeetingConstants.supportForOther,true);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }
}
