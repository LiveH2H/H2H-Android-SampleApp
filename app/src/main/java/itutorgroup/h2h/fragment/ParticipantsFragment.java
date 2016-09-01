package itutorgroup.h2h.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hmodel.H2HModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.adapter.ParticipantAdapter;
import itutorgroup.h2h.bean.Participant;
import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.bean.event.Event;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantsFragment extends BaseFragment {
    private H2HConference conference;
    private ServerConfig serverConfig;
    private ListView lv;
    private List<Participant> participants = new ArrayList<>();
    private ParticipantAdapter adapter;
    public static ParticipantsFragment newInstance(ServerConfig serverConfig) {
        ParticipantsFragment fragment = new ParticipantsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("serverConfig", serverConfig);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conference = H2HConference.getInstance();
        if (getArguments() != null) {
            serverConfig = (ServerConfig) getArguments().getSerializable("serverConfig");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants, container, false);
        lv = ViewUtil.findViewById(view, R.id.iv_participants);
        adapter = new ParticipantAdapter(mContext, participants, R.layout.item_listformat_participant);
        lv.setAdapter(adapter);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListener();
        loadDatas();
    }

    public void loadDatas() {
        if(conference.getClass()==null){
            return;
        }
        List<H2HPeer> peers = conference.getPeers();
        participants.clear();

        for (H2HPeer peer : peers) {
            Participant participant = new Participant();
            participant.displayName = peer.getId();
            participant.isVideo = peer.isVideoOn();
            participant.isAudio = peer.isAudioOn();
            participant.isLocalUser = peer.isLocalUser();
            participant.userRole = peer.userRole();
            if (participant.userRole == H2HPeer.UserRole.Translator){
                participant.displayName = peer.getTranslator().getLanguage();
            }
            participants.add(participant);
        }
        adapter.notifyDataSetChanged();
    }

    private void addListener() {
        adapter.setContactorOperateCallback(new ParticipantAdapter.ContactorOperateCallback() {
            @Override
            public void operateAudio(int position, Participant participant) {
                if (!H2HModel.getInstance().getUserRole().equals("H") && !participant.isLocalUser) {
                    Log.e("App Level", "You don't have permission to toggle this user");
                    return;
                }
                if (participant.isAudio) {
                    participant.turnOffAudio();
                } else {
                    participant.turnOnAudio();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void operateVideo(int position, Participant participant) {
                if (!H2HModel.getInstance().getUserRole().equals("H") && !participant.isLocalUser) {
                    Log.e("App Level", "You don't have permission to toggle this user");
                    return;
                }
                if (participant.isVideo) {
                    participant.turnOffVideo();
                } else {
                    participant.turnOnVideo();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void operateWhiteboard(int position, Participant participant) {
//                if (!H2HModel.getInstance().getUserRole().equals("H") && !participant.isLocalUser) {
//                    Log.e("App Level", "You don't have permission to toggle this user");
//                    return;
//                }
//                participant.isWhiteboard = !participant.isWhiteboard;
//                if (!participant.isWhiteboard) {
//                } else {
//                }
//                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected boolean openEventBus() {
        return true;
    }
    @Subscribe
    public void updateMeetingEvent(Event.UpdateParticipants updateParticipants){
        loadDatas();
    }

}
