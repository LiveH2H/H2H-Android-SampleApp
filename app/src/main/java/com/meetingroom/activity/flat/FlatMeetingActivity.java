package com.meetingroom.activity.flat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itutorgroup.h2hSupport.H2HSupportManager;
import com.itutorgroup.h2hchat.H2HBroadcastReceiver;
import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hchat.H2HChatConstant;
import com.itutorgroup.h2hchat.H2HChatMessage;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hconference.H2HRTCListener;
import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HFeatures;
import com.itutorgroup.h2hmodel.H2HModel;
import com.itutorgroup.h2hmodel.H2HResponse;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardListener;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.meetingroom.activity.MeetingRoomBaseActivity;
import com.meetingroom.bean.ChatMessage;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.bean.poll.summary.Poll;
import com.meetingroom.callback.MDialogmissCallback;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.fragment.WhiteBoardFragment;
import com.meetingroom.fragment.flat.FlatChatRoomDialog;
import com.meetingroom.fragment.flat.FlatConferenceFragment;
import com.meetingroom.fragment.flat.FlatMoreSettingDialog;
import com.meetingroom.fragment.flat.FlatParticipantDialog;
import com.meetingroom.fragment.flat.FlatTranslateSettingDialog;
import com.meetingroom.meet.MeetingActions;
import com.meetingroom.utils.ConferenceUtils;
import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.NotificationUtil;
import com.meetingroom.utils.StringUtil;
import com.meetingroom.utils.ToastUtils;
import com.meetingroom.view.ActionSheetDialog;
import com.meetingroom.view.RateMeetingDialog;
import com.meetingroom.view.RelativeDialog;
import com.meetingroom.widget.AutoDismissMaterialDialog;
import com.meetingroom.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.FastJsonUtils;
import itutorgroup.h2h.utils.ViewUtil;
import me.drakeet.materialdialog.MaterialDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 15:34
 * 邮箱：nianbin@mosainet.com
 */
public class FlatMeetingActivity extends MeetingRoomBaseActivity implements FlatConferenceFragment.FlatConferenceFragmentCallback, EasyPermissions.PermissionCallbacks, MDialogmissCallback


{
    @BindView(R.id.tv_meetingId)
    public TextView tvMeetingId;
    @BindView(R.id.iv_chat_tip)
    ImageView ivChatTip;
    @BindView(R.id.btn_end)
    Button btnEnd;
    @BindView(R.id.ib_mic)
    ImageButton ibMic;
    @BindView(R.id.ib_webcam)
    ImageButton ibWebcam;
    @BindView(R.id.ib_chat)
    ImageButton ibChat;
    @BindView(R.id.ib_whiteboard)
    ImageButton ibWhiteboard;
    @BindView(R.id.ib_hangup)
    ImageButton ibHangup;
    @BindView(R.id.ib_translate)
    ImageButton ibTranslate;
    @BindView(R.id.ib_participants)
    ImageButton ibParticipants;
    @BindView(R.id.ib_raisehand_tips)
    ImageView ibRaisehandTips;
    @BindView(R.id.ib_more)
    ImageButton ibMore;
    @BindView(R.id.rl_title)
    LinearLayout llTitle;
    @BindView(R.id.iv_wifi)
    ImageView ivWifi;
    @BindView(R.id.ib_conference)
    ImageButton ibConference;
    @BindView(R.id.rl_priticipant)
    RelativeLayout rlPriticipant;
    @BindView(R.id.rl_chat)
    View rlChat;

    private H2HBroadcastReceiver chatMessageReceiver;
    private H2HBroadcastReceiver chatUserStatusReceiver;
    private H2HBroadcastReceiver userConnectionStatusReceiver;

    private LoadingDialog loadingDialog;
    private MaterialDialog mdLogout;
    private RateMeetingDialog rateMeetingDialog;


    private FlatParticipantDialog participantDialog;
    private FlatMoreSettingDialog moreSettingDialog;
    private FlatTranslateSettingDialog translateSettingDialog;
    private FlatChatRoomDialog chatRoomDialog;
    private Fragment currentFragment;
    private FlatConferenceFragment conferenceFragment;
    private WhiteBoardFragment whiteBoardFragment;

    private MyHandler mHandler = new MyHandler(this);
    private boolean hasConferenceConnected;
    private boolean isMeetingLauched;
    private ServerConfig serverConfig;
    private String tag = "H2HConference";
    private AutoDismissMaterialDialog startPollDialog, presenceCheckDialog;
    private Poll poll;
    private boolean polling;
    private ArrayList<Poll> polls = new ArrayList<>();
    private boolean presenceChecking;
    private ArrayList<Poll> waitPolls = new ArrayList<>();
    private static final int PERMISSION_BOTH = 2000;
    private boolean isShowWhiteboardLocatingPoll;

    @Override
    protected void initDatas() {
    }


    @Override
    protected void initView() {
        ButterKnife.bind(this);
        serverConfig = (ServerConfig) getIntent().getSerializableExtra("serverConfig");
        ibMic.setSelected(false);
        selectIcon(ibConference);
        tvMeetingId.setText(StringUtil.formatMeetingId(H2HModel.getInstance().getMeetingId()));
        ibHangup.setVisibility(MRUtils.isHost() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initDatas(@Nullable Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            for (Fragment fragment : fm.getFragments()) {
                transaction.remove(fragment);
            }
            transaction.commitNowAllowingStateLoss();
        }
    }

    @Override
    protected int setContent() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.activity_meeting_new_main;
    }

    @Override
    protected void addListener() {
        mdLogout = new MaterialDialog(context)
                .setMessage(context.getString(R.string.logout_tips))
                .setPositiveButton(context.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                        if (loadingDialog.isShowing()) {
                            finish();
                            return;
                        }
                        showRateMeetingDialog();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                    }
                });
        checkPermission();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        destroyResources();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        mdLogout.show();
    }

    //show fragment
    private void show(String tag) {
        if (currentFragment != null && TextUtils.equals(tag, currentFragment.getClass().getSimpleName())) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            conferenceFragment = FlatConferenceFragment.newInstance(serverConfig);
            whiteBoardFragment = WhiteBoardFragment.newInstance(polls, isShowWhiteboardLocatingPoll);
            transaction.add(R.id.container, conferenceFragment, FlatConferenceFragment.class.getSimpleName())
                    .add(R.id.container, whiteBoardFragment, WhiteBoardFragment.class.getSimpleName())
                    .hide(conferenceFragment)
                    .hide(whiteBoardFragment);
            if (H2HFeatures.isVideoEnabled()) {
                fragment = conferenceFragment;
                transaction.show(conferenceFragment);
            } else if (H2HFeatures.isWhiteboardEnabled()) {
                fragment = whiteBoardFragment;
                transaction.show(whiteBoardFragment);
            }
        } else {
            transaction.show(fragment);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    @OnClick({R.id.tv_meetingId, R.id.btn_end, R.id.ib_participants, R.id.ib_translate, R.id.ib_mic,
            R.id.ib_webcam, R.id.ib_chat, R.id.ib_whiteboard, R.id.ib_more, R.id.ib_hangup, R.id.ib_conference})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_hangup:
                if (!MRUtils.isHost()) {
                    if (!H2HConference.getInstance().isRaiseHandEnabled()) {
                        ToastUtils.showToast(context, getString(R.string.raisehand_not_allow));
                        return;
                    }
                }
                showRaiseHand();
                break;
            case R.id.tv_meetingId:
//                showInviteSheetBottom();
                break;
            case R.id.btn_end:
                mdLogout.show();
                break;
            case R.id.ib_participants:
                selectIcon(ibParticipants);
                showParticipantPopupwin(view);
                break;
            case R.id.ib_translate:
                if (H2HModel.getInstance().getMeetingType() == H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
                    return;
                }
                showTranslateSettingPopupwin(view);
                break;
            case R.id.ib_mic:
                changeAudio();
                break;
            case R.id.ib_webcam:
                changeVideo();
                break;
            case R.id.ib_chat:
                selectIcon(ibChat);
                showChatroomDialog();
                ivChatTip.setVisibility(View.GONE);
                break;
            case R.id.ib_whiteboard:
                showWhiteBoardFragment();
                break;
            case R.id.ib_more:
                selectIcon(ibMore);
                showMoreSettingPopupwin(view);
                break;
            case R.id.ib_conference:
                showConferenceFragment();
                break;
        }
    }

    private void showWhiteBoardFragment() {
        selectIcon(ibWhiteboard);
        show(WhiteBoardFragment.class.getSimpleName());
    }

    private void showConferenceFragment() {
        selectIcon(ibConference);
        show(FlatConferenceFragment.class.getSimpleName());
    }


    //control audio
    private void changeAudio() {
        if (H2HModel.getInstance().getMeetingType() == H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            if (ibMic.isSelected()) {
                H2HConference.getInstance().turnOffAudioForUser(H2HModel.getInstance().getRealDisplayName());
                ibMic.setSelected(false);
            } else {
                ToastUtils.showToast(context, getString(R.string.no_permission_mic));
            }
            return;
        }
        if (!ibMic.isSelected()) {
            ConferenceUtils.turnOnAudio();
        } else {
            ConferenceUtils.turnOffAudio();
        }
    }

    //control camera
    private void changeVideo() {
        if (H2HModel.getInstance().getMeetingType() == H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            return;
        }
        if (!ibWebcam.isSelected()) {
            final ActionSheetDialog dialog = new ActionSheetDialog(context).builder().setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem(getString(R.string.open_my_video), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which, String srcItem) {
                            ConferenceUtils.turnOnVideo();
                        }
                    });
            dialog.show();
        } else {
            ActionSheetDialog dialog = new ActionSheetDialog(context).builder().setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem(getString(R.string.stop_my_video), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which, String srcItem) {
                            ConferenceUtils.turnOffVideo();
                        }
                    });
            dialog.show();
        }
    }

    //raise hand tips (special toast)
    public void showRaiseHand() {
        if (!ibHangup.isSelected()) {
            ibHangup.setSelected(true);
            H2HConference.getInstance().raiseHand();
            ToastUtils.showRaiseHandToast(context, new Runnable() {
                @Override
                public void run() {
                    ibHangup.setSelected(false);
                }
            }, false);
        }

    }

    //rate
    private void showRateMeetingDialog() {
        if (rateMeetingDialog == null) {
            rateMeetingDialog = new RateMeetingDialog(context);
            rateMeetingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent();
                    intent.putExtra("hasLogin", false);
//                    intent.putExtra("hasLogin", UserPF.getInstance().getBoolean("IS_LOGIN", false));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        rateMeetingDialog.show();
    }


    private void initParticipantDialog() {
        if (participantDialog == null) {
            participantDialog = new FlatParticipantDialog(context, serverConfig, new FlatParticipantDialog.FlatParticipantPopupWindowCallback() {
                @Override
                public void isRaiseHandCallback(boolean hasRaiseHand) {
                    ibRaisehandTips.setVisibility(hasRaiseHand ? View.VISIBLE : View.GONE);
                }
            });
            participantDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    participantDialog.reset();
                    callback();
                }
            });
        }
    }

    //participants
    private void showParticipantPopupwin(View view) {
        if (participantDialog == null) {
            participantDialog = new FlatParticipantDialog(context, serverConfig, new FlatParticipantDialog.FlatParticipantPopupWindowCallback() {
                @Override
                public void isRaiseHandCallback(boolean hasRaiseHand) {
                    ibRaisehandTips.setVisibility(hasRaiseHand ? View.VISIBLE : View.GONE);
                }
            });
            participantDialog.setDismissListener(new FlatParticipantDialog.DismissListener() {
                @Override
                public void dismiss() {
                    participantDialog.reset();
                    initIcons();
                }
            });
        }
        if (!participantDialog.isShowing()) {
            participantDialog.showOnAnchor(view, RelativeDialog.Orientation.right);
        }
    }

    //more setting
    private void showMoreSettingPopupwin(View view) {
        if (moreSettingDialog == null) {
            moreSettingDialog = new FlatMoreSettingDialog(context, serverConfig);
        }
        if (!moreSettingDialog.isShowing()) {
            moreSettingDialog.showOnAnchor(view, RelativeDialog.Orientation.right);
        }
    }

    //translate setting
    private void showTranslateSettingPopupwin(View view) {
        boolean hasTranslate = false;
        for (H2HPeer h2HPeer : H2HConference.getInstance().getPeers()) {
            if (h2HPeer.userRole() == H2HPeer.UserRole.Translator) {
                hasTranslate = true;
                break;
            }
        }
        if (!hasTranslate) {
            ToastUtils.showToast(context, getString(R.string.no_translator));
            return;
        }
        if (translateSettingDialog == null) {
            translateSettingDialog = new FlatTranslateSettingDialog(context, serverConfig);
        }
        if (!translateSettingDialog.isShowing()) {
            translateSettingDialog.showOnAnchor(view, RelativeDialog.Orientation.right);
        }
    }

    //chatroomdialog
    private void showChatroomDialog() {
        initChatroomDialog();
        if (!chatRoomDialog.isShowing()) {
            chatRoomDialog.show();
        }
    }

    private void initChatroomDialog() {
        if (chatRoomDialog == null) {
            chatRoomDialog = new FlatChatRoomDialog(context);
            chatRoomDialog.setFlatChatRoomDialogCallback(new FlatChatRoomDialog.FlatChatRoomDialogCallback() {
                @Override
                public void getNewMessage(ChatMessage message) {
                    ivChatTip.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSettingConfig();
    }

    @Override
    protected boolean openWahtDog() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        MeetingActions.inviteParticipants(this, resultCode, data);
        if (requestCode == PERMISSION_BOTH) {
            requestPermission();
        } else if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra(MeetingConstants.supportForOther)) {
                showSupporMessage();
            }
        } else if (requestCode == MeetingConstants.startPoll) {
            checkWaitPolls();
        }
    }

    private void afterLaunchMeeting() {
        loadingDialog.dismiss();
        tvMeetingId.setText(StringUtil.formatMeetingId(H2HModel.getInstance().getMeetingId()));

        initVideo();
        initWhiteboard();
        initChat();
        initRaiseHand();
        initLiveTranslator();

        H2HConference.getInstance().listener = new RTCListener();
        initSettingConfig();
        initChatroomDialog();
        initParticipantDialog();
        showConferenceFragment();
        registerBroadcast();
    }

    private void initVideo() {
        ibMic.setSelected(true);
        ibWebcam.setSelected(true);
        if (H2HFeatures.isVideoEnabled()) {
            ViewUtil.setVisibility(ibMic, View.VISIBLE);
            ViewUtil.setVisibility(ibWebcam, View.VISIBLE);
            ViewUtil.setVisibility(ibConference, View.VISIBLE);
            ViewUtil.setVisibility(rlPriticipant, View.VISIBLE);
        } else {
            ViewUtil.setVisibility(ibMic, View.GONE);
            ViewUtil.setVisibility(ibWebcam, View.GONE);
            ViewUtil.setVisibility(ibConference, View.GONE);
            ViewUtil.setVisibility(rlPriticipant, View.GONE);
        }
    }

    private void initWhiteboard() {
        ViewUtil.setVisibility(ibWhiteboard, H2HFeatures.isWhiteboardEnabled() ? View.VISIBLE : View.GONE);
    }

    private void initChat() {
        ViewUtil.setVisibility(rlChat, H2HFeatures.isChatEnabled() ? View.VISIBLE : View.GONE);
    }

    private void initRaiseHand() {
        ibHangup.setVisibility(MRUtils.isHost() || !H2HFeatures.isRaiseHandEnabled() ? View.GONE : View.VISIBLE);
    }

    private void initLiveTranslator() {
        if (H2HModel.getInstance().getMeetingType() == H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST || !H2HFeatures.isLiveTranslatorEnabled()) {
            ViewUtil.setVisibility(ibTranslate, View.GONE);
        } else {
            ViewUtil.setVisibility(ibTranslate, View.VISIBLE);
        }
    }

    private void checkPermission() {
        if (!getIntent().hasExtra("serverConfig")) {
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context, true, new LoadingDialog.BackCallback() {
                @Override
                public void back() {
                    FlatMeetingActivity.this.back();
                }
            });
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
        requestPermission();
    }

    private void launchMeeting() {
        ServerConfig serverConfig = (ServerConfig) getIntent().getSerializableExtra("serverConfig");
        H2HModel.getInstance().getLaunchParameters(serverConfig.origin, serverConfig.serverURL, serverConfig.userToken, context, new H2HCallback() {
            @Override
            public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response) {
                if (isFinishing()) {
                    return;
                }
                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                    H2HConference.getInstance().connect(context, new H2HCallback() {
                        @Override
                        public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response1) {
                            if (isFinishing()) {
                                Log.e("App Level", "isFinishing()");
                                return;
                            }
                            if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                Log.e("App Level", "Launch a etMeetingId");
                                if (!isMeetingLauched) {
                                    isMeetingLauched = true;
                                    hasConferenceConnected = true;
                                    mHandler.sendEmptyMessage(0);
                                    loadingDialog.dismiss();
                                    connectWhiteBoard();
                                    connectSupport();
                                    return;
                                }
                            }
                            if (ex != null) {
                                ex.printStackTrace();
                            }
                            if (!isMeetingLauched) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        launchFail();
                                    }
                                });
                            }

                        }
                    });
                    H2HChat.getInstance().connect(context, new H2HCallback() {
                        @Override
                        public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response1) {
                            if (isFinishing()) {
                                Log.e("App Level", "isFinishing()");
                                return;
                            }
                            if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                Log.e("App Level", "start chat");
                            } else {
                                if (ex != null) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            launchFail();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void flatConferenceFragemtviewCreated() {
//        H2HConference.getInstance().initRTCConnection();
        if (H2HModel.getInstance().getMeetingType() == H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            ibMic.setSelected(false);
            ibWebcam.setSelected(false);
        }
    }

    private void unRegisterBroadcast() {
        if (chatMessageReceiver != null) {
            context.unregisterReceiver(chatMessageReceiver);
            chatMessageReceiver = null;
        }
        if (chatUserStatusReceiver != null) {
            context.unregisterReceiver(chatUserStatusReceiver);
            chatUserStatusReceiver = null;
        }
        if (userConnectionStatusReceiver != null) {
            context.unregisterReceiver(userConnectionStatusReceiver);
            userConnectionStatusReceiver = null;
        }
    }

    private void registerBroadcast() {
        //Register notifications
        if (chatMessageReceiver == null) {
            chatMessageReceiver = new H2HBroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.hasExtra(H2HChatConstant.CLEAR_CHAT)) {
                        receiveClearMessage();
                    } else {
                        //Receive a new message
                        H2HChatMessage chatMessage = (H2HChatMessage) intent.getSerializableExtra(H2HChatConstant.H2HChatMessage);
                        receiveMessage(chatMessage);
                    }
                }
            };
            context.registerReceiver(chatMessageReceiver, new IntentFilter(H2HChatConstant.RECEIVE_CHAT));
        }

        if (chatUserStatusReceiver == null) {
            chatUserStatusReceiver = new H2HBroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //A user status changed
//                H2HChatUser chatUser = (H2HChatUser) intent.getSerializableExtra(H2HChatConstant.H2HChatUser);
                    receiveUserState();
                }
            };
            context.registerReceiver(chatUserStatusReceiver, new IntentFilter(H2HChatConstant.CHAT_USER_STATUS_CHANGE));
        }

        //Register notifications
        if (userConnectionStatusReceiver == null) {
            userConnectionStatusReceiver = new H2HBroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean isConnected = intent.getBooleanExtra(H2HChatConstant.CONNECTION_STATUS, false);
                    receiveUserConnectionStatus(isConnected);
                }
            };
            context.registerReceiver(userConnectionStatusReceiver, new IntentFilter(H2HChatConstant.CONNECTION_STATUS_CHANGE));
        }
        H2HChat.getInstance().startToReceiveMessage();
    }

    private void receiveUserConnectionStatus(boolean isConnected) {
        if (chatRoomDialog != null) {
            ChatMessage message = MRUtils.parseConnectState(context, isConnected);
//            messages.add(message);
            chatRoomDialog.receiveUserConnectionStatus(message);
        }
    }

    private void receiveClearMessage() {
        if (chatRoomDialog != null) {
            chatRoomDialog.receiveClearMessage();
        }
    }

    private void receiveMessage(H2HChatMessage chatMessage) {
        if (chatRoomDialog != null) {
            ChatMessage message = MRUtils.parseMessage(chatMessage);
//            messages.add(message);
            chatRoomDialog.receiveMessage(message);
        }
    }

    private void receiveUserState() {
        if (chatRoomDialog != null) {
//            ChatMessage message = MRUtils.parseUserState(chatUser);
//            messages.add(message);
//            chatRoomDialog.receiveUserState(message);
            chatRoomDialog.receiveUserState();
        }
    }

    public void destroyResources() {
        mHandler.removeCallbacksAndMessages(null);
        H2HSupportManager.getInstance().clientDisconnect();
        H2HConference.getInstance().closeAllConnections();
        H2HChat.getInstance().leaveRoom();
        H2HWhiteboardManager.getInstance().disconnect();
        unRegisterBroadcast();
        NotificationUtil.getInstance().cancelAll(this);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (rateMeetingDialog != null) {
            rateMeetingDialog.dismiss();
        }
        if (startPollDialog != null) {
            startPollDialog.dismiss();
        }
        if (presenceCheckDialog != null) {
            presenceCheckDialog.dismiss();
        }
    }

    public void networkChange(int resId) {
        ivWifi.setImageResource(resId);
    }

    //raise hand tips
    public void showRaiseHand(String name) {
        ToastUtils.showMeetingToast(this, String.format(getString(R.string.raise_hand_tips), name), true, R.color.bg_raisehand_red_tips, -1, R.drawable.ic_raisehand_red, isFullScreen());
    }

    //wifi change tips(yellow)
    public void showWifiYellow() {
        ToastUtils.showMeetingToast(this, getString(R.string.wifi_yellow_tips), true, R.color.bg_wifi_tips, R.color.white, R.drawable.ic_wifi_yellow, isFullScreen());
    }

    //wifi change tips(red)
    public void showWifiRed() {
        ToastUtils.showMeetingToast(this, getString(R.string.wifi_red_tips), true, R.color.bg_wifi_tips, R.color.white, R.drawable.ic_wifi_red, isFullScreen());
    }

    public void showWifiGreen() {
        ToastUtils.showMeetingToast(this, getString(R.string.wifi_green_tips), true, R.color.bg_wifi_tips, R.color.white, R.drawable.ic_wifi_green, isFullScreen());
    }    //chat

    private void updateView() {
        if (conferenceFragment != null && conferenceFragment.isCreated) {
            conferenceFragment.updateView();
        }
    }

    public void raiseHandCallback(H2HPeer h2HPeer) {
        ibRaisehandTips.setVisibility(View.VISIBLE);
        if (participantDialog != null) {
            participantDialog.raiseHand(h2HPeer);
        }
    }

    //check fullScreen
    public boolean isFullScreen() {
        return false;
    }

    public void updateVideoAudioState(String displayName, String type, String action) {
        if (TextUtils.equals(displayName, H2HModel.getInstance().getRealDisplayName())) {
            if (TextUtils.equals(type, "video")) {
                if (TextUtils.equals(action, "enable")) {
                    ibWebcam.setSelected(true);
                } else {
                    ibWebcam.setSelected(false);
                }
            }
            if (TextUtils.equals(type, "audio")) {
                if (TextUtils.equals(action, "enable")) {
                    ibMic.setSelected(true);
                } else {
                    ibMic.setSelected(false);
                }
            }
        }
    }

    private void updateParticipants() {
        if (participantDialog != null) {
            participantDialog.loadDatas();
        }
    }

    private void launchFail() {
        final MaterialDialog mdFail = new MaterialDialog(context);
        mdFail.setCanceledOnTouchOutside(false);
        mdFail.setMessage(getString(R.string.unable_to_join_meeting_id))
                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdFail.dismiss();
                        finish();
                    }
                });
        mdFail.show();
    }


    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FlatMeetingActivity activity = (FlatMeetingActivity) reference.get();
            if (activity != null) {
                if (activity.hasConferenceConnected) {
                    activity.afterLaunchMeeting();
                }
            }
        }
    }

    private class RTCListener extends H2HRTCListener {
        @Override
        public void onConnectivityStrengthChanged(final int connectivityStrength) {
            super.onConnectivityStrengthChanged(connectivityStrength);
            if (connectivityStrength == 0) {
                showWifiGreen();
            } else if (connectivityStrength == 1) {
                showWifiYellow();
            } else {
                showWifiRed();
            }
            if (connectivityStrength == 0) {
                networkChange(R.drawable.ic_wifi_green);
            } else if (connectivityStrength == 1) {
                networkChange(R.drawable.ic_wifi_yellow);
            } else {
                networkChange(R.drawable.ic_wifi_red);
            }

        }


        @Override
        public void onRaiseHandPermissionChanged(Boolean enable) {
            super.onRaiseHandPermissionChanged(enable);
            Log.e(tag, "onRaiseHandPermissionChanged:" + enable);
        }

        @Override
        public void onChatPermissionChanged(Boolean enable) {
            super.onChatPermissionChanged(enable);
            Log.e(tag, "onChatPermissionChanged:" + enable);
        }

        @Override
        public void onAddRemoteStream(final MediaStream remoteStream, final H2HPeer peer) {
            Log.e(tag, peer.getId());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateParticipants();
                    updateView();
                    ToastUtils.showToast(context, String.format(getString(R.string.tip_join_conference), peer.getId()));
                }
            });

        }

        @Override
        public void onPeerConnectionClosed(final H2HPeer peer) {
            Log.e(tag, "onPeerConnectionClosed, number of remotes: " + H2HConference.getInstance().getRemoteRenders().size());
            updateParticipants();
            updateView();
            ToastUtils.showToast(context, String.format(getString(R.string.tip_leave_conference), peer.getId()));
        }


        @Override
        public void onRemoveRemoteStream(final H2HPeer peer) {
            Log.e(tag, "onRemoveRemoteStream");
        }


        @Override
        public void onPeerRaiseHand(final H2HPeer peer) {
            Log.e(tag, "onPeerRaiseHand");
            if (!TextUtils.equals(peer.getId(), H2HConference.getInstance().getLocalUserName())) {
                if (MRUtils.isHost()) {
                    showRaiseHand(peer.getId());
                    raiseHandCallback(peer);
                }
            }
        }

        @Override
        public void onForceLogout(String message) {
            Log.e("tag", "on Force Logout:" + message);
            H2HChat.getInstance().leaveRoom();
            H2HConference.getInstance().closeAllConnections();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.error));
            switch (message) {
                case H2HConference.DUPLICATED_USER:
                    builder.setMessage(getString(R.string.duplicate_user));
                    break;
                case H2HConference.HOST_LEAVE:
                    builder.setMessage(getString(R.string.host_left_the_meeting));
                    break;
                default:
                    builder.setMessage(getString(R.string.host_left_the_meeting));
                    break;
            }
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            });
            builder.show();
        }

        @Override
        public void onToggleMedia(final JSONObject jsonObject) {
            Log.e(tag, "onToggleMedia");
            try {
                String participantName = jsonObject.getString("user");
                String mediaType = jsonObject.getString("type");
                String action = jsonObject.getString("action");
                updateVideoAudioState(participantName, mediaType, action);
                updateParticipants();
                updateView();
            } catch (JSONException e) {
                Log.e("App level", e.getMessage());
            }
        }

        @Override
        public void onHostToggleRaiseHandAudio(boolean enable) {
            if (enable) {
                final MaterialDialog dialog = new MaterialDialog(context);
                dialog.setMessage(getString(R.string.tip_request_audio))
                        .setNegativeButton(getString(R.string.disagree), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .setCanceledOnTouchOutside(false)
                        .setPositiveButton(getString(R.string.agree), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                H2HConference.getInstance().turnOnAudioForUser(H2HModel.getInstance().getRealDisplayName());
                                ibMic.setSelected(true);
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            } else {
                H2HConference.getInstance().turnOffAudioForUser(H2HModel.getInstance().getRealDisplayName());
                ibMic.setSelected(false);
            }
        }
    }

    private void connectSupport() {
        H2HSupportManager.getInstance().connect(context);
    }

    private void connectWhiteBoard() {
        H2HWhiteboardManager.getInstance().connect();
        H2HWhiteboardManager.getInstance().attachWhiteboardListener(new H2HWhiteboardListener() {
            @Override
            public void onGetPoll(final Object... args) {
                final Poll poll = FastJsonUtils.parseObject(args[0].toString(), Poll.class);
                if (poll == null) {
                    return;
                }
                poll.setEndTime(System.currentTimeMillis() + poll.getDuration());
                int index = polls.indexOf(poll);
                if (index >= 0) {
                    polls.set(index, poll);
                } else {
                    polls.add(poll);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (System.currentTimeMillis() >= poll.getEndTime()) {
                            return;
                        }
                        if (AppManager.getAppManager().currentActivity() != FlatMeetingActivity.this || polling) {
                            waitPolls.add(poll);
                            return;
                        }
                        showPoll(poll);
                    }
                });

            }

            @Override
            public void onUpdatePoll(final Object object) {
                Poll poll = (Poll) object;
                int index = polls.indexOf(poll);
                polls.remove(index);
                polls.add(index, poll);
                turnToWhiteboard(poll);
            }

            @Override
            public void onStartPresenceCheck(final Object... args) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        MeetingActions.showPresenceCheckDialog(context,args);
//                        MeetingActions.showPresenceCheckDialog(context, args);
                        try {
                            JSONObject jsonObject = new JSONObject(args[0].toString());
                            jsonObject = jsonObject.optJSONObject("poll");
//                            Poll poll = FastJsonUtils.parseObject(args[0].toString(), Poll.class);
                            Poll poll = new Poll();
                            poll.setStartTime(jsonObject.optLong("startTime"));
                            poll.setEndTime(jsonObject.optLong("endTime"));
                            poll.setEndTime(30 * 1000 + System.currentTimeMillis());
                            showPresenceCheck(poll);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSocketConnected() {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        AppAction.getMRPolls(context, H2HModel.getInstance().getMeetingId(), new HttpResponseHandler(context, PollsResult.class) {
//                            @Override
//                            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                                PollsResult pollsResult = (PollsResult) response;
//                                ArrayList<Poll> temp = new ArrayList<>();
//                                temp.addAll(pollsResult.getPollList());
//                                for (Poll poll : temp) {
//                                    if (poll.isStarted()) {
//                                        poll.setEndTime(System.currentTimeMillis());
//                                        polls.add(poll);
//                                    }
//                                }
//                                H2HWhiteboardManager.getInstance().checkPolls();
//                            }
//                        });
//                    }
//                });

            }
        });

    }

    private void showPoll(Poll poll) {
        if (startPollDialog == null) {
            startPollDialog = new AutoDismissMaterialDialog(this);
            startPollDialog.setMessage(getString(R.string.start_poll_tips, ""))
                    .setPositiveButton(getString(R.string.ok))
                    .setNegativeButton(R.string.cancel)
                    .setCanceledOnTouchOutside(false)
                    .setOnEventListener(new AutoDismissMaterialDialog.OnEventListener() {
                        @Override
                        public void onClick(boolean isPositiveButton, AutoDismissMaterialDialog dialog) {
                            dialog.dismiss();
                            polling = false;
                            if (isPositiveButton) {
                                startPoll(FlatMeetingActivity.this.poll);
                            } else {
                                checkWaitPolls();
                            }
                        }

                        @Override
                        public void onDismiss(AutoDismissMaterialDialog dialog) {
                            polling = false;
                        }

                        @Override
                        public void onUpdate(long time, AutoDismissMaterialDialog dialog) {
                            dialog.setMessage(getString(R.string.start_poll_tips, DateFormat.format("mm:ss", time)));
                        }
                    });
        }
        if (!polling) {
            this.poll = poll;
            startPollDialog.setCountTime(poll.getEndTime() - System.currentTimeMillis());
            startPollDialog.show();
            polling = true;
        }
    }

    private void startPoll(Poll poll) {
//        MeetingActions.startPoll(this, poll, tempIndex);
    }

    private void showPresenceCheck(Poll poll) {
        if (presenceCheckDialog == null) {
            presenceCheckDialog = new AutoDismissMaterialDialog(this);
            presenceCheckDialog.setMessage(getString(R.string.take_attendance, ""))
                    .setPositiveButton(R.string.take_attendance_btn)
                    .setNegativeButton(R.string.cancel)
                    .setCanceledOnTouchOutside(false)
                    .setOnEventListener(new AutoDismissMaterialDialog.OnEventListener() {
                        @Override
                        public void onClick(boolean isPositiveButton, AutoDismissMaterialDialog dialog) {
                            dialog.dismiss();
                            if (isPositiveButton) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("name", H2HModel.getInstance().getRealDisplayName());
                                    LogUtils.e(jsonObject.toString());
                                    H2HWhiteboardManager.getInstance().sendPresenceToHost(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onDismiss(AutoDismissMaterialDialog dialog) {
                            presenceChecking = false;
                        }

                        @Override
                        public void onUpdate(long time, AutoDismissMaterialDialog dialog) {
                            dialog.setMessage(getString(R.string.take_attendance, DateFormat.format("mm:ss", time)));
                        }
                    });
        }
        if (!presenceChecking) {
            presenceChecking = true;
            presenceCheckDialog.setCountTime(poll.getEndTime() - System.currentTimeMillis());
            presenceCheckDialog.show();
        }
    }

    /**
     * EasyPermission Handle
     */
    @AfterPermissionGranted(PERMISSION_BOTH)
    private void requestPermission() {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            launchMeeting();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_audio),
                    PERMISSION_BOTH, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        MeetingActions.checkMeetingPermission(this, perms, PERMISSION_BOTH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void showSupporMessage() {
        showChatroomDialog();
        chatRoomDialog.showSupportMessage();
        if (moreSettingDialog.isShowing()) {
            moreSettingDialog.dismiss();
        }
    }

    private void turnToWhiteboard(final Poll poll) {
        if (whiteBoardFragment == null) {
            isShowWhiteboardLocatingPoll = true;
            showWhiteBoardFragment();
        } else {
            showWhiteBoardFragment();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    whiteBoardFragment.locatePoll(poll);
                }
            });
        }
    }
//    private void showInviteSheetBottom() {
//        if (TextUtils.isEmpty(H2HModel.getInstance().getMeetingId())) {
//            return;
//        }
//        ActionSheetDialog sheetDialog = new ActionSheetDialog(this)
//                .builder()
//                .addSheetItem(getString(R.string.invite_contact_byEmail), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(int which, String srcItem) {
////                        ShareUtil.meetingRoomShareLinks(context);
//                    }
//                })
//                .setCancelable(true)
//                .setCanceledOnTouchOutside(true);
//        sheetDialog.show();
//    }

    private void selectIcon(ImageButton ib) {
        initIcons();
        ib.setSelected(true);
    }

    private void initIcons() {
//        ibMic.setSelected(false);
        ibChat.setSelected(false);
        ibWhiteboard.setSelected(false);
        ibConference.setSelected(false);
        ibParticipants.setSelected(false);
        ibMore.setSelected(false);
    }

    @Override
    public void callback() {
        if (currentFragment != null && TextUtils.equals(currentFragment.getClass().getSimpleName(), WhiteBoardFragment.class.getSimpleName())) {
            selectIcon(ibWhiteboard);
        } else {
            selectIcon(ibConference);
        }
    }

    private void checkWaitPolls() {
        for (Poll poll : waitPolls) {
            if (poll.getEndTime() > System.currentTimeMillis()) {
                showPoll(poll);
                waitPolls.remove(poll);
                break;
            }
        }
    }
}
