package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.VideoChatModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class VideoChatAdapter extends BaseQuickAdapter<VideoChatModel,BaseViewHolder>{
    public VideoChatAdapter(@Nullable List<VideoChatModel> data) {
        super(R.layout.item_video_chat_,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoChatModel item) {
        helper.setText(R.id.item_tv_content,item.getContent());
    }
}
