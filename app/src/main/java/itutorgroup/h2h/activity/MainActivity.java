package itutorgroup.h2h.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HUserManager;

import java.util.TooManyListenersException;

import itutorgroup.h2h.R;


public class MainActivity extends MeetingRoomBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }

    public void joinBtnClicked(View view) {
        startActivity(new Intent(this,JoinMeetingActivity.class));
    }

    public void instantMeetingBtnClicked(View view) {
        startActivity(new Intent(this,InstantMeetingActivity.class));
    }

    public void signupBtnClicked(View view) {
        startActivity(new Intent(this,SignupActivity.class));
    }

    public void loginBtnClicked(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void scheduleBtnClicked(View view) {
        if (!H2HUserManager.getInstance().didLogin()){
            Toast.makeText(MainActivity.this,"You need to login first",Toast.LENGTH_SHORT).show();
        }else {
            startActivity(new Intent(this,ScheduleMeetingActivity.class));
        }
    }

    public void signoutBtnClicked(View view) {
        H2HHttpRequest.getInstance().logoutH2H(new H2HCallback() {
            @Override
            public void onCompleted(Exception ex, H2HCallBackStatus status) {
                Toast.makeText(MainActivity.this,"You have signed out",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
