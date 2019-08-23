package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.PrivatePhotoModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * Created by weipeng on 2018/2/24.
 */

public class RecyclePrivatePhotoAdapter extends BaseQuickAdapter<PrivatePhotoModel,BaseViewHolder> {

    private Context context;
    private int itemWidth;

    public RecyclePrivatePhotoAdapter(@Nullable Context context,@Nullable List<PrivatePhotoModel> data) {
        super(R.layout.item_private_photo,data);
        this.context = context;
        itemWidth = ScreenUtils.getScreenWidth() / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, PrivatePhotoModel item) {

        ImageView ivPhoto = (ImageView) helper.getView(R.id.iv_photo);
        View view = helper.getConvertView();
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemWidth));

        Utils.loadHttpImg(context,Utils.getCompleteImgUrl(item.getImg()), ivPhoto);
        if(StringUtils.toInt(item.getStatus()) == 0){
            helper.setText(R.id.tv_status,"审核中");
        }else if(StringUtils.toInt(item.getStatus()) == 2){
            helper.setText(R.id.tv_status,"审核不通过");
        }

        helper.setVisible(R.id.rl_status,!(StringUtils.toInt(item.getStatus()) == 1));

    }
}
