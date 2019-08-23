package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.UserData;

import java.util.List;

/**
 * json回传数组
 * Created by jiahengfei on 2018/1/26 0026.
 */

public class JsonRequestsUser {
    private int code;
    private String msg;
    private List<UserData> data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestsUser getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestsUser.class);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<UserData> getData() {
        return data;
    }

    public JsonRequestsUser(int code, String msg, List<UserData> data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRequestsUser() {
        super();
    }
}
