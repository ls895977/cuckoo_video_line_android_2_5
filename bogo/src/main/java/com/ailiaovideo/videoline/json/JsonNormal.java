package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.AboutAndFans;

import java.util.List;

/**
 * json粉丝和关注列表
 * Created by jiahengfei on 2018/2/2 0002.
 */
public class JsonNormal {
    private int code;
    private String msg;

    public static JsonNormal getJsonObj(String json){
        return JSON.parseObject(json, JsonNormal.class);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public JsonNormal(int code, String msg) {

        this.code = code;
        this.msg = msg;

    }

    public JsonNormal() {
        super();
    }
}
