package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * json回传数组
 * Created by jiahengfei on 2018/1/26 0026.
 */

public class JsonRequestAll<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 返回json解析
     * @param json json
     */
    public static <T> JsonRequestAll<T> getJson(String json){
        return JSON.parseObject(json, new TypeReference<JsonRequestAll<T>>(){});
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public JsonRequestAll(int code, String msg, T data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRequestAll() {
        super();
    }
}
