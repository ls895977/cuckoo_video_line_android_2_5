package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/20.
 */

public class JsonRequestGetInviteCode extends JsonRequestBase {

    private String invite_code;

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }
}
