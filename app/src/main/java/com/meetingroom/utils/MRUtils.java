package com.meetingroom.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hchat.H2HChatMessage;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hmodel.H2HModel;
import com.meetingroom.bean.ChatMessage;
import com.meetingroom.bean.MeetingState;
import com.meetingroom.bean.SettingConfig;

import org.litepal.crud.DataSupport;

import java.util.Locale;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月25日 17:40
 * 邮箱：nianbin@mosainet.com
 */
public class MRUtils {
    //
    public static boolean isHost(){
        return H2HModel.getInstance().getUserRole()== H2HPeer.UserRole.Host;
    }
    public static boolean isHost(String displayName){
        for(H2HPeer h2HPeer : H2HConference.getInstance().getPeers()){
            if(TextUtils.equals(displayName,h2HPeer.getId())){
                if(h2HPeer.userRole()== H2HPeer.UserRole.Host){
                    return true;
                }
            }
        }
        return false;
    }
    //clear unused datas
    public static void clearDatas(final Context context) {
//        int duration = 2;//day
//        long lastTime = UserPF.getInstance().getLong(UserPF.MEETING_TIME, 0);
//        long nowTime = System.currentTimeMillis();
//        if (lastTime != 0) {
//            long dTime = nowTime - lastTime;
//            if (dTime / 1000 / 60 / 60 / 24 > duration) {
//                int serverConfis = DataSupport.deleteAll(SettingConfig.class);
//                int meetingState = DataSupport.deleteAll(MeetingState.class);
//                LogUtils.writeLog(context, "clearDatas", String.format(Locale.getDefault(), "nowTime:%d,lastTime:%d\nclear->serverConfig:%d,meetingState:%d", nowTime, lastTime, serverConfis, meetingState));
//                Log.e("test", String.format("clear->serverConfig:%d,meetingState:%d", serverConfis, meetingState));
//                UserPF.getInstance().putLong(UserPF.MEETING_TIME, nowTime);
//            }
//            //just for test
//            /*if(dTime/1000/60>1){
//                int serverConfis = DataSupport.deleteAll(SettingConfig.class);
//                int meetingState = DataSupport.deleteAll(MeetingState.class);
//                LogUtils.writeLog(context,"clearDatas",String.format("nowTime:%d,lastTime:%d\nclear->serverConfig:%d,meetingState:%d",nowTime,lastTime,serverConfis,meetingState));
//                Log.e("test",String.format("clear->serverConfig:%d,meetingState:%d",serverConfis,meetingState));
//                UserPF.getInstance().putLong(UserPF.MEETING_TIME,nowTime);
//            }*/
//        } else {
//            LogUtils.writeLog(context, "clearDatas", String.format(Locale.getDefault(), "nowTime:%d,lastTime:%d", nowTime, lastTime));
//            UserPF.getInstance().putLong(UserPF.MEETING_TIME, nowTime);
//        }
    }

    public static int getImgIdbyName(Context context, String message) {
        return context.getResources().getIdentifier(String.format("emoji_%s", getImgNamebyMessage(message)), "drawable", context.getPackageName());
    }

    private static boolean isMsgImg(String message) {
        return message.startsWith("<img alt=") || message.startsWith("<img title=");
    }

    private static String getImgNamebyMessage(String message) {
        String[] result = message.split("/");
        return result[result.length - 2].trim().split("\\.")[0];
    }

    public static ChatMessage parseConnectState(Context context, boolean isConnected) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.content = context.getString(isConnected ? R.string.chat_me_join_tip : R.string.chat_me_left_tip);
        return chatMessage;
    }

    public static ChatMessage parseMessage(H2HChatMessage chatMessage) {
        H2HChat h2HChat = H2HChat.getInstance();
        ChatMessage message = new ChatMessage();
        String messageBody = chatMessage.getMessageBody();
        messageBody = messageBody.replaceAll("(\\\\u)+(\\w{2,6})", "");
        String from = chatMessage.getFrom();
        String fromId = from.substring(from.indexOf('/') + 1);
        if (TextUtils.equals(fromId, h2HChat.getUserId())) {
            message.msgSend = true;
        } else {
            message.msgComing = true;
        }
        if (fromId.startsWith(h2HChat.getNameStarts())) {
            try {
                message.name = fromId.substring(h2HChat.getNameStarts().length());
            } catch (Exception e) {
                e.printStackTrace();
                message.name = fromId;
            }
        } else {
            message.name = fromId;
        }
        message.isEmoji = MRUtils.isMsgImg(messageBody);
        message.content = messageBody;
        return message;
    }

    /*public static ChatMessage parseUserState(H2HChatUser chatUser) {
        String displayName = chatUser.getDisplayName();
        Boolean isOnline = chatUser.isOnline();
        ChatMessage message = new ChatMessage();
        String content = (TextUtils.equals(displayName, H2HChat.getInstance().displayName) ? "You" : displayName) + (isOnline ? " joined" : " left");
        message.content = content;
        return message;
    }*/
    public static long getCurrentTimeMillis(){
       return System.currentTimeMillis();
    }
}
