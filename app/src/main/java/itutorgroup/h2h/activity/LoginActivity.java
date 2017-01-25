package itutorgroup.h2h.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HResponse;

import itutorgroup.h2h.R;

public class LoginActivity extends MeetingRoomBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {
    }

    @Override
    protected int setContent() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void addListener() {

    }

    public void signinClicked(View view) {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String pwd = ((EditText) findViewById(R.id.pwdEditText)).getText().toString();
        if (email.length()>0 && pwd.length()>0){
            showLoadingDialog();
            H2HHttpRequest.getInstance().loginH2H(email, pwd, new H2HCallback() {
                @Override
                public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response) {
                    if (isFinishing()) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                showToast("Login Success");
                            } else {
                                final String message = "Failed to Login" + response != null && !TextUtils.isEmpty(response.message)
                                        ? ": " + response.message : "";

                                showToast(message);
                            }
                        }
                    });
                }
            });
        }else {
            showToast("Please fill all the blanks");
        }
    }
}
