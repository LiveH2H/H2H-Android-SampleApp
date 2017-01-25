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

public class SignupActivity extends MeetingRoomBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_signup;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }

    public void signupClicked(View view) {
        String firstName = ((EditText) findViewById(R.id.firstNameEditText)).getText().toString().trim();
        String lastName = ((EditText) findViewById(R.id.lastNameEditText)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String pwd = ((EditText) findViewById(R.id.pwdEditText)).getText().toString();
        if (firstName.length() > 0 && lastName.length() > 0 && email.length() > 0 && pwd.length() >= 5 && pwd.length() <= 19) {
            showLoadingDialog();
            H2HHttpRequest.getInstance().signUpH2HUser(firstName, lastName, email, pwd, new H2HCallback() {

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
                                showToast("User Sign Up Success");
                            } else {
                                final String message = "Sign Up Failed"
                                        + response != null && !TextUtils.isEmpty(response.message)
                                        ? ": " + response.message : "";

                                showToast(message);
                            }
                        }
                    });
                }
            });
        } else {
            showToast("Please fill all the blanks, password must between 5-19 characters");
        }
    }
}
