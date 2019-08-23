package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.view.View;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.utils.Utils;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicImagePreviewActivity extends BaseActivity {

    @BindView(R.id.photo_view)
    PhotoView photoView;

    public static final String IMAGE_PATH = "IMAGE_PATH";
    private String imagePath;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dynamic_image_preview;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        imagePath = getIntent().getStringExtra(IMAGE_PATH);

        Utils.loadHttpImg(this,imagePath,photoView,0);
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:

                finish();
                break;
            default:
                break;
        }
    }
}
