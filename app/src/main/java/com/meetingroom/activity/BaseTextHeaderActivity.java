package com.meetingroom.activity;

import android.view.View;
import android.widget.TextView;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * Created by Rays on 16/6/23.
 */
public class BaseTextHeaderActivity extends BaseActivity {

    /**
     * 设置标题
     */
    protected void setHeaderTitle(int resid) {
        setHeaderTitle(getText(resid));
    }

    /**
     * 设置标题
     */
    protected void setHeaderTitle(CharSequence name) {
        TextView view = ViewUtil.findViewById(this, R.id.header_title);
        if (view != null) {
            view.setText(name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置右边文字按钮
     * @param resid
     * @param onClickListener
     */
    protected TextView setHeaderRightText(int resid, View.OnClickListener onClickListener) {
        return setHeaderRightText(getText(resid), onClickListener);
    }

    /**
     * 设置右边文字按钮
     * @param text
     * @param onClickListener
     */
    protected TextView setHeaderRightText(CharSequence text, View.OnClickListener onClickListener) {
        TextView view = ViewUtil.findViewById(this, R.id.header_right);
        if (view != null) {
            view.setText(text);
            view.setOnClickListener(onClickListener);
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
