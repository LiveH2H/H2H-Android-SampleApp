package itutorgroup.h2h.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HModel;
import com.mosai.utils.Tools;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.utils.StringUtil;

public class JoinMeetingActivity extends MeetingRoomBaseActivity {
    private static final int PERMISSION_CAMERA = 0;
    private static final int PERMISSION_RECORD_AUDIO = 1;
    private EditText etMeetingId, etEmailAddress;
    private String email;
    private Button joinMeetingBtn;
    private ProgressBar spinner;
    private H2HHttpRequest h2HHttpRequest;

    @Override
    protected void initDatas() {
    }

    @Override
    protected void initView() {
        etMeetingId = (EditText) findViewById(R.id.meetingidEditText);
        etEmailAddress = (EditText) findViewById(R.id.emailAddressEditText);
        joinMeetingBtn = (Button) findViewById(R.id.joinMeetingBtn);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        Intent intent = getIntent();
        String meetingId = intent.getStringExtra("meetingId");
        if (meetingId!=null){
            etMeetingId.setText(meetingId);
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
                String email = etMeetingId.getText().toString().replace("-","");
                if(!TextUtils.isEmpty(email)&&!TextUtils.equals(email,JoinMeetingActivity.this.email)){
                    JoinMeetingActivity.this.email=email;
                    etMeetingId.setText(StringUtil.formatMeetingId(email));
                    etMeetingId.setSelection(StringUtil.formatMeetingId(email).length());
                }
            }
        });
    }


    private void cancelJoinMeeting(){
        spinner.setVisibility(View.INVISIBLE);
        joinMeetingBtn.setClickable(true);
    }

    public void joinMeetingClicked(View view) {

        spinner.setVisibility(View.VISIBLE);
        joinMeetingBtn.setClickable(false);
        String meetingId = getIntent().getStringExtra("meetingId");
        if (TextUtils.isEmpty(etMeetingId.getText().toString().trim()) ) {
            showToast(getString(R.string.meetingId_tip));
            cancelJoinMeeting();
            return;
        }

        if (TextUtils.isEmpty(etEmailAddress.getText().toString().trim()) && meetingId == null) {
            showToast(getString(R.string.emailAddress_tip));
            cancelJoinMeeting();
            return;
        }

        if (!Tools.isConnect(context)) {
            showToast(getString(R.string.check_network_tip));
            cancelJoinMeeting();
            return;
        }
        requestAudioPermission();
    }

    private void launchMeeting(String origin,String serverURL, final String userToken) {

        final H2HModel model = H2HModel.getInstance();
        model.getLaunchParameters(origin, serverURL, userToken, new H2HCallback() {
            @Override
            public void onCompleted(Exception ex, H2HCallBackStatus status) {
                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                    H2HConference.getInstance().connect(JoinMeetingActivity.this, new H2HCallback() {
                        @Override
                        public void onCompleted(Exception ex, H2HCallBackStatus status) {
                            if (ex != null) {
                                Log.e("App Level", ex.getMessage());
                            } else {
                                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                    Log.d("App Level", "Launch a etMeetingId");
                                    if(TextUtils.equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName(),JoinMeetingActivity.class.getSimpleName())){
                                        Intent intent = new Intent(JoinMeetingActivity.this, MeetingActivity.class);
                                        ServerConfig serverConfig = new ServerConfig();
                                        serverConfig.userToken = userToken;
                                        intent.putExtra("serverConfig", serverConfig);
                                        startActivityForResult(intent,0);
                                    }
                                }
                            }
                        }
                    });
                    H2HChat.getInstance().connect(JoinMeetingActivity.this, new H2HCallback() {
                        @Override
                        public void onCompleted(Exception ex, H2HCallBackStatus status) {
                            if (ex != null) {
                                Log.e("App Level", ex.getMessage());
                            } else {
                                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                    Log.e("App Level", "start chat");
                                } else {
                                    Log.e("App Level", "something went wrong");
                                }
                            }
                        }
                    });
                } else if (status == H2HCallBackStatus.H2HCallBackStatusFail) {
                    Toast.makeText(JoinMeetingActivity.this, "Unable to join etMeetingId", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ((ContextCompat.checkSelfPermission(context,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {

                    String email =  etEmailAddress.getText().toString().trim();
                    h2HHttpRequest = H2HHttpRequest.getInstance();
                    h2HHttpRequest.joinMeeting(
                            etMeetingId.getText().toString().trim().replace("-", ""),
                            email,
                            new H2HCallback() {
                        @Override
                        public void onCompleted(Exception ex, H2HCallBackStatus status) {
                            if (status == H2HCallBackStatus.H2HCallBackStatusOK){
                                launchMeeting(h2HHttpRequest.getOrigin(),h2HHttpRequest.getServerURL(),h2HHttpRequest.getUserToken());
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(JoinMeetingActivity.this,"Meeting Not Found, Please Enter an Exiting Meeting id",Toast.LENGTH_SHORT).show();
                                        cancelJoinMeeting();
                                    }
                                });
                            }
                        }
                    });
            } else {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestAudioPermission();
                }
            }
        }
    };


    @PermissionGrant(PERMISSION_CAMERA)
    public void requestCameraSuccess() {
        handler.sendEmptyMessage(0);
    }

    @PermissionDenied(PERMISSION_CAMERA)
    public void requestCameraFaild() {
    }

    @PermissionGrant(PERMISSION_RECORD_AUDIO)
    public void requestAudioSuccess() {
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)){
            handler.sendEmptyMessage(0);
        }else {
            requestCameraPermission();
        }
    }

    @PermissionDenied(PERMISSION_RECORD_AUDIO)
    public void requestAudioFailed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestCameraPermission() {
        MPermissions.requestPermissions(this, PERMISSION_CAMERA, Manifest.permission.CAMERA);
    }

    private void requestAudioPermission() {
        MPermissions.requestPermissions(this, PERMISSION_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
