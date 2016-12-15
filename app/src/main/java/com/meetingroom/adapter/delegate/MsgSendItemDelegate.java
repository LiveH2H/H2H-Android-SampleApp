package com.meetingroom.adapter.delegate;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.meetingroom.bean.ChatMessage;
import com.mosai.utils.DensityUtil;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

import itutorgroup.h2h.R;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月13日 11:14
 * 邮箱：nianbin@mosainet.com
 */
public class MsgSendItemDelegate implements ItemViewDelegate<ChatMessage> {
    public MsgSendItemDelegate(){

    }
    private Context mContext;
    public MsgSendItemDelegate(Context context){
        this.mContext = context;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_listformat_chatmsg_send;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item.msgSend && !item.isEmoji;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvContent = holder.getView(R.id.tv_content);
        if(mContext==null){
            tvContent.setText(chatMessage.content);
            tvName.setText(chatMessage.name);
            tvName.setVisibility(View.VISIBLE);

        }else{
            tvName.setText(chatMessage.name);
            tvName.setVisibility(View.GONE);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString name = new SpannableString(chatMessage.name);
            name.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(mContext,12)),0,chatMessage.name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(name)
                    .append("\n\n")
                    .append(Html.fromHtml(chatMessage.content));
            tvContent.setText(builder);
        }
    }
}
