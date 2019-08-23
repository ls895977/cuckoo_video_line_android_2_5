package com.ailiaovideo.videoline.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.dialog.BGDialogBase;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.utils.JavaScriptInterface;
import com.ailiaovideo.videoline.webview.config.CuckooWebChromeClient;
import com.ailiaovideo.videoline.webview.config.CuckooWebViewClient;
import com.ailiaovideo.videoline.webview.config.ImageClickInterface;

import java.util.List;

/**
 * Created by Administrator on 2015/10/20 0020.
 * 首页广告管理类
 */
public class AdManager extends BGDialogBase {

    private Activity context;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private View contentView;
    private RelativeLayout adRootContent;
    private AnimDialogUtils animDialogUtils;
    /**
     * 广告弹窗距离两侧的距离-单位(dp)
     */
    private int padding = 44;
    /**
     * 广告弹窗的宽高比
     */
    private float widthPerHeight = 0.75f;

    // 弹窗背景是否透明
    private boolean isAnimBackViewTransparent = false;
    // 弹窗是否可关闭
    private boolean isDialogCloseable = true;
    // 弹窗关闭点击事件
    private View.OnClickListener onCloseClickListener = null;
    // 设置弹窗背景颜色
    private int backViewColor = Color.parseColor("#FFD350");
    // 弹性动画弹性参数
    private double bounciness = AdConstant.BOUNCINESS;
    // 弹性动画速度参数
    private double speed = AdConstant.SPEED;
    // viewPager滑动动画效果
    private ViewPager.PageTransformer pageTransformer = null;
    // 是否覆盖全屏幕
    private boolean isOverScreen = true;

    private OnImageClickListener onImageClickListener = null;
    private WebView webView;
    private String url = "";
    private final String newUrl;

    public AdManager(Activity context, String uri) {
        super(context, R.style.dialogGift);
        this.context = context;
        url = uri;

        UserModel userModel = SaveData.getInstance().getUserInfo();
        newUrl = url + "/uid/" + userModel.getId();
        initView();

    }

    private void initView() {
        setContentView(R.layout.ad_dialog_content_layout);

        adRootContent = (RelativeLayout) findViewById(R.id.ad_root_content);

        adRootContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        webView = (WebView) findViewById(R.id.webview);
        initWebView();

        webView.loadUrl(newUrl);
        webView.setBackgroundColor(0);


        Window dialogWindow = context.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.TOP);
        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 120;//设置Dialog距离底部的距离


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
        webView.addJavascriptInterface(new ImageClickInterface(context), "injectedObject");
//        webView.setWebViewClient(new CuckooWebViewClient(context));
    }


    // ######################## 点击事件处理操作类 ########################

    /**
     * ViewPager每一项的单击事件
     */
    public interface OnImageClickListener {

//        public void onImageClick(View view, AdInfo advInfo);

    }

    // ######################## get set方法 #########################

    /**
     * 设置弹窗距离屏幕左右两侧的距离
     *
     * @param padding
     * @return
     */
    public AdManager setPadding(int padding) {
        this.padding = padding;

        return this;
    }

    /**
     * 设置弹窗宽高比
     *
     * @param widthPerHeight
     * @return
     */
    public AdManager setWidthPerHeight(float widthPerHeight) {
        this.widthPerHeight = widthPerHeight;

        return this;
    }

    /**
     * 设置ViewPager Item点击事件
     *
     * @param onImageClickListener
     * @return
     */
    public AdManager setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;

        return this;
    }

    /**
     * 设置背景是否透明
     *
     * @param animBackViewTransparent
     * @return
     */
    public AdManager setAnimBackViewTransparent(boolean animBackViewTransparent) {
        isAnimBackViewTransparent = animBackViewTransparent;

        return this;
    }

    /**
     * 设置弹窗关闭按钮是否可见
     *
     * @param dialogCloseable
     * @return
     */
    public AdManager setDialogCloseable(boolean dialogCloseable) {
        isDialogCloseable = dialogCloseable;

        return this;
    }

    /**
     * 设置弹窗关闭按钮点击事件
     *
     * @param onCloseClickListener
     * @return
     */
    public AdManager setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;

        return this;
    }

    /**
     * 设置弹窗背景颜色
     *
     * @param backViewColor
     * @return
     */
    public AdManager setBackViewColor(int backViewColor) {
        this.backViewColor = backViewColor;

        return this;
    }

    /**
     * 设置弹窗弹性动画弹性参数
     *
     * @param bounciness
     * @return
     */
    public AdManager setBounciness(double bounciness) {
        this.bounciness = bounciness;

        return this;
    }

    /**
     * 设置弹窗弹性动画速度参数
     *
     * @param speed
     * @return
     */
    public AdManager setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    /**
     * 设置ViewPager滑动动画效果
     *
     * @param pageTransformer
     */
    public AdManager setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        this.pageTransformer = pageTransformer;

        return this;
    }

    /**
     * 设置弹窗背景是否覆盖全屏幕
     *
     * @param overScreen
     * @return
     */
    public AdManager setOverScreen(boolean overScreen) {
        isOverScreen = overScreen;

        return this;
    }
}
