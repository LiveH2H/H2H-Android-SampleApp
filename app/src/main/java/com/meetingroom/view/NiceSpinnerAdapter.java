package com.meetingroom.view;

import android.content.Context;

import java.util.List;

/**
 * @author angelo.marchesin
 */

public class NiceSpinnerAdapter<T> extends NiceSpinnerBaseAdapter {

    private List<T> mItems;

    public NiceSpinnerAdapter(Context context, List<T> items, int textColor, int backgroundSelector) {
        super(context, textColor, backgroundSelector);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size() - 1;
    }

    @Override
    public T getItem(int position) {
        if (position >= mSelectedIndex) {
            return mItems.get(position + 1);
        } else {
            return mItems.get(position);
        }
    }

    @Override
    public T getItemInDataset(int position) {
        return mItems.get(position);
    }


    public void refresh(List<T> mItems){
        this.mItems.clear();
        this.mItems.addAll(mItems);
        notifyDataSetChanged();
    }
}