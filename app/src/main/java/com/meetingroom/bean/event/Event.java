package com.meetingroom.bean.event;

import com.meetingroom.bean.ChatMessage;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月28日 17:11
 * 邮箱：nianbin@mosainet.com
 */
public class Event {
    public static class UpdateParticipants {

    }
    public static class ConferenceReconnect {

    }
    public static class UpdateChatRoom{
        public ChatMessage chatMessage;

    }
}
