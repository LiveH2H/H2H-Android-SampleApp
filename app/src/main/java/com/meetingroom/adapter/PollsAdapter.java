package com.meetingroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetingroom.bean.poll.summary.Options;
import com.meetingroom.callback.PollOptionSelectCallback;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/27 0027 13:28
 * 邮箱：nianbin@mosainet.com
 */
public class PollsAdapter extends CommonAdapter<Options> {
    private PollOptionSelectCallback pollOptionSelectCallback;
    public List<Integer> indexs = new ArrayList<>();
    public boolean singleSelect=true;
    public Integer index = -1;
    public PollsAdapter(Context context, int layoutId, List<Options> datas, boolean singleSelect, PollOptionSelectCallback pollOptionSelectCallback) {
        super(context, layoutId, datas);
        this.singleSelect = singleSelect;
        this.pollOptionSelectCallback = pollOptionSelectCallback;
    }


    @Override
    protected void convert(ViewHolder viewHolder, Options item, int position) {
        final Integer index = position;
        final ImageView ivAnwser = viewHolder.getView(R.id.iv_answer);
        final LinearLayout ll = viewHolder.getView(R.id.ll);
        TextView tvAnswer = viewHolder.getView(R.id.tv_answer);
        tvAnswer.setText(item.getText());
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singleSelect){
                    PollsAdapter.this.index = index;
                }else{
                    if(indexs.contains(index)){
                        indexs.remove(index);
                    }else{
                        indexs.add(index);
                    }
                }
                if(pollOptionSelectCallback!=null){
                    pollOptionSelectCallback.callback();
                }
                notifyDataSetChanged();
            }
        });
        if(singleSelect){
            ll.setBackgroundResource(PollsAdapter.this.index==index?R.color.pressed_bgcolor_green:R.color.white);
            ivAnwser.setImageResource(PollsAdapter.this.index==index?R.drawable.ic_check_checked : R.drawable.ic_check_unchecked);
        }else{
            ll.setBackgroundResource(indexs.contains(index)?R.color.pressed_bgcolor_green:R.color.white);
            ivAnwser.setImageResource(indexs.contains(index) ? R.drawable.ic_check_checked : R.drawable.ic_check_unchecked);
        }
    }

}
