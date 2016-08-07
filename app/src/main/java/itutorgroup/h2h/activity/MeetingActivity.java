package itutorgroup.h2h.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;

import itutorgroup.h2h.R;
import itutorgroup.h2h.adapter.MeetingFragmentAdapter;
import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.utils.NotificationUtil;
import itutorgroup.h2h.view.BottomNavigatorView;
import itutorgroup.h2h.view.FragmentNavigator;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 15:34
 * 邮箱：nianbin@mosainet.com
 */
public class MeetingActivity extends MeetingRoomBaseActivity implements BottomNavigatorView.OnBottomNavigatorViewItemClickListener,MeetingRoomBaseActivity.NetworkChangedInterface {
    private MaterialDialog mdLogout;
    public FragmentNavigator mNavigator;
    private BottomNavigatorView bottomNavigatorView;
    private ServerConfig serverConfig;
    @Override
    protected void initDatas() {

    }

    @Override
    protected void initView() {
        bottomNavigatorView = (BottomNavigatorView) findViewById(R.id.bottomNavigatorView);
        if (bottomNavigatorView != null) {
            bottomNavigatorView.setOnBottomNavigatorViewItemClickListener(this);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG,"onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
        outState.putSerializable("serverConfig",serverConfig);
        Log.e(TAG,"onSaveInstanceState");
    }
    @Override
    protected void initDatas(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,(savedInstanceState==null)+"");
        if(savedInstanceState!=null){
            serverConfig = (ServerConfig) savedInstanceState.getSerializable("serverConfig");
            Log.e(TAG,serverConfig.toString());
        }else{
            serverConfig = (ServerConfig) getIntent().getSerializableExtra("serverConfig");
        }
        Log.e(TAG,(serverConfig==null)+"");
        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new MeetingFragmentAdapter(serverConfig), R.id.container);
        mNavigator.onCreate(savedInstanceState);
        setCurrentTab(MeetingFragmentAdapter.fragment.conference.ordinal());
//        setCurrentTab(MeetingFragmentAdapter.fragment.participants.ordinal());
//        setCurrentTab(MeetingFragmentAdapter.fragment.chat.ordinal());
//        setCurrentTab(MeetingFragmentAdapter.fragment.conference.ordinal());
    }

    @Override
    protected int setContent() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.activity_meeting_meeting;
    }

    @Override
    protected void addListener() {
        mdLogout = new MaterialDialog(context)
                .setMessage(context.getString(R.string.logout_tips))
                .setPositiveButton(context.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                        H2HChat.getInstance().leaveRoom();
                        H2HConference.getInstance().closeAllConnections();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                    }
                });
    }
    private void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        bottomNavigatorView.select(position);
    }

    @Override
    public void onBottomNavigatorViewItemClick(int position, View view) {
        setCurrentTab(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationUtil.getInstance().cancelAll();
//        H2HChat.getInstance().leaveRoom();
        Log.e(TAG,"onDestroy");
    }

    @Override
    public void onBackPressed() {
        mdLogout.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG,"onNewIntent");
        setCurrentTab(MeetingFragmentAdapter.fragment.chat.ordinal());
    }

    @Override
    public void networkChanged(boolean isAvailable) {
    }

    @Override
    protected boolean openEventBus() {
        return false;
    }
}
