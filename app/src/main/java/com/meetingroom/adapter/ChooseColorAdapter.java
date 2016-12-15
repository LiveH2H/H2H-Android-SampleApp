package com.meetingroom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageButton;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月11日 17:15
 * 邮箱：nianbin@mosainet.com
 */
public class ChooseColorAdapter extends CommonAdapter<String> {
    public int position;
    public ChooseColorAdapter(Context context, List<String> listDatas, int layoutId,int position) {
        super(context,layoutId,listDatas);
        this.position = position;
    }


    @Override
    protected void convert(ViewHolder viewHolder, String item, final int position) {
        ImageButton ib = viewHolder.getView(R.id.ib_color);
        ib.setSelected(this.position==position);
        makeShapes(ib,item);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseColorAdapter.this.position=position;
                notifyDataSetChanged();
            }
        });
    }
    private void makeShapes(ImageButton ib,String color) {
        GradientDrawable myGrad = (GradientDrawable)ib.getBackground();
        myGrad.setColor(Color.parseColor(color));
    }
}
