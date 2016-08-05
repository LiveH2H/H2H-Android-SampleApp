package itutorgroup.h2h.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;


/**
 * 提示框
 * @author Rays 2016年4月12日
 *
 */
public class HintDialog extends Dialog implements View.OnClickListener {
	private TextView tvMessages, tvTitle;
	
	public HintDialog(Context context) {
		this(context, R.style.dialog);
	}
	
	public HintDialog(Context context, int theme) {
		super(context, theme);
		initViews(context);
	}

	private void initViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_hint, null);
        tvMessages = ViewUtil.findViewById(view, R.id.tvMessages);
        tvTitle = ViewUtil.findViewById(view, R.id.tvTitle);
		view.findViewById(R.id.btnOk).setOnClickListener(this);
		
		this.setContentView(view);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.width = (int) (metrics.widthPixels * 0.6f);
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		layoutParams.gravity = Gravity.CENTER;
	}

	@Override
	public void onClick(View v) {
		cancel();
	}

    public void setTitles(CharSequence text) {
        tvTitle.setText(text);
    }

    public void setTitles(int resid) {
        tvTitle.setText(resid);
    }

    public void setMessages(CharSequence text) {
        tvMessages.setText(text);
	}
	
	public void setMessages(int resid) {
        tvMessages.setText(resid);
	}
}
