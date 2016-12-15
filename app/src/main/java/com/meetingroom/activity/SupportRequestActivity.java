package com.meetingroom.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.itutorgroup.h2hSupport.H2HSupportManager;
import com.itutorgroup.h2hmodel.H2HModel;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.ResourcesUtil;
import com.meetingroom.widget.SwitchButtonLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.BuildConfig;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.StringUtil;
import itutorgroup.h2h.utils.ViewUtil;

public class SupportRequestActivity extends BaseTextHeaderActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView tvSubmitted, tvVersion, header_title, header_right;
    private RadioGroup rgContacts;
    private EditText etContacts, etComments;
    private SwitchButtonLayout sblVolumeHigh, sblVolumeLow, sblEcho, sblMaterials, sblDelay,
            sblWhiteboard, sblNoVideo, sblNoise, sblNoSound, sblOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_support_request);
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        header_title = ViewUtil.findViewById(this, R.id.header_title);
        header_right = ViewUtil.findViewById(this, R.id.header_right);
        tvSubmitted = ViewUtil.findViewById(this, R.id.tvSubmitted);
        tvVersion = ViewUtil.findViewById(this, R.id.tvVersion);
        rgContacts = ViewUtil.findViewById(this, R.id.rgContacts);
        etComments = ViewUtil.findViewById(this, R.id.etComments);
        etContacts = ViewUtil.findViewById(this, R.id.etContacts);
        sblVolumeHigh = ViewUtil.findViewById(this, R.id.sblVolumeHigh);
        sblVolumeLow = ViewUtil.findViewById(this, R.id.sblVolumeLow);
        sblEcho = ViewUtil.findViewById(this, R.id.sblEcho);
        sblMaterials = ViewUtil.findViewById(this, R.id.sblMaterials);
        sblDelay = ViewUtil.findViewById(this, R.id.sblDelay);
        sblWhiteboard = ViewUtil.findViewById(this, R.id.sblWhiteboard);
        sblNoVideo = ViewUtil.findViewById(this, R.id.sblNoVideo);
        sblNoise = ViewUtil.findViewById(this, R.id.sblNoise);
        sblNoSound = ViewUtil.findViewById(this, R.id.sblNoSound);
        sblOther = ViewUtil.findViewById(this, R.id.sblOther);
    }

    private void initListener() {
        rgContacts.setOnCheckedChangeListener(this);
        header_title.setOnClickListener(this);
        header_right.setOnClickListener(this);
    }

    private void initData() {
        H2HModel model = H2HModel.getInstance();

        header_title.setText(getString(R.string.support_request_title, StringUtil.space(model.getMeetingId(), "-", 3)));
        header_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_default));
        header_right.setText(R.string.submit);
        header_right.setTextColor(ResourcesUtil.getColor(this, R.color.blue_button));
        header_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_small));
        header_right.setVisibility(View.VISIBLE);

        tvSubmitted.setText(getString(R.string.support_request_submitted_by, model.getRealDisplayName()));
        tvVersion.setText(getString(R.string.support_request_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_title:
                finish();
                break;
            case R.id.header_right:
                submit();
                break;
        }
    }

    private void submit() {
        List<String> reasonsList = new ArrayList<>();
        getCheckedReasons(reasonsList);
        if (reasonsList.isEmpty()) {
            showToast(R.string.support_request_reason_check);
            return;
        }

        String comments = etComments.getText().toString();
        final String contacts = etContacts.getText().toString();
        showLoadingDialog();
        H2HSupportManager.getInstance().submitSupportRequest(reasonsList, comments, contacts, new AsyncHttpClient.JSONObjectCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, JSONObject result) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingDialog();
                if (e != null) {
                    e.printStackTrace();
                    showToast(R.string.support_request_submit_fail);
                    return;
                }
                LogUtils.i(result == null ? "result is null" : result.toString());
                if (result != null && result.has("h2h_return")) {
                    showToast(R.string.submit_request_success);
                    finish();
                } else {
                    showToast(R.string.support_request_submit_fail);
                }
            }
        });
    }

    private void getCheckedReasons(List<String> reasonsList) {
        if (sblVolumeHigh.isChecked()) {
            reasonsList.add(sblVolumeHigh.getValue());
        }
        if (sblVolumeLow.isChecked()) {
            reasonsList.add(sblVolumeLow.getValue());
        }
        if (sblEcho.isChecked()) {
            reasonsList.add(sblEcho.getValue());
        }
        if (sblMaterials.isChecked()) {
            reasonsList.add(sblMaterials.getValue());
        }
        if (sblDelay.isChecked()) {
            reasonsList.add(sblDelay.getValue());
        }
        if (sblWhiteboard.isChecked()) {
            reasonsList.add(sblWhiteboard.getValue());
        }
        if (sblNoVideo.isChecked()) {
            reasonsList.add(sblNoVideo.getValue());
        }
        if (sblNoise.isChecked()) {
            reasonsList.add(sblNoise.getValue());
        }
        if (sblNoSound.isChecked()) {
            reasonsList.add(sblNoSound.getValue());
        }
        if (sblOther.isChecked()) {
            reasonsList.add(sblOther.getValue());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbWeChat:
                etContacts.setHint(R.string.support_request_wechat_input_hint);
                break;
            case R.id.rbPhone:
                etContacts.setHint(R.string.support_request_phone_input_hint);
                break;
        }
    }

    @Override
    protected boolean isDialogTheme() {
        return true;
    }
}
