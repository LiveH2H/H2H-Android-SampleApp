package itutorgroup.h2h.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}
