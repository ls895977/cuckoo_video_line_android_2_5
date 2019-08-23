package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.audiorecord.entity.AudioEntity;
import com.ailiaovideo.videoline.audiorecord.view.CommonSoundItemView;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.DynamicListModel;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.DynamicImagePreviewActivity;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.BGLevelTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DynamicAdapter extends BaseQuickAdapter<DynamicListModel, BaseViewHolder> {

    private Context mContext;

    public DynamicAdapter(Context context, @Nullable List<DynamicListModel> data) {
        super(R.layout.item_dynamic, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final DynamicListModel item) {
        UserModel userInfo = item.getUserInfo();

        helper.setText(R.id.item_tv_content, item.getMsg_content());
        if (userInfo != null) {
            helper.setText(R.id.item_tv_name, userInfo.getUser_nickname());
        }
        helper.setText(R.id.item_tv_time, item.getPublish_time());
        //回复
        helper.setText(R.id.item_tv_common_count, item.getComment_count());
        //点赞
        helper.setText(R.id.item_tv_like_count, item.getLike_count());

        RecyclerView rv = helper.getView(R.id.rv_photo_list);
        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        DynamicImgAdapter dynamicImgAdapter = new DynamicImgAdapter(item.getThumbnailPicUrls());
        rv.setAdapter(dynamicImgAdapter);
        dynamicImgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, DynamicImagePreviewActivity.class);
                intent.putExtra(DynamicImagePreviewActivity.IMAGE_PATH, item.getOriginalPicUrls().get(position));
                mContext.startActivity(intent);
            }
        });

        if (StringUtils.toInt(item.getIs_audio()) == 1) {
            helper.setGone(R.id.pp_sound_item_view, true);
        } else {
            helper.setGone(R.id.pp_sound_item_view, false);
        }

        CommonSoundItemView commonSoundItemView = helper.getView(R.id.pp_sound_item_view);
        AudioEntity audioEntity = new AudioEntity();
        audioEntity.setUrl(item.getAudio_file());
        commonSoundItemView.setSoundData(audioEntity);
        if (userInfo != null) {
            Utils.loadHttpIconImg(mContext, userInfo.getAvatar(), (ImageView) helper.getView(R.id.item_iv_avatar), 0);
        }

        //点赞
        helper.addOnClickListener(R.id.item_iv_like_count);

        if (StringUtils.toInt(item.getIs_like()) == 1) {
            helper.setBackgroundRes(R.id.item_iv_like_count, R.mipmap.ic_dynamic_thumbs_up_s);
        } else {
            helper.setBackgroundRes(R.id.item_iv_like_count, R.mipmap.ic_dynamic_thumbs_up_n);
        }

        if (StringUtils.toInt(item.getUid()) == StringUtils.toInt(SaveData.getInstance().getId())) {
            helper.setGone(R.id.item_del, true);
        } else {
            helper.setGone(R.id.item_del, false);
        }

        if (item.getUserInfo() != null) {

            ((BGLevelTextView) helper.getView(R.id.tv_level)).setLevelInfo(item.getUserInfo().getSex(), item.getUserInfo().getLevel());
        }
        helper.addOnClickListener(R.id.item_tv_chat);
        helper.addOnClickListener(R.id.item_del);
        helper.addOnClickListener(R.id.item_iv_avatar);
    }
}
