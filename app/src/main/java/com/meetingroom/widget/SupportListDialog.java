package com.meetingroom.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.meetingroom.adapter.IssuesListAdapter;
import com.meetingroom.utils.SystemUtil;

import java.util.Arrays;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * support提示框
 *
 * @author Rays 2016年4月12日
 */
public class SupportListDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private Button btnSend;
    private TextView tvTitle;
    private IssuesListAdapter adapter;
    private SendResultCallback sendResultCallback;
    public SupportListDialog(Context context) {
        this(context, R.style.dialog);
    }

    public SupportListDialog(Context context, int theme) {
        super(context, theme);
        initViews(context);
    }

    private void initViews(Context context) {
        if(context instanceof SendResultCallback){
            sendResultCallback = (SendResultCallback) context;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_support_list, null);
        tvTitle = ViewUtil.findViewById(view, R.id.tvTitle);
        btnSend = ViewUtil.findViewById(view, R.id.btnSend);
        btnSend.setOnClickListener(this);
        view.findViewById(R.id.ibtnClose).setOnClickListener(this);

        adapter = new IssuesListAdapter(context);
        listView = ViewUtil.findViewById(view, android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        setContentView(view);

        initWindowSize();
    }

    private void initWindowSize() {
        Window window = getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (SystemUtil.isTablet(getContext())) {
                layoutParams.width = (int) (metrics.widthPixels * 0.6f);
                layoutParams.height = (int) (metrics.heightPixels * 0.9f);
            } else {
                layoutParams.width = (int) (metrics.widthPixels * 0.95f);
                layoutParams.height = (int) (metrics.heightPixels * 0.67f);
            }
            layoutParams.gravity = Gravity.CENTER;
        }
    }

    public void changeValue(int issues, String title) {
        ViewUtil.setVisibility(btnSend, View.INVISIBLE);
        adapter.resetPositionCheck();
        adapter.replace(Arrays.asList(getContext().getResources().getStringArray(issues)));
        tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.btnSend:
                String issues = adapter.getSelectedValue();
                if(!TextUtils.isEmpty(issues)){
                    if(sendResultCallback!=null){
                        sendResultCallback.sendContent(issues);
                        dismiss();
                    }
//                    Intent intent = new Intent(getContext(), SupportChatActivity.class);
//                    intent.putExtra(SupportChatActivity.INTENT_MESSAGE, issues);
//                    getContext().startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setPositionCheck(position);
        ViewUtil.setVisibility(btnSend, View.VISIBLE);
    }
    public interface SendResultCallback{
        void sendContent(String issues);
    }
}
