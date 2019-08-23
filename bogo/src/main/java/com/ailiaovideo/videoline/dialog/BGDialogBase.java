package com.ailiaovideo.videoline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.utils.BGViewUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class BGDialogBase extends Dialog implements DialogInterface.OnDismissListener {
    
    protected LinearLayout linearLayoutRoot;
    private View contentView;

    public static final int DEFAULT_PADDING_LEFT_RIGHT = ConvertUtils.dp2px(20);
    public static final int DEFAULT_PADDING_TOP_BOTTOM = ConvertUtils.dp2px(10);

    protected QMUITipDialog waitDialog;//声明一个QMUITipDialog对象


    public BGDialogBase(@NonNull Context context) {
        this(context, R.style.dialogBlackBg);
    }

    public BGDialogBase(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        baseInit();
    }

    private void baseInit() {

        linearLayoutRoot = new LinearLayout(getContext());
        linearLayoutRoot.setBackgroundColor(Color.parseColor("#00000000"));
        linearLayoutRoot.setGravity(Gravity.CENTER);
        this.setOnDismissListener(this);
        
        setCanceledOnTouchOutside(false);
    }

    public BGDialogBase paddingTop(int top)
    {
        linearLayoutRoot.setPadding(linearLayoutRoot.getPaddingLeft(), top, linearLayoutRoot.getPaddingRight(), linearLayoutRoot.getPaddingBottom());
        return this;
    }

    public BGDialogBase paddingBottom(int bottom)
    {
        linearLayoutRoot.setPadding(linearLayoutRoot.getPaddingLeft(), linearLayoutRoot.getPaddingTop(), linearLayoutRoot.getPaddingRight(), bottom);
        return this;
    }

    public BGDialogBase paddingLeft(int left)
    {
        linearLayoutRoot.setPadding(left, linearLayoutRoot.getPaddingTop(), linearLayoutRoot.getPaddingRight(), linearLayoutRoot.getPaddingBottom());
        return this;
    }

    public BGDialogBase paddingRight(int right)
    {
        linearLayoutRoot.setPadding(linearLayoutRoot.getPaddingLeft(), linearLayoutRoot.getPaddingTop(), right, linearLayoutRoot.getPaddingBottom());
        return this;
    }

    public BGDialogBase paddings(int paddings)
    {
        linearLayoutRoot.setPadding(paddings, paddings, paddings, paddings);
        return this;
    }

    /**
     * 设置窗口上下左右的边距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public BGDialogBase padding(int left, int top, int right, int bottom)
    {
        linearLayoutRoot.setPadding(left, top, right, bottom);
        return this;
    }

    private BGDialogBase setDialogView(View view, ViewGroup.LayoutParams params)
    {
        contentView = view;
        wrapperView(contentView);
        if (params == null)
        {
            params = new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        padding(DEFAULT_PADDING_LEFT_RIGHT, DEFAULT_PADDING_TOP_BOTTOM, DEFAULT_PADDING_LEFT_RIGHT, DEFAULT_PADDING_TOP_BOTTOM);
        super.setContentView(linearLayoutRoot, params);
        return this;
    }

    private void wrapperView(View view)
    {
        linearLayoutRoot.removeAllViews();
        linearLayoutRoot.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置高度
     *
     * @param width
     * @return
     */
    public BGDialogBase setWidth(int width)
    {
        BGViewUtil.setViewWidth(linearLayoutRoot, width);
        return this;
    }

    /**
     * 设置宽度
     *
     * @param height
     * @return
     */
    public BGDialogBase setHeight(int height)
    {
        BGViewUtil.setViewHeight(linearLayoutRoot, height);
        return this;
    }

    /**
     * 设置全屏
     *
     * @return
     */
    public BGDialogBase setFullScreen()
    {
        paddings(0);
        setWidth(BGViewUtil.getScreenWidth()).setHeight(ScreenUtils.getScreenHeight() - BGViewUtil.getStatusBarHeight());
        return this;
    }

    // ------------------------setContentView

    @Override
    public void setContentView(int layoutResID)
    {
        View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        this.setContentView(view, null);
    }

    public void setContentView(int layoutResID, ViewGroup.LayoutParams params)
    {
        View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        this.setContentView(view, params);
    }

    @Override
    public void setContentView(View view)
    {
        this.setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        setDialogView(view, params);
    }

    public View getContentView()
    {
        return contentView;
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        
    }

    protected void showLoadingDialog(String msg){

        if(waitDialog != null){
            waitDialog.dismiss();
        }
        waitDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        waitDialog.show();
    }

    protected void hideLoadingDialog(){
        if(waitDialog != null){
            waitDialog.dismiss();
        }
    }
}
