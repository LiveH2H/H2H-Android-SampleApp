package com.meetingroom.bean;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;

import java.io.Serializable;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 17:03
 * 邮箱：nianbin@mosainet.com
 */
public class Participant implements Serializable {
    public String displayName;
    public H2HPeer.UserRole userRole;
    public boolean isAudio;
    public boolean isVideo;
    public boolean isWhiteboard;
    public boolean isLocalUser;
    public boolean isRaisingHand;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return displayName.equals(that.displayName);

    }

    @Override
    public int hashCode() {
        return displayName.hashCode();
    }
}
