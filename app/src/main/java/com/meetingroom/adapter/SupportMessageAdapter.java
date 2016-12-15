package com.meetingroom.adapter;

import android.content.Context;

import com.itutorgroup.h2hSupport.SupportMessage;
import com.zhy.adapter.abslistview.MultiItemTypeAdapter;

import java.util.List;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月13日 10:08
 * 邮箱：nianbin@mosainet.com
 */
public class SupportMessageAdapter extends MultiItemTypeAdapter<SupportMessage> {

    public SupportMessageAdapter(Context context, List<SupportMessage> datas) {
        super(context, datas);
    }
}
