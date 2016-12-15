package com.meetingroom.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年09月08日 10:44
 * 邮箱：nianbin@mosainet.com
 */
public class MeetingState extends DataSupport implements Serializable{
    private String meetingId;
    private String userId;
    private boolean isAudioOn;
    private boolean isVideoOn;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAudioOn() {
        return isAudioOn;
    }

    public void setAudioOn(boolean audioOn) {
        isAudioOn = audioOn;
    }

    public boolean isVideoOn() {
        return isVideoOn;
    }

    public void setVideoOn(boolean videoOn) {
        isVideoOn = videoOn;
    }
}
