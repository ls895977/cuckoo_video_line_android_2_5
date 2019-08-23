package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.AboutAndFans;

import java.util.List;

/**
 * json粉丝和关注列表
 * Created by jiahengfei on 2018/2/2 0002.
 */
public class JsonAboutFans {
    private int code;
    private String msg;
    private List<AboutAndFans> data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonAboutFans getJsonObj(String json){
        return JSON.parseObject(json, JsonAboutFans.class);
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

    public List<AboutAndFans> getData() {
        return data;
    }

    public void setData(List<AboutAndFans> data) {
        this.data = data;
    }

    public JsonAboutFans(int code, String msg, List<AboutAndFans> data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonAboutFans() {
        super();
    }
}
