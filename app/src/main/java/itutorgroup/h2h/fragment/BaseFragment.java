package itutorgroup.h2h.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import itutorgroup.h2h.widget.HintDialog;
import itutorgroup.h2h.widget.progress.DefaultProgressIndicator;
import itutorgroup.h2h.widget.progress.TextProgressIndicator;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年06月24日 15:06
 * 邮箱：nianbin@mosainet.com
 */
public class BaseFragment extends Fragment {
    private static final String fragment_level = "Fragemnt_Level";
    protected String tag = this.getClass().getSimpleName();
    private TextProgressIndicator textProgressIndicator;
    private HintDialog hintDialog;
    protected DefaultProgressIndicator progressIndicator;
    protected Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        Log.e(fragment_level,tag+"onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(fragment_level,tag+"onCreate");
        if(openEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(fragment_level,tag+"onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(fragment_level,tag+"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(fragment_level,tag+"onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(openEventBus()){
            EventBus.getDefault().unregister(this);
        }
        Log.e(fragment_level,tag+"onDestroy");
    }
    public void showTextProgressDialog(String message) {
        if (textProgressIndicator == null)
            textProgressIndicator = TextProgressIndicator.newInstance(mContext);
        textProgressIndicator.showDialog(message);
    }

    public void dismissTextProgressDialog() {
        if (textProgressIndicator != null)
            textProgressIndicator.dismissDialg();
    }
    public void showProgressDialog() {
        if (progressIndicator == null) {
            progressIndicator = DefaultProgressIndicator.newInstance(mContext);

        }
        progressIndicator.show();
    }

    public void dismissProgressDialog() {
        if (progressIndicator != null && progressIndicator.isShowing()) {
            progressIndicator.dismiss();
        }
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(int resId) {
        return showHintDialog(getText(resId));
    }

    /**
     * 显示提示框
     */
    public HintDialog showHintDialog(CharSequence text) {
        if (hintDialog == null) {
            hintDialog = new HintDialog(mContext);
        }
        hintDialog.setMessages(text);
        if (!hintDialog.isShowing()) {
            hintDialog.show();
        }
        return hintDialog;
    }


    /**
     * 隐藏提示框
     */
    public void dismissHintDialog() {
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
    }

    protected boolean openEventBus(){
        return false;
    }
}
