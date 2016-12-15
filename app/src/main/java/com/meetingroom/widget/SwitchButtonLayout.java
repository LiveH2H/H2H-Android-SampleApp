package com.meetingroom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import itutorgroup.h2h.R;

/**
 * Created by Rays on 2016/10/31.
 */

public class SwitchButtonLayout extends LinearLayout {
    private SwitchButton switchButton;
    private TextView switchText;

    public SwitchButtonLayout(Context context) {
        this(context, null);
    }

    public SwitchButtonLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchButtonLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.switch_button_text, this);
        this.setGravity(Gravity.CENTER_VERTICAL);
        int padding = getResources().getDimensionPixelSize(R.dimen.standard_padding_small);
        this.setPadding(0, padding, 0, padding);

        switchButton = (SwitchButton) findViewById(R.id.switchButton);
        switchText = (TextView) findViewById(R.id.switchText);

        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButtonLayout);
        if (ta != null) {
            switchText.setText(ta.getString(R.styleable.SwitchButtonLayout_sbl_text));
            switchButton.setChecked(ta.getBoolean(R.styleable.SwitchButtonLayout_sbl_checked, false));
            ta.recycle();
        }
    }

    public void setText(int resId) {
        switchText.setText(resId);
    }

    public void setText(CharSequence text) {
        switchText.setText(text);
    }

    public String getValue() {
        return (String) getTag();
    }

    public boolean isChecked() {
        return switchButton.isChecked();
    }

}
