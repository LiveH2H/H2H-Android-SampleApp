package com.meetingroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.meetingroom.utils.LogUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月30日 15:12
 * 邮箱：nianbin@mosainet.com
 */
public class WhiteboardItemAdapter extends CommonAdapter<View> {
    public int position=0;
    public WhiteboardItemAdapter(Context context, int layoutId, List<View> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final View item, final int position) {
//        Bitmap bitmap = Tools.getViewDrawingCache(item);
//        if (bitmap != null) {
//            ImageView imageView = viewHolder.getView(R.id.iv_whiteboard);
//            imageView.setImageBitmap(bitmap);
//        }
        LogUtils.e("WhiteboardItemAdapter:"+position);
        viewHolder.setText(R.id.tv_whiteboard, item.getTag().toString());
        LinearLayout linearLayout = viewHolder.getView(R.id.ll_container);
        linearLayout.setSelected(this.position == position);
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
