package com.meetingroom.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用ViewHolder
 * @author Rays 2015年7月15日
 *
 */
public class ViewHolder {
	private final SparseArray<View> mViews;
	private View mConvertView;

	private ViewHolder(int position, ViewGroup parent, Context context, int layoutId, LayoutInflater inflater) {
		mViews = new SparseArray<View>();
		mConvertView = inflater.inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * @param position
	 * @param convertView
	 * @param parent
	 * @param context
	 * @param layoutId
	 * @param inflater
	 * @return
	 */
	public static ViewHolder get(int position, View convertView, ViewGroup parent, 
			Context context, int layoutId, LayoutInflater inflater) {
		if (convertView == null) {
			return new ViewHolder(position, parent, context, layoutId, inflater);
		}
		return (ViewHolder) convertView.getTag();
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	/**
	 * 获取容器view
	 * @return
	 */
	public View getConvertView() {
		return mConvertView;
	}
	
	/**
	 * 设置TextView
	 * @param viewId
	 * @param text
	 * @return
	 */
	public TextView setText(int viewId, CharSequence text) {
		TextView view = getView(viewId);
		view.setText(text);
		return view;
	}
	
	/**
	 * 设置TextView
	 * @param viewId
	 * @param resid
	 * @return
	 */
	public TextView setText(int viewId, int resid) {
		TextView view = getView(viewId);
		view.setText(resid);
		return view;
	}

    public ImageView setImageByResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return view;
    }
	
	/**
	 * 设置ImageView
	 * @param viewId
	 * @param url
	 * @return
	 */
//	public ImageView setImageByUrl(int viewId, String url) {
//		ImageView view = getView(viewId);
//		String tag = (String) view.getTag();
//		if (tag == null || !tag.equals(url)) {
//			view.setTag(url);
//			ImageLoader.getInstance().displayImage(url, view);
//		}
//		return view;
//	}
	
	/**
	 * 设置ImageView
	 * @param viewId
	 * @param url
	 * @param options
	 * @return
	 */
//	public ImageView setImageByUrl(int viewId, String url, DisplayImageOptions options) {
//		ImageView view = getView(viewId);
//		String tag = (String) view.getTag();
//		if (tag == null || !tag.equals(url)) {
//			view.setTag(url);
//			ImageLoader.getInstance().displayImage(url, view, options);
//		}
//		return view;
//	}
	
	/**
	 * 设置ImageView
	 * @param viewId
	 * @param url
	 * @param options
	 * @return
	 */
//	public ImageView setImageByUrl(int viewId, String url, DisplayImageOptions options, ImageLoadingListener listener) {
//		ImageView view = getView(viewId);
//		String tag = (String) view.getTag();
//		if (tag == null || !tag.equals(url)) {
//			view.setTag(url);
//			ImageLoader.getInstance().displayImage(url, view, options, listener);
//		}
//		return view;
//	}
	
	/**
	 * 设置CheckBox
	 * @param viewId
	 * @param checked
	 * @return
	 */
	public CheckBox setCheckBox(int viewId, boolean checked) {
		CheckBox view = getView(viewId);
		view.setChecked(checked);
		return view;
	}
}
