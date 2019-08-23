package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.DynamicCommonListModel;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DynamicCommonAdapter extends BaseQuickAdapter<DynamicCommonListModel,BaseViewHolder>{
    public DynamicCommonAdapter(@Nullable List<DynamicCommonListModel> data) {
        super(R.layout.item_dynamic_common,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicCommonListModel item) {

        helper.setText(R.id.item_tv_name,item.getUserInfo().getUser_nickname());
        helper.setText(R.id.item_tv_time,item.getAddtime());
        helper.setText(R.id.item_tv_content,item.getBody());
        Utils.loadHttpImg(MyApplication.getInstances(),item.getUserInfo().getAvatar(), (ImageView) helper.getView(R.id.item_iv_avatar),0);
    }
}
