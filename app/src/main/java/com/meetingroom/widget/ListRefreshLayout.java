package com.meetingroom.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Rays on 16/7/22.
 */
public class ListRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    // listview实例
    private ListView mListView;
    // 上拉接口监听器, 到了最底部的上拉加载操作
    private OnLoadListener mOnLoadListener;
    // ListView的加载中footer
    private View mListViewFooter;
    // 是否在加载中 ( 上拉加载更多 )
    private boolean isLoading;
    private boolean isLoadingEnabled;
    private boolean isScroll;

    public ListRefreshLayout(Context context) {
        this(context, null);
    }

    public ListRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        if (getChildCount() > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                mListView.setOnScrollListener(this);
            }
        }
    }

    /**
     * 设置加载状态,添加或者移除加载更多圆形进度条
     * @param loading
     */
    private void setLoading(boolean loading) {
        isLoading = loading;
        if (mListView == null || mListViewFooter == null) {
            return;
        }
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);

        }
    }

    /**
     * 加载完成
     */
    public void loadCompleted() {
        setLoading(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isLoadingEnabled && !isLoading && mListView != null && mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1) {
            setLoading(true);
            if (mOnLoadListener != null) {
                mOnLoadListener.onLoad();
            }
        }
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    public void setListViewFooter(View mListViewFooter) {
        this.mListViewFooter = mListViewFooter;
    }

    public void setLoadingEnabled(boolean loadingEnabled) {
        isLoadingEnabled = loadingEnabled;
    }

    /**
     * 加载更多的接口
     */
    public interface OnLoadListener {
        void onLoad();
    }
}
