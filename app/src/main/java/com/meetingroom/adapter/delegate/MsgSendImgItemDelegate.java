package com.meetingroom.adapter.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetingroom.bean.ChatMessage;
import com.meetingroom.utils.MRUtils;
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
public class MsgSendImgItemDelegate implements ItemViewDelegate<ChatMessage> {
    private Context mContext;
    public MsgSendImgItemDelegate(){}
    public MsgSendImgItemDelegate(Context context){mContext=context;}
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_listformat_chatmsg_send_img;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position) {
        return item.msgSend && item.isEmoji;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        ImageView ivContent = holder.getView(R.id.iv_content);
        TextView tvContent = holder.getView(R.id.tv_content);
        if(mContext==null){
            tvName.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);
            ivContent.setVisibility(View.VISIBLE);
            tvName.setText(chatMessage.name);
            ivContent.setImageResource(MRUtils.getImgIdbyName(holder.getConvertView().getContext(), chatMessage.content));

        }else{
            tvName.setVisibility(View.GONE);
            tvContent.setVisibility(View.VISIBLE);
            ivContent.setVisibility(View.GONE);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString name = new SpannableString(chatMessage.name);
            name.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(mContext,12)),0,chatMessage.name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            SpannableString img = new SpannableString("icon");
            Drawable drawable = mContext.getResources().getDrawable(MRUtils.getImgIdbyName(holder.getConvertView().getContext(), chatMessage.content));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            img.setSpan(new ImageSpan(drawable),0,new String("icon").length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(name)
                    .append("\n\n")
                    .append(img);
            tvContent.setText(builder);
        }
    }
}
