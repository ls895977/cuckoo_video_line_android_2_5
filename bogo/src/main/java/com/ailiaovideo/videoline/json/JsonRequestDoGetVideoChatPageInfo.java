package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.SlideModel;

import java.util.List;

/**
 * Created by weipeng on 2018/2/27.
 */

public class JsonRequestDoGetVideoChatPageInfo extends JsonRequestBase {

    private List<SlideModel> slide;

    public List<SlideModel> getSlide() {
        return slide;
    }

    public void setSlide(List<SlideModel> slide) {
        this.slide = slide;
    }
}
