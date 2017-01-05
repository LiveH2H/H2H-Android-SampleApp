package com.meetingroom.fragment;


import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HHLSListener;
import com.itutorgroup.h2hmodel.H2HFeatures;
import com.itutorgroup.h2hmodel.H2HModel;
import com.meetingroom.activity.MeetingActivity;
import com.meetingroom.adapter.delegate.ChatOverlayAdapter;
import com.meetingroom.bean.ChatMessage;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.callback.WebinarHostCallback;
import com.meetingroom.meet.MeetingActions;

import org.webrtc.VideoRendererGui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConferenceFragment extends BaseFragment {
    private static final long TOUCH_TIME_DURATION = 3000;
    public GLSurfaceView glSurfaceView;
    protected boolean hideLayout;
    private ListView lvChat;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private RadioGroup rpRank;
    @SuppressWarnings("deprecation")
    private AbsoluteLayout altAvatars, altNames;
    private MaterialDialog mdLogout;
    private TextView tvWaitHost;
    private long lastTouchTime;
    private Timer timerTouch;
    private ConferenceFragmentCallback conferenceFragmentCallback;
    private TextView tvWebinarState;
    private FrameLayout ftMeeting,ftBroadcast;
    private JCVideoPlayerStandard videoview;
    private SimpleDraweeView ivDefaultAvatar;

    public ConferenceFragment() {
    }

    public static ConferenceFragment newInstance() {
        return new ConferenceFragment();
    }

    public static ConferenceFragment newInstance(ServerConfig serverConfig) {
        ConferenceFragment fragment = new ConferenceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("serverConfig", serverConfig);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConferenceFragmentCallback) {
            conferenceFragmentCallback = (ConferenceFragmentCallback) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conference, container, false);
        glSurfaceView = ViewUtil.findViewById(view, R.id.gl_surface);
        altAvatars = ViewUtil.findViewById(view, R.id.alt_avatars);
        altNames = ViewUtil.findViewById(view, R.id.alt_names);
        rpRank = ViewUtil.findViewById(view, R.id.rg_rank);
        tvWaitHost = ViewUtil.findViewById(view, R.id.tvWaitHost);
        lvChat = ViewUtil.findViewById(view, R.id.lv_chat_overlay);
        tvWebinarState = ViewUtil.findViewById(view, R.id.tv_webinar_host_state);
        ftMeeting = ViewUtil.findViewById(view,R.id.ft_meeting);
        ftBroadcast = ViewUtil.findViewById(view,R.id.ft_broadcast);
        videoview = ViewUtil.findViewById(view,R.id.videoview);
        ivDefaultAvatar = ViewUtil.findViewById(view,R.id.ivDefaultAvatar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mdLogout = new MaterialDialog(mContext)
                .setMessage(mContext.getString(R.string.logout_tips))
                .setPositiveButton(mContext.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                        H2HConference.getInstance().closeAllConnections();
                        H2HChat.getInstance().leaveRoom();
                        ((MeetingActivity) mContext).setResult(Activity.RESULT_OK);
                        ((MeetingActivity) mContext).finish();
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                    }
                });
        addListener();
        handleConferenceType(view);
    }

    /**
     * 控制meeting type是否broadcast
     */
    private void handleConferenceType(View view){
        //type为Broadcast的会议类型处理方式
        if(H2HModel.getInstance().getMeetingType()== H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST){
            ftBroadcast.setVisibility(View.VISIBLE);
            ftMeeting.setVisibility(View.GONE);
            if (H2HConference.getInstance().isBlockedUser()) {
                blockedUser();
                return;
            }
            if (conferenceFragmentCallback != null) {
                H2HConference.getInstance().initRTCConnection();
                videoview.openLiveMode();
                videoview.setUp(H2HModel.getInstance().getBroadcastUrl(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                showAndPlayHLSVideo(H2HConference.getInstance().isExistHost());
                H2HConference.getInstance().attachHLSListener(new H2HHLSListener(){
                    @Override
                    public void onHostJoin() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(tag, "onHostJoin, reload url");
                                if (isFinishing()) {
                                    return;
                                }
                                showAndPlayHLSVideo(true);
                            }
                        }, 2000);
                    }
                    @Override
                    public void onHostLeave() {
                        showHLSVideo(false);
//                        ((MeetingActivity) getActivity()).destroyResources();
//                        MeetingActions.showHostLeaveDialog(getActivity());
                    }

                    @Override
                    public void onLeftRoom() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((MeetingActivity) getActivity()).destroyResources();
                                MeetingActions.showLeftRoomDialog(getActivity());
                            }
                        });
                    }

                    @Override
                    public void onBlockedUser() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                blockedUser();
                            }
                        });
                    }

                    @Override
                    public void onToggleMedia(final boolean isEnable) {
                        if (isFinishing()) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showHLSVideo(isEnable);
                                ViewUtil.setVisibility(tvWaitHost, View.GONE);
                            }
                        });
                    }
                });
                isCreated = true;
                conferenceFragmentCallback.conferenceFragmentviewCreated();
            }
        }else{
            ftBroadcast.setVisibility(View.GONE);
            ftMeeting.setVisibility(View.VISIBLE);
            VideoRendererGui.setView(glSurfaceView, null);
            checkTouchEvent(view);
            rpRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    rank = i == R.id.rb_pointrank ? RANK_POINT : RANK_ROW;
                    touch();
                }
            });
            if (conferenceFragmentCallback != null) {
                H2HConference.getInstance().initRTCConnection();
                isCreated = true;
                conferenceFragmentCallback.conferenceFragmentviewCreated();
            }
        }
    }

    private void blockedUser() {
        ((MeetingActivity) getActivity()).destroyResources();
        MeetingActions.showBlockedUserDialog(getActivity());
    }

    private void showAndPlayHLSVideo(boolean isPlay) {
        Log.i(tag, "showAndPlayHLSVideo() isPlay=" + isPlay);
        showHLSVideo(isPlay);
        if (isPlay && H2HFeatures.isVideoEnabled()) {
            videoview.startPlayLogic();
        }
    }

    private void showHLSVideo(boolean isShow) {
        if (isShow) {
            ViewUtil.setVisibility(videoview, View.VISIBLE);
            ViewUtil.setVisibility(ivDefaultAvatar, View.GONE);
            ViewUtil.setVisibility(tvWaitHost, View.GONE);
        } else {
            ViewUtil.setVisibility(videoview, View.GONE);
            ViewUtil.setVisibility(tvWaitHost, View.VISIBLE);
            ViewUtil.setVisibility(ivDefaultAvatar, View.VISIBLE);
            ivDefaultAvatar.setImageURI(H2HConference.getInstance().getHostAvatar());
        }
    }


    private void addListener() {
        ChatOverlayAdapter adapter = new ChatOverlayAdapter(mContext, R.layout.item_listformat_chat_overlay, chatMessages);
        lvChat.setAdapter(adapter);
    }


    public void updateView() {
        if(H2HModel.getInstance().getMeetingType()!= H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST){
            Log.e(tag, "updateView");
            MeetingActions.updateView(mContext, rank, false, hideLayout, rpRank, glSurfaceView, altAvatars, altNames, new WebinarHostCallback() {
                @Override
                public void callback(boolean exist) {
                    tvWebinarState.setVisibility(exist ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerTouch != null) {timerTouch.cancel();
        }
        JCVideoPlayer.releaseAllVideos();
    }

    private void checkTouchEvent(View view){
        lastTouchTime = System.currentTimeMillis();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touch();
            }
        });
        timerTouch = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long eTime = System.currentTimeMillis();
                long dTime = eTime-lastTouchTime;
                if(dTime>=TOUCH_TIME_DURATION){
                    if(!hideLayout){
                        hideLayout = true;
                        Log.e(tag,"hide");
                        new Handler(mContext.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                updateView();
                            }
                        });
                    }
                    lastTouchTime = eTime;
                }
            }
        };
        timerTouch.schedule(task,new Date(System.currentTimeMillis()),TOUCH_TIME_DURATION);
    }

    private void touch(){
        lastTouchTime = System.currentTimeMillis();
        Log.e(tag,"touch");
        hideLayout = false;
        updateView();
    }

    public interface ConferenceFragmentCallback {
        void conferenceFragmentviewCreated();
    }
}
