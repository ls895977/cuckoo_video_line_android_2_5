package cn.tillusory.tiui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.tiui.view.TiBeautyView;

/**
 * Created by Anko on 2018/5/12.
 * Copyright (c) 2018 拓幻科技 - tillusory.cn. All rights reserved.
 */
public class TiPanelLayout extends ConstraintLayout {

    private TiSDKManager tiSDKManager;
    private ImageView tiBeautyIV;
    private TiBeautyView tiBeautyView;

    public TiPanelLayout(Context context) {
        super(context);
    }

    public TiPanelLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TiPanelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TiPanelLayout init(@NonNull TiSDKManager tiSDKManager) {
        this.tiSDKManager = tiSDKManager;

        initView();

        initData();

        return this;
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ti_panel, this);

        tiBeautyIV = findViewById(R.id.tiBeautyIV);

        tiBeautyView = findViewById(R.id.tiBeautyTrimView);
    }

    private void initData() {

        tiBeautyView.init(tiSDKManager);

        tiBeautyIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                tiBeautyIV.setSelected(!tiBeautyIV.isSelected());

                tiBeautyView.setVisibility(tiBeautyIV.isSelected() ? VISIBLE : GONE);
            }
        });

        //空白处隐藏面板
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.performClick();

                tiBeautyIV.setSelected(false);
                tiBeautyView.setVisibility(GONE);

                return false;
            }
        });
    }
}
