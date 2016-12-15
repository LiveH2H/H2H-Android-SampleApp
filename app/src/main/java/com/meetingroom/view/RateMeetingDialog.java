package com.meetingroom.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.meetingroom.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import itutorgroup.h2h.R;

/**
 * 评分弹出框
 *
 * @author Rays 2016年4月12日
 *
 */
public class RateMeetingDialog extends Dialog{

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tv_thanks)
    TextView tv_thanks;

	public RateMeetingDialog(Context context) {
		this(context, R.style.dialog);
	}

	public RateMeetingDialog(Context context, int theme) {
		super(context, theme);
		initViews(context);
	}

	private void initViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_rate_meeting, null);
        this.setContentView(view);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);


        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowing())
                    dismiss();
                if(onDialogClickListener!=null){
                    onDialogClickListener.closeCallback();
                }
            }
        });

        TextView tvH2HInfo = ((TextView) view.findViewById(R.id.tv_liveh2h_info));
        tvH2HInfo.setMovementMethod(LinkMovementMethod.getInstance());
        tvH2HInfo.setText(Html.fromHtml(context.getString(R.string.liveh2h_information)));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    ratingBar.setVisibility(View.GONE);
                    tv_thanks.setVisibility(View.VISIBLE);
                }
            }
        });

        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = Tools.getScreenWidth(context);
        layoutParams.height = Tools.getScreenHeight(context);
		layoutParams.gravity = Gravity.BOTTOM;
	}

    public void setOnDialogClickListener(RateMeetingDialog.onDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    onDialogClickListener onDialogClickListener;
    public interface onDialogClickListener {
        void closeCallback();
    }

}
