package com.meetingroom.adapter.delegate;

import android.content.Context;

import com.meetingroom.bean.ChatMessage;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月18日 14:21
 * 邮箱：nianbin@mosainet.com
 */
public class ChatOverlayAdapter extends CommonAdapter<ChatMessage> {

    public ChatOverlayAdapter(Context context, int layoutId, List<ChatMessage> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ChatMessage item, int position) {
        viewHolder.setText(R.id.tv_chat,String.format("%s:%s",item.name,item.content));
    }
}
