package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.UserModel;

public class JsonRequestPerfectRegisterInfo extends JsonRequestBase{

    private UserModel data;

    public UserModel getData() {
        return data;
    }

    public void setData(UserModel data) {
        this.data = data;
    }
}
