package itutorgroup.h2h.widget.progress;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosai.ui.render.LevelLoadingRenderer;
import com.mosai.ui.render.LoadingDrawable;
import com.mosai.utils.ToastUtils;

import itutorgroup.h2h.R;


/**
 * Created by Rays on 16/5/12.
 */
public class TextProgressIndicator extends Dialog implements IProgressIndicator{
    private Context context;
    public static TextProgressIndicator newInstance(Context context) {
        return new TextProgressIndicator(context);
    }

    public static TextProgressIndicator newInstance(Context context, int themeResId) {
        return new TextProgressIndicator(context);
    }

    public TextProgressIndicator(Context context) {
        this(context, R.style.dialog);
    }

    public TextProgressIndicator(Context context, int themeResId) {
        super(context, themeResId);
        initViews(context);
    }
    ImageView iv;
    TextView tv;
    private void initViews(Context context) {
        this.context = context;
        View view = View.inflate(context,R.layout.layout_dialog_loading,null);
        iv = (ImageView) view.findViewById(R.id.iv_progress);
        tv = (TextView) view.findViewById(R.id.messages);
        LoadingDrawable loadingDrawable = new LoadingDrawable(new LevelLoadingRenderer(context).setCircleColor(context.getResources().getColor(R.color.colorPrimary)));
        iv.setImageDrawable(loadingDrawable);
        loadingDrawable.start();
        this.setContentView(view);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }
    public void showDialog(){
        if(!isShowing()){
            show();
        }
    }
    public void showDialog(String message){
        if(!isShowing()){
            tv.setText(message);
            show();
        }
    }
    public void dismissDialg(){
        if(isShowing()){
            dismiss();
        }
    }

    @Override
    public void showErrorInfo(String message) {
        ToastUtils.showToast(context,message);
    }

    @Override
    public void showProgress() {
        showDialog();
    }

    @Override
    public void dismissProgress() {
        dismissDialg();
    }
}
