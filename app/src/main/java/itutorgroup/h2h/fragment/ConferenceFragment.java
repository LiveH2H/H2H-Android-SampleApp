package itutorgroup.h2h.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hconference.H2HRTCListener;
import com.itutorgroup.h2hmodel.H2HModel;
import com.mosai.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.VideoRendererGui;

import java.util.Iterator;
import java.util.Set;

import itutorgroup.h2h.R;
import itutorgroup.h2h.activity.MeetingActivity;
import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.bean.event.Event;
import itutorgroup.h2h.utils.StringUtil;
import itutorgroup.h2h.utils.ViewUtil;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConferenceFragment extends BaseFragment {
    private static final int dash = 5;
    private AbsoluteLayout altAvatars;
    private ImageButton ivHungup;
    private H2HConference conference;
    private GLSurfaceView videoView;
    private ServerConfig serverConfig;
    private MaterialDialog mdLogout;
    private ImageView ivVideo, ivAudio;
    private TextView tvMeetingId;
    private Boolean isUsingFrontCamera = true;

    public ConferenceFragment() {
    }

    public static ConferenceFragment newInstance(ServerConfig serverConfig) {
        ConferenceFragment fragment = new ConferenceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("serverConfig", serverConfig);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serverConfig = (ServerConfig) getArguments().getSerializable("serverConfig");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conference, container, false);
        ivHungup = ViewUtil.findViewById(view, R.id.ib_hangup);
        ivVideo = ViewUtil.findViewById(view, R.id.iv_video);
        ivAudio = ViewUtil.findViewById(view, R.id.iv_audio);
        tvMeetingId = ViewUtil.findViewById(view, R.id.tv_meetingId);
        videoView = ViewUtil.findViewById(view, R.id.gl_surface);
        altAvatars = ViewUtil.findViewById(view, R.id.alt_avatars);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvMeetingId.setText(StringUtil.formatMeetingId(H2HModel.getInstance().getMeetingId()));

        ivAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ivAudio.isSelected()) {
                    turnOnAudio();
                } else {
                    turnOffAudio();
                }
            }
        });
        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ivVideo.isSelected()) {
                    turnOnVideo();
                } else {
                    turnOffVideo();
                }
            }
        });
        ivHungup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdLogout.show();
            }
        });
        mdLogout = new MaterialDialog(mContext)
                .setMessage(mContext.getString(R.string.logout_tips))
                .setPositiveButton(mContext.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdLogout.dismiss();
                        H2HChat.getInstance().leaveRoom();
                        H2HConference.getInstance().closeAllConnections();
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
        conference = H2HConference.getInstance();
        conference.listener = new RTCListener();
        VideoRendererGui.setView(videoView, null);
        conference.initRTCConnection();
        ivAudio.setSelected(true);
        ivVideo.setSelected(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.onPause();
        }
        conference.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.onResume();
        }
        conference.onResume();
    }

    private class RTCListener extends H2HRTCListener {

        @Override
        public void onConnectivityStrengthChanged(int connectivityStrength) {
            Log.e("App level", "Connectivity Strength:" + connectivityStrength);
        }

        @Override
        public void onAddRemoteStream(final MediaStream remoteStream, final H2HPeer peer) {
            Log.e("App Level", "onAddRemoteStream" + peer.getId());
            EventBus.getDefault().post(new Event.UpdateParticipants());
            updateVideoViewLayoutByAdding(peer.getId(), false);
            updateAvatars(peer.getId());
        }


        @Override
        public void onPeerConnectionClosed(final H2HPeer peer) {
            Log.e(tag, "onPeerConnectionClosed, number of remotes: " + conference.getRemoteRenders().size());
            EventBus.getDefault().post(new Event.UpdateParticipants());
            updateVideoViewLayoutByClosing(peer.getId());
            updateAvatars(peer.getId());
        }

        @Override
        public void onRemoveRemoteStream(final H2HPeer peer) {
            Log.e(tag, "onRemoveRemoteStream");
        }

        @Override
        public void onToggleMedia(final JSONObject jsonObject) {
            ((MeetingActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(tag, "onToggleMedia");
                    try {
                        String participantName = jsonObject.getString("user");
                        String mediaType = jsonObject.getString("type");
                        String action = jsonObject.getString("action");
                        //Handle UI
                        //Update me Video/Audio State
                        updateVideoAudioState(participantName, mediaType, action);
                        //notice to update participants
                        EventBus.getDefault().post(new Event.UpdateParticipants());
                        if (TextUtils.equals("video", mediaType)) {
                            updateAvatars(participantName);
                        }
                    } catch (JSONException e) {
                        Log.e("App level", e.getMessage());
                    }

                }
            });
        }

        @Override
        public void onForceLogout(String message) {
            Log.e("App level", "on Force Logout");
            H2HChat.getInstance().leaveRoom();
            H2HConference.getInstance().closeAllConnections();

            AlertDialog alertDialog = new AlertDialog.Builder(
                    mContext).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Someone else used your credential to join the meeting. You are forced to logout.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ((MeetingActivity) mContext).setResult(Activity.RESULT_OK);
                    ((MeetingActivity) mContext).finish();
                }
            });
            alertDialog.show();
        }

        @Override
        public void onPeerRaiseHand(H2HPeer peer) {
            Toast.makeText(mContext,peer.getId()+" raises hand",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRaiseHandPermissionChanged(Boolean enable) {
            Toast.makeText(mContext,"raise hand enabled:" +enable, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChatPermissionChanged(Boolean enable) {
            Toast.makeText(mContext,"chat enabled:" +enable,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }

    @Subscribe
    public void updateMeetingEvent(Event.UpdateParticipants updateParticipants) {
    }

    public void turnOnVideo() {
        H2HConference.getInstance().turnOnVideoForUser(H2HModel.getInstance().getRealDisplayName());
    }

    public void turnOffVideo() {
        H2HConference.getInstance().turnOffVideoForUser(H2HModel.getInstance().getRealDisplayName());
    }

    public void turnOnAudio() {
        H2HConference.getInstance().turnOnAudioForUser(H2HModel.getInstance().getRealDisplayName());
    }

    public void turnOffAudio() {
        H2HConference.getInstance().turnOffAudioForUser(H2HModel.getInstance().getRealDisplayName());
    }

    private void updateVideoAudioState(String displayName, String type, String action) {
        if (TextUtils.equals(displayName, H2HModel.getInstance().getRealDisplayName())) {
            if (TextUtils.equals(type, "video")) {
                if (TextUtils.equals(action, "enable")) {
                    ivVideo.setSelected(true);
                } else {
                    ivVideo.setSelected(false);
                }
            }
            if (TextUtils.equals(type, "audio")) {
                if (TextUtils.equals(action, "enable")) {
                    ivAudio.setSelected(true);
                } else {
                    ivAudio.setSelected(false);
                }
            }
        }
    }

    private void updateAvatars(String peerId) {
        altAvatars.removeAllViews();
        if (conference.getRemoteRenders().size() == 0) {
            if (TextUtils.equals(H2HModel.getInstance().getRealDisplayName(), peerId)) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeers()) {
                    if (TextUtils.equals(peerId, h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            ImageView ivSmall = new ImageView(mContext);
                            ivSmall.setImageResource(R.drawable.ic_blank_avatar);
                            ivSmall.setScaleType(ImageView.ScaleType.FIT_XY);
                            AbsoluteLayout.LayoutParams ivSmallParams = new AbsoluteLayout.LayoutParams(transformWidth(25) + dash, transformHeight(25) + dash, transformWidth(37), transformHeight(75));
                            ivSmall.setLayoutParams(ivSmallParams);
                            altAvatars.addView(ivSmall);
                        }
                    }
                }
            }

        } else if (conference.getRemoteRenders().size() == 1) {
            for (H2HPeer h2HPeer1 : H2HConference.getInstance().getPeers()) {
                if (TextUtils.equals(H2HModel.getInstance().getRealDisplayName(), peerId)) {
                    if (!h2HPeer1.isVideoOn()) {
                        ImageView ivSmall = new ImageView(mContext);
                        ivSmall.setImageResource(R.drawable.ic_blank_avatar);
                        ivSmall.setScaleType(ImageView.ScaleType.FIT_XY);
                        AbsoluteLayout.LayoutParams ivSmallParams = new AbsoluteLayout.LayoutParams(transformWidth(25) + dash, transformHeight(25) + dash, transformWidth(37), transformHeight(75));
                        ivSmall.setLayoutParams(ivSmallParams);
                        altAvatars.addView(ivSmall);
                    }
                } else {
                    if (!h2HPeer1.isVideoOn()) {
                        ImageView ivLarge = new ImageView(mContext);
                        ivLarge.setImageResource(R.drawable.ic_blank_avatar);
                        ivLarge.setScaleType(ImageView.ScaleType.FIT_XY);
                        AbsoluteLayout.LayoutParams ivLargeParams = new AbsoluteLayout.LayoutParams(transformWidth(100) + dash, transformHeight(75) + dash, 0, 0);
                        ivLarge.setLayoutParams(ivLargeParams);
                        altAvatars.addView(ivLarge);
                    }
                }
            }
        } else {
            if (TextUtils.equals(H2HModel.getInstance().getRealDisplayName(), peerId)) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeers()) {
                    if (TextUtils.equals(peerId, h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            ImageView ivLarge = new ImageView(mContext);
                            ivLarge.setImageResource(R.drawable.ic_blank_avatar);
                            ivLarge.setScaleType(ImageView.ScaleType.FIT_XY);
                            AbsoluteLayout.LayoutParams ivLargeParams = new AbsoluteLayout.LayoutParams(transformWidth(100) + dash, transformHeight(75) + dash, 0, 0);
                            ivLarge.setLayoutParams(ivLargeParams);
                            altAvatars.addView(ivLarge);
                        }
                    }
                }
            }
            Set<String> keys = conference.getRemoteRenders().keySet();
            int i = 0;
            for (String key : keys) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeers()) {
                    if (TextUtils.equals(key, h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            ImageView ivSmall = new ImageView(mContext);
                            ivSmall.setImageResource(R.drawable.ic_blank_avatar);
                            ivSmall.setScaleType(ImageView.ScaleType.FIT_XY);
                            AbsoluteLayout.LayoutParams ivSmallParams = new AbsoluteLayout.LayoutParams(transformWidth(25) + dash, transformHeight(25) + dash, transformWidth(25 * i), transformHeight(75));
                            ivSmall.setLayoutParams(ivSmallParams);
                            altAvatars.addView(ivSmall);
                        }
                    }
                }
                i++;
            }
        }
    }

    private void updateVideoViewLayoutByClosing(String peerId) {
        if (!TextUtils.equals(conference.getLocalUserName(), peerId)) {
            VideoRendererGui.remove(conference.getRemoteRenders().get(peerId));
            conference.getRemoteRenders().remove(peerId);
            updateVideoViewLayoutByAdding(peerId, true);
            Iterator<H2HPeer> iterator = conference.getPeers().iterator();
            while (iterator.hasNext()) {
                H2HPeer h2HPeer = iterator.next();
                if (TextUtils.equals(peerId, h2HPeer.getId())) {
                    conference.getPeers().remove(h2HPeer);
                    return;
                }
            }
        }
    }

    private void updateVideoViewLayoutByAdding(String peerId, boolean isClosing) {

        if (conference.getRemoteRenders().size() == 0) {
            if (conference.getLocalRender() != null) {
                VideoRendererGui.update(conference.getLocalRender(), 37, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            }
        } else if (conference.getRemoteRenders().size() == 1) {
            try {
                VideoRendererGui.update(conference.getLocalRender(), 37, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                if (!isClosing) {
                    VideoRendererGui.update(conference.getRemoteRenders().get(peerId), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                } else {
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    for (String key : keys) {
                        VideoRendererGui.update(conference.getRemoteRenders().get(key), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    }
                }
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                VideoRendererGui.update(conference.getLocalRender(), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    VideoRendererGui.update(conference.getRemoteRenders().get(key), 25 * i, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    i++;
                }
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }

    private int transformHeight(int heightPercent) {
        int totalHeight = (int) (Tools.getAtyHeight(mContext) - mContext.getResources().getDimension(R.dimen.titleheight) - mContext.getResources().getDimension(R.dimen.bottomheight));
        return (int) (heightPercent * 0.01 * totalHeight);
    }

    private int transformWidth(int widthPercent) {
        return (int) (widthPercent * 0.01 * Tools.getAtyWidth(mContext));
    }
}
