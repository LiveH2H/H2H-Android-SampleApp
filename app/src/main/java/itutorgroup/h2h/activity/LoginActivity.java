package itutorgroup.h2h.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;

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
            H2HHttpRequest.getInstance().loginH2H(email, pwd, new H2HCallback() {
                @Override
                public void onCompleted(Exception ex, H2HCallBackStatus status) {
                    if (status == H2HCallBackStatus.H2HCallBackStatusOK){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"Failed to Login",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }else {
            Toast.makeText(this,"Please fill all the blanks",Toast.LENGTH_SHORT).show();
        }
    }
}
