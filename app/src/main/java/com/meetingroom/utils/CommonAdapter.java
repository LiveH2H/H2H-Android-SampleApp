package com.meetingroom.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/** 
 * 万能适配器 
 * @param <T> 
 *            数据源的数据类型 
 */  
public abstract class CommonAdapter<T> extends BaseAdapter {  
    /** 
     * 上下文 
     */  
    protected Context mContext;  
    /** 
     * 数据源 
     */  
    protected List<T> listDatas;  
    /** 
     * Item布局ID 
     */  
    protected int layoutId;  
    public CommonAdapter(Context context, List<T> listDatas, int layoutId) {  
        this.mContext = context;  
        this.listDatas = listDatas;  
        this.layoutId = layoutId;  
    }  
  
    @Override  
    public int getCount() {  
        return listDatas == null ? 0 : listDatas.size();  
    }  
  
    @Override  
    /** 
     * 获取当前点击的Item的数据时用 
     * 在onItemClick中 parent.getAdapter().getItem(),获取当前点击的Item的数据 
     */  
    public Object getItem(int position) {  
        return listDatas.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    /** 
     * 只关心这一个方法 
     */  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	CommonViewHolder holder = CommonViewHolder.getViewHolder(mContext, convertView,  
                parent, layoutId);  
        fillData(holder, position);  
        return holder.getMConvertView();  
    }  
  
    /** 
     *  
     * 抽象方法，用于子类实现，填充数据 
     * @param holder 
     * @param position 
     */  
    protected abstract void fillData(CommonViewHolder holder, int position);  
  
    
    
} 