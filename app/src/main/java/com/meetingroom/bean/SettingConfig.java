package com.meetingroom.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月18日 13:49
 * 邮箱：nianbin@mosainet.com
 */
public class SettingConfig extends DataSupport implements Serializable{
    private boolean isHost;
    private String meetingId;
    private String userId;
    private boolean allowChat=true;
    private boolean allowOverlay=true;
    private boolean isTimer=false;
    private int timerMins=-1;
    private String themeColor = "#1F2020";
    private boolean allowRaiseHand=true;

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

    public boolean isAllowChat() {
        return allowChat;
    }

    public void setAllowChat(boolean allowChat) {
        this.allowChat = allowChat;
    }

    public boolean isAllowOverlay() {
        return allowOverlay;
    }

    public void setAllowOverlay(boolean allowOverlay) {
        this.allowOverlay = allowOverlay;
    }

    public boolean isTimer() {
        return isTimer;
    }

    public void setTimer(boolean timer) {
        isTimer = timer;
    }

    public int getTimerMins() {
        return timerMins;
    }

    public void setTimerMins(int timerMins) {
        this.timerMins = timerMins;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public boolean isAllowRaiseHand() {
        return allowRaiseHand;
    }

    public void setAllowRaiseHand(boolean allowRaiseHand) {
        this.allowRaiseHand = allowRaiseHand;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingConfig that = (SettingConfig) o;

        if (meetingId != null ? !meetingId.equals(that.meetingId) : that.meetingId != null)
            return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = meetingId != null ? meetingId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
