package itutorgroup.h2h.bean;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;

import java.io.Serializable;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 17:03
 * 邮箱：nianbin@mosainet.com
 */
public class Participant implements Serializable{
    public String displayName;
    public H2HPeer.UserRole userRole;
    public boolean isAudio;
    public boolean isVideo;
    public boolean isWhiteboard;
    public boolean isLocalUser;
    public void turnOnVideo() {
        H2HConference.getInstance().turnOnVideoForUser(displayName);
    }

    public void turnOffVideo() {
        H2HConference.getInstance().turnOffVideoForUser(displayName);
    }

    public void turnOnAudio() {
        H2HConference.getInstance().turnOnAudioForUser(displayName);
    }

    public void turnOffAudio() {
        H2HConference.getInstance().turnOffAudioForUser(displayName);
    }
}
