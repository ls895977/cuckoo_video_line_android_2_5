package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.CuckooGiftCabinetModel;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/19.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestGiftCabinetList extends JsonRequestBase {

    private List<CuckooGiftCabinetModel> gift_list;

    public List<CuckooGiftCabinetModel> getGift_list() {
        return gift_list;
    }

    public void setGift_list(List<CuckooGiftCabinetModel> gift_list) {
        this.gift_list = gift_list;
    }
}