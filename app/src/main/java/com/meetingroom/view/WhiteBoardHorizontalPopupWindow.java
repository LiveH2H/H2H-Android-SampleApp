package com.meetingroom.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

import com.itutorgroup.util.SystemUtil;
import com.meetingroom.adapter.WhiteboardItemAdapter;
import com.meetingroom.utils.Tools;
import com.mosai.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月30日 14:58
 * 邮箱：nianbin@mosainet.com
 */
public class WhiteBoardHorizontalPopupWindow extends RelativePopupWindow {
    private WhiteboardItemAdapter adapter;
    private HorizontalListView hlv;
    private Context context;
    private List<View> views=new ArrayList<>();
    public WhiteBoardHorizontalPopupWindow(Context context, List<View> views, onItemClickListener onItemClickListener){
        super(context);
        this.context = context;
        if(views!=null){
            this.views.clear();
            this.views.addAll(views);
        }
        this.onItemClickListener = onItemClickListener;
        init();
    }
    private void init(){
        View view = View.inflate(mContext, R.layout.layout_whiteboard_horizontal,null);
        hlv = ViewUtil.findViewById(view,R.id.hlv);
        if(SystemUtil.isTablet(mContext)){
            setHeight(DensityUtil.dip2px(mContext,200));
            setWidth(Tools.getAtyWidth(mContext)/4*3);
        }else{
            setHeight(DensityUtil.dip2px(mContext,110));
            setWidth(Tools.getAtyWidth(mContext));
        }

        view.setFocusable(true); // 这个很重要
        view.setFocusableInTouchMode(true);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        setContentView(view);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isShowing()) {
                        dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
        adapter = new WhiteboardItemAdapter(context,R.layout.item_listformat_whiteboard_horizontal,views);
        hlv.setAdapter(adapter);
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemclick(position);
            }
        });
    }
    private onItemClickListener onItemClickListener;
    public interface onItemClickListener{
        void onItemclick(int position);
    }

    @Override
    protected boolean isDim() {
        return true;
    }
    public void refresh(List<View> views){
        if(views!=null){
            this.views.clear();
            this.views.addAll(views);
            adapter.notifyDataSetChanged();
        }

    }
}
