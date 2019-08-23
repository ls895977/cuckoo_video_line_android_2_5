package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DynamicImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int imgWidth = 0;

    public DynamicImgAdapter(@Nullable List<String> data) {
        super(R.layout.item_dynamic_img, data);
        imgWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(30)) / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        View view = helper.getConvertView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgWidth, imgWidth);
        params.setMargins(10, 10, 10, 10);
        view.setLayoutParams(params);

        Utils.loadHttpImg(MyApplication.getInstances(), item, (ImageView) helper.getView(R.id.item_iv_img),0);
    }
}
