package com.meetingroom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Rays on 16/6/30.
 */
public class ObservableScrollView extends ScrollView {
    private OnScrollChangedListener onScrollChangedListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
