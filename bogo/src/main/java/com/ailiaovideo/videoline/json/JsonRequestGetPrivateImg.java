package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.PrivatePhotoModel;

import java.util.List;

/**
 * Created by weipeng on 2018/2/24.
 */

public class JsonRequestGetPrivateImg extends JsonRequestBase {

    private List<PrivatePhotoModel> list;

    public List<PrivatePhotoModel> getList() {
        return list;
    }

    public void setList(List<PrivatePhotoModel> list) {
        this.list = list;
    }
}
