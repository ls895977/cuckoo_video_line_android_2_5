package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.InviteUserListModel;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/14.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class InviteUserListAdapter extends BaseQuickAdapter<InviteUserListModel,BaseViewHolder> {
    private Context context;


    public InviteUserListAdapter(Context context,@Nullable List<InviteUserListModel> data) {
        super(R.layout.item_invite_user_list,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteUserListModel item) {

        Utils.loadHttpImg(context,Utils.getCompleteImgUrl(item.getAvatar()), (ImageView) helper.getView(R.id.item_iv_avatar));
        helper.setText(R.id.item_tv_name,item.getUser_nickname());
        helper.setText(R.id.item_tv_income,"总奖励:" + item.getIncome_total());
    }
}
