package itutorgroup.h2h.fragment;


import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.itutorgroup.h2hmodel.H2HModel;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.mosai.utils.DensityUtil;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

public class WhiteBoardFragment extends BaseFragment {
    private WebView webView;
    private ProgressBar myProgressBar;
    private Button btnReload;
    public WhiteBoardFragment() {}

    public static WhiteBoardFragment newInstance() {
        WhiteBoardFragment fragment = new WhiteBoardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_white_board, container, false);
//        webView = ViewUtil.findViewById(view, R.id.webview);
//        myProgressBar = ViewUtil.findViewById(view, R.id.myProgressBar);
//        btnReload = ViewUtil.findViewById(view,R.id.btn_reload);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebview();
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
    }
    private void initWebview() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(DensityUtil.getWebviewScale(mContext));
        MyBrowser myBrowser = new MyBrowser();
        webView.setWebViewClient(myBrowser);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    myProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == myProgressBar.getVisibility()) {
                        myProgressBar.setVisibility(View.VISIBLE);
                    }
                    myProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        if (H2HWhiteboardManager.getInstance().getWhiteboardUrls().size()>0)
        webView.loadUrl(H2HWhiteboardManager.getInstance().getWhiteboardUrls().get(0));
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
}
