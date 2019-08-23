package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.CuckooSubscribeModel;

import java.util.List;

public class JsonDoGetSubscribeModelList extends JsonRequestBase{
    private List<CuckooSubscribeModel> list;

    public List<CuckooSubscribeModel> getList() {
        return list;
    }

    public void setList(List<CuckooSubscribeModel> list) {
        this.list = list;
    }
}
