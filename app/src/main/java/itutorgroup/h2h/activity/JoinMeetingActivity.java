package itutorgroup.h2h.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.meetingroom.meet.MeetingActions;
import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.SystemUtil;
import com.mosai.utils.Tools;

import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.StringUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class JoinMeetingActivity extends MeetingRoomBaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int PERMISSION_BOTH = 2000;
    private EditText etMeetingId, etEmailAddress;
    private String email;
    private Button joinMeetingBtn;

    @Override
    protected void initDatas() {
    }

    @Override
    protected void initView() {
        etMeetingId = (EditText) findViewById(R.id.meetingidEditText);
        etEmailAddress = (EditText) findViewById(R.id.emailAddressEditText);
        joinMeetingBtn = (Button) findViewById(R.id.joinMeetingBtn);
        String meetingId = getIntent().getStringExtra("meetingId");
        if (meetingId != null) {
            etMeetingId.setText(StringUtil.formatMeetingId(meetingId));
            etEmailAddress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelJoinMeeting();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_meeting_join;
    }

    @Override
    protected void addListener() {

        etMeetingId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = etMeetingId.getText().toString().replace("-", "");
                if (!TextUtils.isEmpty(email) && !TextUtils.equals(email, JoinMeetingActivity.this.email)) {
                    JoinMeetingActivity.this.email = email;
                    etMeetingId.setText(StringUtil.formatMeetingId(email));
                    etMeetingId.setSelection(etMeetingId.length());
                }
            }
        });
    }


    private void cancelJoinMeeting() {
        joinMeetingBtn.setClickable(true);
    }

    public void joinMeetingClicked(View view) {
        joinMeetingBtn.setClickable(false);
        if (TextUtils.isEmpty(etMeetingId.getText().toString().trim())) {
            showToast(getString(R.string.meetingId_tip));
            cancelJoinMeeting();
            return;
        }
        if (TextUtils.isEmpty(etEmailAddress.getText().toString().trim()) && !getIntent().hasExtra("meetingId")) {
            showToast(getString(R.string.emailAddress_tip));
            cancelJoinMeeting();
            return;
        }
        if (!Tools.isConnect(context)) {
            showToast(getString(R.string.check_network_tip));
            cancelJoinMeeting();
            return;
        }
        requestPermission();
    }

    @AfterPermissionGranted(PERMISSION_BOTH)
    private void requestPermission() {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            joinMeeting();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_audio),
                    PERMISSION_BOTH, perms);
        }
    }

    private void joinMeeting() {
        showLoadingDialog();
        String email = etEmailAddress.getText().toString().trim();
        String meetingId = etMeetingId.getText().toString().trim().replace("-", "");
        final H2HHttpRequest h2HHttpRequest = H2HHttpRequest.getInstance();
        h2HHttpRequest.joinMeeting(meetingId, email, new H2HCallback() {

            @Override
            public void onCompleted(Exception ex, final H2HCallBackStatus status) {
                if (isFinishing()) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                            launchMeeting(h2HHttpRequest.getOrigin(), h2HHttpRequest.getServerURL(), h2HHttpRequest.getUserToken());
                        } else {
                            showToast("Meeting Not Found, Please Enter an Exiting Meeting id");
                        }
                        dismissLoadingDialog();
                        cancelJoinMeeting();
                    }
                });
            }
        });
    }

    private void launchMeeting(String origin, String serverURL, final String userToken) {
        Intent intent;
        if (SystemUtil.isTablet(this)) {
            intent = new Intent(this, com.meetingroom.activity.flat.FlatMeetingActivity.class);
        } else {
            intent = new Intent(this, com.meetingroom.activity.MeetingActivity.class);
        }
        com.meetingroom.bean.ServerConfig serverConfig = new com.meetingroom.bean.ServerConfig();
        serverConfig.userToken = userToken;
        serverConfig.origin = origin;
        serverConfig.serverURL = serverURL;
        intent.putExtra("serverConfig", serverConfig);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtils.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtils.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        MeetingActions.checkMeetingPermission(this, perms, PERMISSION_BOTH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
