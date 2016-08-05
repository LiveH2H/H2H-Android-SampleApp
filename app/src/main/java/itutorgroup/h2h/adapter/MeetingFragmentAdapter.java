package itutorgroup.h2h.adapter;

import android.support.v4.app.Fragment;

import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.fragment.ChatFragment;
import itutorgroup.h2h.fragment.ParticipantsFragment;
import itutorgroup.h2h.fragment.ConferenceFragment;
import itutorgroup.h2h.fragment.WhiteBoardFragment;
import itutorgroup.h2h.view.FragmentNavigatorAdapter;


/**
 * Created by aspsine on 16/3/31.
 */
public class MeetingFragmentAdapter implements FragmentNavigatorAdapter {
    public enum fragment {
        conference, participants, chat,whiteboard;
    }
    private ServerConfig serverConfig;
    public MeetingFragmentAdapter(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }
    @Override
    public Fragment onCreateFragment(int position) {
        if (position == fragment.conference.ordinal()) {
            return ConferenceFragment.newInstance(serverConfig);
        } else if (position == fragment.participants.ordinal()) {
            return ParticipantsFragment.newInstance(serverConfig);
        } else if (position == fragment.chat.ordinal()) {
           return ChatFragment.newInstance(serverConfig);
        } else if(position == fragment.whiteboard.ordinal()){
            return WhiteBoardFragment.newInstance();
        }
        return null;
    }

    @Override
    public String getTag(int position) {
        return fragment.values()[position].toString();
    }

    @Override
    public int getCount() {
        return fragment.values().length;
    }
}
