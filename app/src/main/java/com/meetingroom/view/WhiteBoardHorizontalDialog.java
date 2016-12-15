package com.meetingroom.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.meetingroom.adapter.WhiteboardItemAdapter;
import com.meetingroom.utils.SystemUtil;
import com.meetingroom.utils.Tools;
import com.mosai.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月30日 14:58
 * 邮箱：nianbin@mosainet.com
 */
public class WhiteBoardHorizontalDialog extends RelativeDialog {
//    private BubbleRelativeLayout bbrl;
    private WhiteboardItemAdapter adapter;
    private HorizontalListView hlv;
    private Context context;
    private List<View> views=new ArrayList<>();
    public WhiteBoardHorizontalDialog(Context context, List<View> views, onItemClickListener onItemClickListener){
        super(context);
        this.context = context;
        if(views!=null){
            this.views.clear();
            this.views.addAll(views);
        }
        this.onItemClickListener = onItemClickListener;
        init();
        initConfig();
    }
    private void init(){
        view = View.inflate(mContext, R.layout.layout_whiteboard_horizontal,null);
        hlv = ViewUtil.findViewById(view, R.id.hlv);
//        bbrl = ViewUtil.findViewById(view,R.id.bubble);
        if(SystemUtil.isTablet(mContext)){
            height = (DensityUtil.dip2px(mContext,200));
            width = (Tools.getAtyWidth(mContext)/4*3);
//            bbrl.setArrowOffset(width-DensityUtil.dip2px(context,20));
        }else{
            height = (DensityUtil.dip2px(mContext,120));
            width = (Tools.getAtyWidth(mContext));
//            bbrl.setArrowOffset(width-DensityUtil.dip2px(context,20));
        }

        adapter = new WhiteboardItemAdapter(context,R.layout.item_listformat_whiteboard_horizontal,views);
        hlv.setAdapter(adapter);
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPosition(position);
                if(onItemClickListener!=null)
                    onItemClickListener.onItemclick(position);
            }
        });
    }
    private onItemClickListener onItemClickListener;
    public interface onItemClickListener{
        void onItemclick(int position);
    }

    public void refresh(List<View> views, int position){
        if(views!=null){
            this.views.clear();
            this.views.addAll(views);
            adapter.setPosition(position);
            adapter.notifyDataSetChanged();
        }

    }
}
