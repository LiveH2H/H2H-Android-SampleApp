package itutorgroup.h2h.adapter;

import android.content.Context;

import com.zhy.adapter.abslistview.MultiItemTypeAdapter;

import java.util.List;

import itutorgroup.h2h.bean.ChatMessage;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月13日 10:08
 * 邮箱：nianbin@mosainet.com
 */
public class ChatMessageAdapter extends MultiItemTypeAdapter<ChatMessage>{

    public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
        super(context, datas);
    }
}
