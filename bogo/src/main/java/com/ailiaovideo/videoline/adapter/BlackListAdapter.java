package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.event.BlackEvent;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.utils.BGEventManage;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/19.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class BlackListAdapter extends BaseQuickAdapter<UserModel,BaseViewHolder> {
    private Context mContext;

    public BlackListAdapter(Context context,@Nullable List<UserModel> data) {
        super(R.layout.item_black,data);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, UserModel item) {

        Utils.loadHttpImg(mContext,Utils.getCompleteImgUrl(item.getAvatar()), (ImageView) helper.getView(R.id.item_iv_avatar));
        helper.setText(R.id.item_tv_name,item.getUser_nickname());
        helper.setOnClickListener(R.id.item_tv_relieve, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BlackEvent blackEvent = new BlackEvent();
                blackEvent.setPosition(helper.getPosition());
                BGEventManage.post(blackEvent);
            }
        });
    }
}
