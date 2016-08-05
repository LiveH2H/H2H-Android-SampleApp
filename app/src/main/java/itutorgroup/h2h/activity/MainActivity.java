package itutorgroup.h2h.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HConferenceConstant;
import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HModel;
import com.mosai.utils.DensityUtil;
import com.mosai.utils.Tools;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.utils.StringUtil;
import itutorgroup.h2h.utils.ViewUtil;


public class MainActivity extends BaseActivity {
    private static final int PERMISSION_CAMERA = 0;
    private static final int PERMISSION_RECORD_AUDIO = 1;
    public static final String BASE_URL = "https://app.liveh2h.com/tutormeetweb/corpmeeting";
    public String userToken;
    public String serverURL;
    public String origin;
    public String serverUrl = "meet1.liveh2h.com";
    public List<String> serverUrls;
    private EditText etMeetingId, etDisplayName, etEmailAddress;
    private Spinner srDomain;
    private String email;
    private Button joinMeetingBtn;
    private ProgressBar spinner;

    @Override
    protected void initDatas() {
        serverUrls = Arrays.asList(getResources().getStringArray(R.array.serverUrl));
    }

    @Override
    protected void initView() {
        srDomain = ViewUtil.findViewById(this, R.id.sr_domain);
        etMeetingId = (EditText) findViewById(R.id.meetingidEditText);
        etDisplayName = (EditText) findViewById(R.id.displayNameEditText);
        etEmailAddress = (EditText) findViewById(R.id.emailAddressEditText);
        joinMeetingBtn = (Button) findViewById(R.id.joinMeetingBtn);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelJoinMeeting();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_main;
    }

    @Override
    protected void addListener() {
        ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, serverUrls);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srDomain.setAdapter(adapter);
        srDomain.setDropDownVerticalOffset(DensityUtil.dip2px(mContext, 40));
        srDomain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                serverUrl = serverUrls.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                if(!TextUtils.isEmpty(email)&&!TextUtils.equals(email,MainActivity.this.email)){
                    MainActivity.this.email=email;
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
        if (TextUtils.isEmpty(etMeetingId.getText().toString().trim())) {
            showToast(getString(R.string.meetingId_tip));
            cancelJoinMeeting();
            return;
        }
        if (TextUtils.isEmpty(etDisplayName.getText().toString().trim())) {
            showToast(getString(R.string.displayName_tip));
            cancelJoinMeeting();
            return;
        }
        if (TextUtils.isEmpty(etEmailAddress.getText().toString().trim())) {
            showToast(getString(R.string.emailAddress_tip));
            cancelJoinMeeting();
            return;
        }
        //If user hasn't logged in, userToken must be null
        if (!Tools.isConnect(mContext)) {
            showToast(getString(R.string.check_network_tip));
            cancelJoinMeeting();
            return;
        }
        requestAudioPermission();
        requestCameraPermission();
    }

    private void launchMeeting(String serverURL, final String userToken, String origin) {

        final H2HModel model = H2HModel.getInstance();
        model.getLaunchParameters(origin, serverURL, userToken, new H2HCallback() {
            @Override
            public void onCompleted(Exception ex, H2HCallBackStatus status) {
                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                    H2HConference.getInstance().connect(MainActivity.this, new H2HCallback() {
                        @Override
                        public void onCompleted(Exception ex, H2HCallBackStatus status) {
                            if (ex != null) {
                                Log.e("App Level", ex.getMessage());
                            } else {
                                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                    Log.d("App Level", "Launch a etMeetingId");
                                    if(TextUtils.equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName(),MainActivity.class.getSimpleName())){
                                        Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                                        ServerConfig serverConfig = new ServerConfig();
                                        serverConfig.userToken = userToken;
                                        intent.putExtra("serverConfig", serverConfig);
                                        startActivityForResult(intent,0);
                                    }
                                }
                            }
                        }
                    });
                    H2HChat.getInstance().connect(MainActivity.this, new H2HCallback() {
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
                    Toast.makeText(MainActivity.this, "Unable to join etMeetingId", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class GetRTCParametersAsyncTask extends AsyncTask<String, Void, Boolean> {

        JSONObject responseJson = new JSONObject();

        @Override
        protected Boolean doInBackground(String... strings) {

            String meetingId = strings[0];
            String encodedName = strings[1];
            String encodedEmail = strings[2];
            Log.e(encodedName,encodedEmail);
            String requestStr = "{\"meetingId\":\"" + meetingId + "\",\"locale\":\"en\",\"action\":\"joinMeeting\",\"app\":\"meet\",\"name\":\"" + encodedName + "\",\"email\":\"" + encodedEmail + "\"}";
            try {
                requestStr = URLEncoder.encode(requestStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("App Level", e.getMessage());
            }
            HttpGet httpGet = new HttpGet(BASE_URL + ".do?json=" + requestStr);
            HttpClient httpclient = new DefaultHttpClient();
            Log.e("Request url", BASE_URL + ".do?json=" + requestStr);

            try {
                HttpResponse response = httpclient.execute(httpGet);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    responseJson = new JSONObject(data);
                    Log.e("Request response", responseJson.toString());
                    return true;
                }
            } catch (IOException e) {
                Log.e(H2HConferenceConstant.LOGTAG, e.getMessage());
                return false;
            } catch (JSONException e) {
                Log.e(H2HConferenceConstant.LOGTAG, e.getMessage());
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean didSuccess) {
            cancelJoinMeeting();
            if (didSuccess) {
                try {
                    int returnCode = responseJson.getInt("returnCode");
                    if (returnCode == 0) {
                        serverURL = responseJson.getString("serverURL");
                        userToken = responseJson.getString("meetingUri");
                        origin = responseJson.getString("origin");
                        launchMeeting(serverURL, userToken, origin);
                    } else {
                        String message = responseJson.getString("message");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("App Level", e.getMessage());
                }
            } else {
                Log.e("App Level", "Api failed");
            }
        }
    }
    private GetRTCParametersAsyncTask getRTCParametersAsyncTask;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ((ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                if(getRTCParametersAsyncTask==null){
                    Log.e(TAG,"open");
                    getRTCParametersAsyncTask = new GetRTCParametersAsyncTask();
                    getRTCParametersAsyncTask.execute(etMeetingId.getText().toString().trim().replace("-",""), etDisplayName.getText().toString().trim(), etEmailAddress.getText().toString().trim());
                }
            } else {
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestAudioPermission();

                }
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    requestCameraPermission();
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
        handler.sendEmptyMessage(0);
    }

    @PermissionGrant(PERMISSION_RECORD_AUDIO)
    public void requestAudioSuccess() {
        handler.sendEmptyMessage(0);
    }

    @PermissionDenied(PERMISSION_RECORD_AUDIO)
    public void requestAudioFailed() {
        handler.sendEmptyMessage(0);
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
        if(resultCode==RESULT_OK){
            if(getRTCParametersAsyncTask!=null){
                getRTCParametersAsyncTask=null;
            }
        }
    }
}
