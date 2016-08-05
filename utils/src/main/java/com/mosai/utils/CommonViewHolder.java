package com.mosai.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommonViewHolder {

	/**
	 * View容器，用于存放Holer中的View 照顾下小白 SparseArray 是Android推荐使用的一个优化容器，相当于一个Map
	 * <integer,View>
	 */
	private SparseArray<View> mViews;

	/**
	 * Item布局View convertView
	 */
	private View mConvertView;

	public CommonViewHolder(Context context, ViewGroup parent, int layoutId) {
		mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
		mConvertView.setTag(this);
	}

	/**
	 * 获取ViewHolder
	 * 
	 * @param context
	 *            上下文
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 *            布局layout Id
	 * @param position
	 * @return
	 */
	public static CommonViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId) {

		if (convertView == null)
			return new CommonViewHolder(context, parent, layoutId);
		return (CommonViewHolder) convertView.getTag();
	}

	/**
	 * 获取Holder中的ItemView
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {

		View item = mViews.get(viewId);
		if (item == null) {
			item = mConvertView.findViewById(viewId);
			mViews.put(viewId, item);
		}
		return (T) item;
	}

	/**
	 * 获取convertView
	 * 
	 * @return
	 */
	public View getMConvertView() {
		return mConvertView;
	}
}