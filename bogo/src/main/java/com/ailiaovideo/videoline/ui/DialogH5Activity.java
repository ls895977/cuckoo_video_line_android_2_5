package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.utils.JavaScriptInterface;
import com.ailiaovideo.videoline.webview.config.ImageClickInterface;

public class DialogH5Activity extends BaseActivity {

    private WebView webView;
    private String newUrl;
    private String url = "";
    public static DialogH5Activity instance;
    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dialog_h5;
    }

    @Override
    protected void initView() {
        instance = this;

        webView = (WebView) findViewById(R.id.webview);
        findViewById(R.id.finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String uri = getIntent().getStringExtra("uri");

        url = uri;

        UserModel userModel = SaveData.getInstance().getUserInfo();
        newUrl = url + "/uid/" + userModel.getId();

        initWebView();
        webView.loadUrl(newUrl);
        webView.setBackgroundColor(0);


    }


    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(false);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 缩放比例 1
//        webView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "JavaScriptInterface");
        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

//        mWebChromeClient = new CuckooWebChromeClient(this);
//        webView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
//        webView.setWebViewClient(new CuckooWebViewClient(context));
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

}
