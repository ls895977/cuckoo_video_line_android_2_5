package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.RewardCoinModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CuckooRewardCoinAdapter extends BaseQuickAdapter<RewardCoinModel,BaseViewHolder>{
    public CuckooRewardCoinAdapter(@Nullable List<RewardCoinModel> data) {
        super(R.layout.item_reward_coin,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RewardCoinModel item) {
        helper.setText(R.id.item_tv_coin,"打赏" + item.getReward_coin_num() + ConfigModel.getInitData().getCurrency_name());
    }

    public void refresh() {
        RewardCoinModel rewardCoinModel = new RewardCoinModel();
        rewardCoinModel.setId("7777777");
        rewardCoinModel.setReward_coin_num("其他金额");
        getData().add(rewardCoinModel);
        notifyDataSetChanged();
    }
}
