package com.ailiaovideo.videoline.modle;

import com.ailiaovideo.videoline.json.JsonRequestBase;

public class JsonDoGetVideoCallInfoModel extends JsonRequestBase{
    private String ext;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
