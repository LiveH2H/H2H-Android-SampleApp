package com.meetingroom.adapter;

import android.content.Context;

import com.meetingroom.utils.ResourcesUtil;

import itutorgroup.h2h.R;

/**
 * 会议列表适配器
 *
 * @author Rays 2016年4月12日
 */
public class IssuesListAdapter extends CommonAdapter<String> {
    private int colorDefault;
    private int colorCheck;
    private int positionCheck;

    public IssuesListAdapter(Context context) {
        super(context, R.layout.item_issues);
        colorDefault = ResourcesUtil.getColor(context, R.color.default_text);
        colorCheck = ResourcesUtil.getColor(context, R.color.btn_login_bg);
        resetPositionCheck();
    }

    @Override
    public void convert(ViewHolder holder, String obj, int position) {
        holder.setText(R.id.tvIssues, obj).setTextColor(position == positionCheck ? colorCheck : colorDefault);
    }

    public void setPositionCheck(int positionCheck) {
        this.positionCheck = positionCheck;
        notifyDataSetChanged();
    }

    public String getSelectedValue() {
        if (positionCheck > -1 && positionCheck < getCount()) {
            return getItem(positionCheck);
        }
        return null;
    }

    public void resetPositionCheck() {
        this.positionCheck = -1;
    }
}
