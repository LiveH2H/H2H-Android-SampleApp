package itutorgroup.h2h.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;

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

        String userName = ((EditText) findViewById(R.id.userNameEditText)).getText().toString().trim();
        String firstName = ((EditText) findViewById(R.id.firstNameEditText)).getText().toString().trim();
        String lastName = ((EditText) findViewById(R.id.lastNameEditText)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String pwd = ((EditText) findViewById(R.id.pwdEditText)).getText().toString();
        if (userName.length()>0 && firstName.length()>0 && lastName.length()>0 && email.length()>0 && pwd.length()>=5 && pwd.length()<=19){
            H2HHttpRequest.getInstance().signUpH2HUser(userName, firstName, lastName, email, pwd, new H2HCallback() {
                @Override
                public void onCompleted(Exception ex, H2HCallBackStatus status) {
                    if (status==H2HCallBackStatus.H2HCallBackStatusOK){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignupActivity.this,"User Sign Up Success",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignupActivity.this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(this,"Please fill all the blanks, password must between 5-19 characters",Toast.LENGTH_LONG).show();
        }
    }
}
