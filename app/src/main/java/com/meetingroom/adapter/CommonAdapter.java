package com.meetingroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用Adapter
 * @author Rays 2015年7月15日
 * 
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected List<T> data = new ArrayList<T>();
	protected LayoutInflater layoutInflater;
	protected Context context;
	private int layoutId;
	private boolean mNotifyOnChange = true;
	
	public CommonAdapter(Context context, int layoutId) {
		this.context = context;
		this.layoutId = layoutId;
		this.layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = ViewHolder.get(position, convertView, parent, context, layoutId, layoutInflater); 
        convert(viewHolder, getItem(position), position);  
        return viewHolder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T obj, int position);  
	
	public void insert(List<T> object) {
		data.addAll(0, object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void insert(List<T> object, int index) {
		data.addAll(index, object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void insert(T object) {
		data.add(0, object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void insert(T object, int index) {
		data.add(index, object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void add(List<T> object) {
		data.addAll(object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}

	public void add(T object) {
		data.add(object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void replace(List<T> object){
		data.clear();
		data.addAll(object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void clear(){
		data.clear();
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void remove(int index){
		data.remove(index);
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	public void remove(T object){
		data.remove(object);
		if (mNotifyOnChange) notifyDataSetChanged();
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
		if (mNotifyOnChange) notifyDataSetChanged();
	}
	
	@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }
	
	/**
	 * 数据变化时是否更新列表
	 * @param notifyOnChange
	 */
	public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
}
