package com.meetingroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetingroom.bean.poll.getResult.Option;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/27 0027 13:28
 * 邮箱：nianbin@mosainet.com
 */
public class PollAdapter extends CommonAdapter<Option> {
    public int index = -1;

    public PollAdapter(Context context, int layoutId, List<Option> datas) {
        super(context, layoutId, datas);
    }


    @Override
    protected void convert(ViewHolder viewHolder, Option item, final int position) {
        final ImageView ivAnwser = viewHolder.getView(R.id.iv_answer);
        final LinearLayout ll = viewHolder.getView(R.id.ll);
        TextView tvAnswer = viewHolder.getView(R.id.tv_answer);
        tvAnswer.setText(item.getText());
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
            }
        });
        ll.setBackgroundResource(index==position?R.color.pressed_bgcolor_green:R.color.white);
        ivAnwser.setImageResource(index == position ? R.drawable.ic_check_checked : R.drawable.ic_check_unchecked);
    }
}
