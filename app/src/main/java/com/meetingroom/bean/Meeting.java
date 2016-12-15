package com.meetingroom.bean;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 会议实体类
 * 
 * @author Rays 2016年4月14日
 */
public class Meeting implements Serializable {

    //private (shareType=1): Only meeting invitees can join the meeting
    //internal (shareType=2): Users from the same company of the host can join the meeting
    //secure (shareType=3): Users can join the meeting with correct meeting passcode (passcode should be 4-digits number)
    //open (shareType=4): Anyone can join the meeting with meetingId

    public String hostName;
    public String surveyStatus;
    public int surveySn;
    public int quizDuration;
    public String subject;
    public String meetingIcon;
    public boolean hostVideo;
    public String description;
    public String meetingSn;
    public String meetingId;
    public int relayServerId;
    public int meetingType;
    public int serverId;
    public int shareType;
    public int duration;
    public String valid;
    public int quizCompleted;
    public int iconType;
    public long startTime;
    public int viewCount;
    public List<Object> lang;
    public String quizStatus;
    public int quizSn;
    public int hostSn;
    public String timeZone;
    public int docCount;
    public String sessionSn;
    public String avatar;
    public String userName;
    public boolean recordMeeting;
    public int userSn;
    public int attendeesCount;
    public int accountSn;
    public int surveyDuration;
    public String[] location;
    public long endTime;
    public int surveyCompleted;
    public boolean isShared;
    public boolean isFavorite;
	
	private CharSequence startTimeFormat;
	private String meetingIdFormat;
	private String locationStr;

    public CharSequence getStartTimeFormat() {
		if (TextUtils.isEmpty(startTimeFormat) && startTime > 0) {
			startTimeFormat = DateFormat.format("MMM dd, yyyy hh:mm a zz", startTime).toString();
		}
		return startTimeFormat;
	}

//	public String getMeetingIdFormat() {
//		if (TextUtils.isEmpty(meetingIdFormat) && !TextUtils.isEmpty(meetingId)) {
//			meetingIdFormat = StringUtil.space(meetingId, "-", 3);
//		}
//		return meetingIdFormat;
//	}

    public String getLocationStr() {
        if (TextUtils.isEmpty(locationStr) && location != null) {
            locationStr = "";
            boolean isFirst = true;
            for (String s : location) {
                if (!isFirst) {
                    locationStr += ",";
                }
                locationStr += s;
                isFirst = false;
            }
        }
        return locationStr;
    }

}
