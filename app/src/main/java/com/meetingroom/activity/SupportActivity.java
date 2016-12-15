package com.meetingroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itutorgroup.h2hSupport.H2HSupportManager;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.widget.SupportListDialog;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * Created by Rays on 16/7/8.
 */
public class SupportActivity extends BaseTextHeaderActivity implements View.OnClickListener,SupportListDialog.SendResultCallback{
    private View tvCamera, tvMicrophone, tvConnection, tvOthers, tvBoard, tvInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_support);
        initViews();
        initListener();
        initData();
    }

    private void initViews() {
        tvCamera = ViewUtil.findViewById(this, R.id.tvCamera);
        tvMicrophone = ViewUtil.findViewById(this, R.id.tvMicrophone);
        tvConnection = ViewUtil.findViewById(this, R.id.tvConnection);
        tvOthers = ViewUtil.findViewById(this, R.id.tvOthers);
        tvBoard = ViewUtil.findViewById(this, R.id.tvBoard);
        tvInvitation = ViewUtil.findViewById(this, R.id.tvInvitation);
    }

    private void initListener() {
        tvCamera.setOnClickListener(this);
        tvMicrophone.setOnClickListener(this);
        tvConnection.setOnClickListener(this);
        tvOthers.setOnClickListener(this);
        tvBoard.setOnClickListener(this);
        tvInvitation.setOnClickListener(this);
    }

    private void initData() {
        setHeaderTitle(R.string.support);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCamera:
                showSupportListDialog(R.array.support_camera, R.string.support_item_camera);
                break;
            case R.id.tvMicrophone:
                showSupportListDialog(R.array.support_microphone, R.string.support_item_microphone);
                break;
            case R.id.tvConnection:
                showSupportListDialog(R.array.support_connection, R.string.support_item_connection);
                break;
            case R.id.tvOthers:
//                showSupportListDialog(R.array.support_o, R.string.support_item_others);
//                startActivity(new Intent(context, SupportChatActivity.class));
                turnToSupportChat();
                break;
            case R.id.tvBoard:
                showSupportListDialog(R.array.support_board, R.string.support_item_board);
                break;
            case R.id.tvInvitation:
                showSupportListDialog(R.array.support_invitation, R.string.support_item_invitation);
                break;
        }
    }

    private void turnToSupportChat() {
        Intent intent = new Intent();
        intent.putExtra(MeetingConstants.supportForOther,true);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void showSupportListDialog(int issues, int title) {
        SupportListDialog dialog = new SupportListDialog(context);
        dialog.changeValue(issues, getString(R.string.support_dialog_title, getString(title)));
        dialog.show();
    }

    @Override
    protected boolean isDialogTheme() {
        return true;
    }

    @Override
    public void sendContent(String issues) {
        H2HSupportManager.getInstance().sendMessage(issues);
        turnToSupportChat();
    }
}
