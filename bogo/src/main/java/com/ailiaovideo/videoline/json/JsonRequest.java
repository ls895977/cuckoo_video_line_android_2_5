package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;

/**
 * json回传数组
 * Created by jiahengfei on 2018/1/26 0026.
 */

public class JsonRequest {
    private int code;
    private String msg;
    private Object data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequest getJsonObj(String json){
        return JSON.parseObject(json, JsonRequest.class);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public JsonRequest(int code, String msg, Object data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRequest() {
        super();
    }
}
