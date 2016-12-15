package com.meetingroom.adapter.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetingroom.callback.GroupTypeSelectCallback;
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
public class GroupTypeAdapter extends CommonAdapter<String> {
    private GroupTypeSelectCallback groupTypeSelectCallback;
    public Integer index = 0;
    public GroupTypeAdapter(Context context, int layoutId, List<String> datas, GroupTypeSelectCallback groupTypeSelectCallback) {
        super(context, layoutId, datas);
        this.groupTypeSelectCallback = groupTypeSelectCallback;
    }


    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        final Integer index = position;
        final ImageView ivAnwser = viewHolder.getView(R.id.iv_group_type);
        final LinearLayout ll = viewHolder.getView(R.id.ll);
        TextView tvAnswer = viewHolder.getView(R.id.tv_group_type);
        tvAnswer.setText(item);
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    GroupTypeAdapter.this.index = index;
                if(groupTypeSelectCallback !=null){
                    groupTypeSelectCallback.callback(index);
                }
                notifyDataSetChanged();
            }
        });
//            ll.setBackgroundResource(GroupTypeAdapter.this.index==index?R.color.pressed_bgcolor_green:R.color.white);
            ivAnwser.setImageResource(GroupTypeAdapter.this.index==index?R.drawable.ic_check_checked : R.drawable.ic_check_unchecked);

    }

}
