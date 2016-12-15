package com.meetingroom.view;

import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.meetingroom.utils.LogUtils;
import com.mosai.utils.DensityUtil;

import itutorgroup.h2h.utils.ViewUtil;

import static com.itutorgroup.h2hwhiteboard.H2HWhiteboardConstant.ASSETS_DRAW_INDEX;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月30日 14:25
 * 邮箱：nianbin@mosainet.com
 */
public class MyWebView extends FrameLayout {
    public WebView webView;
    //    private Context context;
    private ProgressBar progressBar;

    public MyWebView(Context context, String url) {
        super(context);
//      this.context = context;
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 2)));
        webView = new WebView(context);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(webView);
        addView(progressBar);
        initWebview(url);
    }

    private void initWebview(String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(false);
//        if (!SystemUtil.isTablet(context)) {
//            webView.setInitialScale(50);
//        }
//        webView.setInitialScale(200);
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //message为js方法返回值
                Log.e("message", "onJsAlert: " + message);
                if (!"undefined".equals(message) && jsReturnListener != null) {
                    jsReturnListener.onReceiveValue(message);
                }
                result.confirm();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    ViewUtil.setVisibility(progressBar, View.INVISIBLE);
                } else {
                    ViewUtil.setVisibility(progressBar, View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

        });
        //String url1="file:///android_asset/fabric/index.html";
        webView.loadUrl(ASSETS_DRAW_INDEX);
    }

    public void setJSUrl(String jsUrl) {
        webView.loadUrl(jsUrl);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtils.e("onPageFinished()");
        }
    }


    /**
     * 调用js的return
     */
    public interface OnJsReturnListener {
        void onReceiveValue(String s);
    }

    private OnJsReturnListener jsReturnListener;

    public void setOnJsReturnListener(final OnJsReturnListener jsReturnListener) {
        this.jsReturnListener = jsReturnListener;
    }
}
