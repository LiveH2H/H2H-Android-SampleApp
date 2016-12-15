package com.meetingroom.adapter;

import android.content.Context;
import android.view.View;

import com.itutorgroup.h2hconference.H2HTranslator;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月19日 19:15
 * 邮箱：nianbin@mosainet.com
 */
public class TranslateLanguageAdapter extends CommonAdapter<H2HTranslator> {
    public int position = 0;

    public TranslateLanguageAdapter(Context context, int layoutId, List<H2HTranslator> datas) {
        super(context, layoutId, datas);
    }
    @Override
    protected void convert(ViewHolder viewHolder, H2HTranslator item, final int position) {
        viewHolder.getView(R.id.rl_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateLanguageAdapter.this.position = position;
                notifyDataSetChanged();
            }
        });
        viewHolder.setText(R.id.tv_language, item.getLanguage());
        viewHolder.getView(R.id.iv_check).setVisibility(position == this.position ? View.VISIBLE : View.INVISIBLE);
    }
}
