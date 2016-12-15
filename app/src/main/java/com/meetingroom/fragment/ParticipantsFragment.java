package com.meetingroom.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hmodel.H2HModel;
import com.meetingroom.adapter.ParticipantAdapter;
import com.meetingroom.bean.Participant;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantsFragment extends BaseFragment {
    private LinearLayout llInvite;
    private ListView lv;
    private List<Participant> participants = new ArrayList<>();
    private ParticipantAdapter adapter;
    private ImageView ivBack;
    private Set<String> raisePerson = new HashSet<>();
    private ParticipantsFragmentCallback participantsFragmentCallback;
    public static ParticipantsFragment newInstance(ServerConfig serverConfig) {
        ParticipantsFragment fragment = new ParticipantsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("serverConfig", serverConfig);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ParticipantsFragmentCallback) {
            participantsFragmentCallback = (ParticipantsFragmentCallback) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_participants, container, false);
        llInvite = ViewUtil.findViewById(view, R.id.ll_invite);
        lv = ViewUtil.findViewById(view, R.id.iv_participants);
        adapter = new ParticipantAdapter(mContext, participants);
        lv.setAdapter(adapter);
        ivBack = ViewUtil.findViewById(view, R.id.iv_back);
        if (participantsFragmentCallback != null) {
            isCreated = true;
            participantsFragmentCallback.participantsviewCreated();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListener();
    }

    public void loadDatas() {
        List<H2HPeer> peers = H2HConference.getInstance().getPeers();
        participants.clear();
        for (H2HPeer peer : peers) {
            Participant participant = new Participant();
            participant.displayName = peer.getId();
            participant.isVideo = peer.isVideoOn();
            participant.isAudio = peer.isAudioOn();
            participant.isLocalUser = peer.isLocalUser();
            participant.userRole = peer.userRole();
            if (participant.userRole == H2HPeer.UserRole.Translator) {
                participant.displayName = peer.getTranslator().getLanguage();
            }
            //判断是否有人举手
            if (MRUtils.isHost()) {
                Iterator<String> iterator = raisePerson.iterator();
                while (iterator.hasNext()) {
                    String id = iterator.next();
                    if (TextUtils.equals(participant.displayName, id)) {
                        participant.isRaisingHand = true;
                        break;
                    }
                }
            }

            participants.add(participant);
        }
        adapter.notifyDataSetChanged();
    }

    public void raiseHand(H2HPeer h2HPeer) {
        raisePerson.add(h2HPeer.getId());
        loadDatas();
    }

    private void addListener() {
        adapter.setContactorOperateCallback(new ParticipantAdapter.ContactorOperateCallback() {
            @Override
            public void operateAudio(int position, Participant participant) {
                if (H2HModel.getInstance().getUserRole() != H2HPeer.UserRole.Host && !participant.isLocalUser) {
                    Log.e("App Level", "You don't have permission to toggle this user");
                    ToastUtils.showToast(mContext, getString(R.string.forbit_permission));
                    return;
                }
                if (participant.isAudio) {
                    participant.turnOffAudio();
                } else {
                    participant.turnOnAudio();
                }
            }

            @Override
            public void operateVideo(int position, Participant participant) {
                if (H2HModel.getInstance().getUserRole() != H2HPeer.UserRole.Host && !participant.isLocalUser) {
                    Log.e("App Level", "You don't have permission to toggle this user");
                    ToastUtils.showToast(mContext, getString(R.string.forbit_permission));
                    return;
                }
                if (participant.isVideo) {
                    participant.turnOffVideo();
                } else {
                    participant.turnOnVideo();
                }
            }

            @Override
            public void operateWhiteboard(int position, Participant participant) {
            }

            @Override
            public void operateRaiseHand(int position, Participant participant) {
                if (raisePerson.contains(participant.displayName)) {
                    raisePerson.remove(participant.displayName);
                }
                if (participantsFragmentCallback != null) {
                    participantsFragmentCallback.isRaiseHandCallback(raisePerson.size() != 0);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (participantsFragmentCallback != null) {
                    participantsFragmentCallback.onBack();
                }
            }
        });
        llInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(mContext, InviteContactsActivity.class), 0);
            }
        });
        view.findViewById(R.id.tv_copy_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareUtil.meetingRoomShareLinks(mContext);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        MeetingActions.inviteParticipants(mContext, resultCode, data);
    }

    public interface ParticipantsFragmentCallback {
        void isRaiseHandCallback(boolean hasRaiseHand);
        void participantsviewCreated();
        void onBack();
    }
}
