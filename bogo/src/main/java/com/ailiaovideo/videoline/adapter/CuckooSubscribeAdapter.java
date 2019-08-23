package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.modle.CuckooSubscribeModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CuckooSubscribeAdapter extends BaseQuickAdapter<CuckooSubscribeModel,BaseViewHolder>{
    public CuckooSubscribeAdapter(@Nullable List<CuckooSubscribeModel> data) {
        super(R.layout.item_subscribe_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CuckooSubscribeModel item) {
        Utils.loadHttpImg(item.getAvatar(), (ImageView) helper.getView(R.id.item_iv_avatar));
        helper.setText(R.id.item_tv_name,item.getUser_nickname());
        helper.setText(R.id.item_tv_time,item.getCreate_time());
        helper.setText(R.id.item_tv_status,item.getStatus_msg());

        helper.setImageResource(R.id.pagemsg_view_dian, SelectResHelper.getOnLineRes(StringUtils.toInt(item.getIs_online())));
        helper.setText(R.id.pagemsg_view_isonline,StringUtils.toInt(item.getIs_online()) == 1 ? "在线" : "离线");
    }
}
