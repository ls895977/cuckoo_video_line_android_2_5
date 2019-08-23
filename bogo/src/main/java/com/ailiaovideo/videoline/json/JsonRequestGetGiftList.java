package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.GiftModel;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/6.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestGetGiftList extends JsonRequestBase {
    private List<GiftModel> list;

    public List<GiftModel> getList() {
        return list;
    }

    public void setList(List<GiftModel> list) {
        this.list = list;
    }
}
