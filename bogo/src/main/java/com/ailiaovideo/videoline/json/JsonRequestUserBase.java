package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.modle.UserModel;

/**
 * json返回解析类型
 * Created by jiahengfei on 2018/1/26 0026.
 */
public class JsonRequestUserBase {
    private int code;
    private String msg;
    private UserModel data;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestUserBase getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestUserBase.class);
    }

    public JsonRequestUserBase() {
        super();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(UserModel data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

    public UserModel getData() {
        return data;
    }

    public JsonRequestUserBase(int code, String msg, UserModel data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
