package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.utils.Utils;
import com.github.chrisbanes.photoview.PhotoView;

public class PerViewImgActivity extends BaseActivity {

    private PhotoView photoView;

    public static final String IMAGE_PATH = "IMAGE_PATH";
    private String path;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_per_view_img;
    }

    @Override
    protected void initView() {
        photoView = findViewById(R.id.photo_view);

    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        path = getIntent().getStringExtra(IMAGE_PATH);
        if(path != null){
            Utils.loadHttpImg(this,path,photoView);
        }
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    public static final void startPerViewImg(Context context,String path){
        Intent intent = new Intent(context,PerViewImgActivity.class);
        intent.putExtra(PerViewImgActivity.IMAGE_PATH,path);
        context.startActivity(intent);
    }
}
