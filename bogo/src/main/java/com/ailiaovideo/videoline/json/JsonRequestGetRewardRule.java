package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.RewardCoinModel;

import java.util.List;

public class JsonRequestGetRewardRule extends JsonRequestBase{

    private List<RewardCoinModel> list;

    public List<RewardCoinModel> getList() {
        return list;
    }

    public void setList(List<RewardCoinModel> list) {
        this.list = list;
    }
}
