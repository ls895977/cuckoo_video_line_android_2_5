package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.modle.ConfigModel;

/**
 * json返回解析类型
 * Created by jiahengfei on 2018/1/26 0026.
 */
public class JsonRequestConfig {
    private int code;
    private String msg;
    private ConfigModel data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestConfig getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestConfig.class);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ConfigModel getData() {
        return data;
    }

    public void setCode(int code) {

        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(ConfigModel data) {
        this.data = data;
    }

    public JsonRequestConfig(int code, String msg, ConfigModel data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRequestConfig() {
        super();
    }
}
