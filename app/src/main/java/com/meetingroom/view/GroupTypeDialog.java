package com.meetingroom.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.meetingroom.adapter.delegate.GroupTypeAdapter;
import com.meetingroom.callback.GroupTypeSelectCallback;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年11月16日 10:12
 * 邮箱：nianbin@mosainet.com
 */
public class GroupTypeDialog extends Dialog {
    private View view;
    private Context mContext;
    private ListView lv;
    public GroupTypeDialog(Context context) {
        this(context, R.style.dialog);
    }
    public List<String> grouptype = new ArrayList<>();
    public GroupTypeAdapter adapter;
    public GroupTypeDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }
    public void init(List<String> grouptype, int selectIndex, final GroupTypeDialogCallback groupTypeDialogCallback){
        this.grouptype = grouptype;
        view = View.inflate(mContext,R.layout.layout_dialog_grouptype,null);
        lv = ViewUtil.findViewById(view,R.id.lv);
        adapter = new GroupTypeAdapter(mContext, R.layout.item_listformat_grouptype, this.grouptype, new GroupTypeSelectCallback() {
            @Override
            public void callback(int position) {
                if (groupTypeDialogCallback!=null) {
                    groupTypeDialogCallback.callback(position);
                }
                dismiss();
            }
        });
        adapter.index = selectIndex;
        lv.setAdapter(adapter);
        setContentView(view);
    }
    public interface GroupTypeDialogCallback{
        void callback(int index);
    }
}
