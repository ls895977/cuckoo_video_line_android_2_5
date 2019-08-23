package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.UserData;

/**
 * json返回解析类型
 * Created by jiahengfei on 2018/1/26 0026.
 */
public class JsonRequestUser {
    private int code;
    private String msg;
    private UserData data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestUser getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestUser.class);
    }


    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

    public UserData getData() {
        return data;
    }

    public JsonRequestUser(int code, String msg, UserData data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRequestUser() {
        super();
    }
}
