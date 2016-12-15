package com.meetingroom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import itutorgroup.h2h.R;


/**
 * 描述:[-] 1/3 [+]
 * 作者：zhangjiawei
 * 时间：2016/10/9
 */
public class PlusMinButton extends LinearLayout {
    private ImageView iv_minus;
    private ImageView iv_plus;
    private TextView tv_count_page;

    public PlusMinButton(Context context) {
        super(context);
        init();
    }

    public PlusMinButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlusMinButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_plus_minus_button, this);
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        tv_count_page = (TextView) findViewById(R.id.tv_count_page);
        iv_minus.setVisibility(View.INVISIBLE);
        iv_plus.setVisibility(View.INVISIBLE);
        /*iv_minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String page = tv_count_page.getText().toString();
                String currentPage = page.split("/")[0];
                String countPage = page.split("/")[1];
                if (Integer.valueOf(currentPage) <= 1) {
                    tv_count_page.setText(1 + "/" + countPage);
                    return;
                }
                tv_count_page.setText((Integer.valueOf(currentPage) - 1) + "/" + countPage);
                notifyListener((Integer.valueOf(currentPage) - 1));
            }
        });

        iv_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String page = tv_count_page.getText().toString();
                String currentPage = page.split("/")[0];
                String countPage = page.split("/")[1];
                if (Integer.valueOf(currentPage) >= Integer.valueOf(countPage)) {
                    tv_count_page.setText(countPage + "/" + countPage);
                    return;
                }
                tv_count_page.setText((Integer.valueOf(currentPage) + 1) + "/" + countPage);
                notifyListener((Integer.valueOf(currentPage) + 1));
            }
        });*/
    }

    public void setCurrentPage(String newPage) {
        String page = tv_count_page.getText().toString();
        String countPage = page.split("/")[1];
        tv_count_page.setText(newPage + "/" + countPage);
//      notifyListener((Integer.valueOf(newPage)));
    }

    public void setCountPage(String newPage) {
        String page = tv_count_page.getText().toString();
        String currentPage = page.split("/")[0];
        tv_count_page.setText(currentPage + "/" + newPage);
//      notifyListener((Integer.valueOf(newPage)));
    }

    private void notifyListener(int currentPage) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageChange(currentPage);
        }
    }

    private OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;

    }

    public interface OnPageChangeListener {
        void onPageChange(int currentPage);
    }
}
