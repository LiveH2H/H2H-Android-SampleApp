package com.meetingroom.adapter;

import android.content.Context;

import com.meetingroom.bean.ChatMessage;
import com.zhy.adapter.abslistview.MultiItemTypeAdapter;

import java.util.List;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月13日 10:08
 * 邮箱：nianbin@mosainet.com
 */
public class ChatMessageAdapter extends MultiItemTypeAdapter<ChatMessage> {

    public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
        super(context, datas);
    }
    public void setDatas(List<ChatMessage> chatMessages){
        this.mDatas.clear();
        this.mDatas.addAll(chatMessages);
        notifyDataSetChanged();
    }
}
