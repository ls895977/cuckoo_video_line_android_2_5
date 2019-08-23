package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.alibaba.fastjson.JSON;

/**
 * json返回解析类型TargetUserData
 * Created by weipeng on 2018/1/26 0026.
 */
public class JsonRequestTarget {
    private int code;
    private String msg;
    private TargetUserData data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestTarget getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestTarget.class);
    }

    public JsonRequestTarget() {
        super();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(TargetUserData data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getMsg() {
        return msg;
    }

    public TargetUserData getData() {
        return data;
    }

    public JsonRequestTarget(int code, String msg, TargetUserData data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
