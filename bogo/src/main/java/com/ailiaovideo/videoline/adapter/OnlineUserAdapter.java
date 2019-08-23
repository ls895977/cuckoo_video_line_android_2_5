package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.OnlineUserModel;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/26.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class OnlineUserAdapter extends BaseQuickAdapter<OnlineUserModel,BaseViewHolder> {

    private int width;
    private Context mContext;

    public OnlineUserAdapter(Context context,@Nullable List<OnlineUserModel> data) {
        super(R.layout.item_online_user,data);
        width = ScreenUtils.getScreenWidth() / 3 - 30;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OnlineUserModel item) {

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width,width);
        params.setMargins(5,0,5,0);
        helper.getConvertView().setLayoutParams(params);
        helper.setText(R.id.tv_user_nickname,item.getUser_nickname());
        Utils.loadHttpImg(mContext,item.getAvatar(), (ImageView) helper.getView(R.id.iv_avatar));
        helper.addOnClickListener(R.id.tv_follow);

        if(StringUtils.toInt(item.getIs_follow()) == 1){
            helper.setVisible(R.id.tv_follow,false);
        }else{
            helper.setVisible(R.id.tv_follow,true);
        }
    }
}
