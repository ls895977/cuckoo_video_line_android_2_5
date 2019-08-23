package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.CuckooVideoCallListModel;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CuckooVideoCallListAdapter extends BaseQuickAdapter<CuckooVideoCallListModel,BaseViewHolder>{
    public CuckooVideoCallListAdapter(@Nullable List data) {
        super(R.layout.item_video_call_list,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CuckooVideoCallListModel item) {
        Utils.loadHttpImg(item.getAvatar(), (ImageView) helper.getView(R.id.item_iv_avatar));
        helper.setText(R.id.item_tv_name,item.getUser_nickname());
        helper.setText(R.id.item_tv_status,item.getCreate_time());
    }
}
