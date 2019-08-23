package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.modle.UserModel;

import java.util.List;

/**
 * json回传数组
 * Created by weipeng on 2018/1/26 0026.
 */

public class JsonRequestsTarget {

    private int code;
    private String msg;
    private List<TargetUserData> data;
    private List<UserModel> online_emcee;
    private String online_emcee_count;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestsTarget getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestsTarget.class);
    }

    public List<UserModel> getOnline_emcee() {
        return online_emcee;
    }

    public void setOnline_emcee(List<UserModel> online_emcee) {
        this.online_emcee = online_emcee;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<TargetUserData> data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public String getOnline_emcee_count() {
        return online_emcee_count;
    }

    public void setOnline_emcee_count(String online_emcee_count) {
        this.online_emcee_count = online_emcee_count;
    }

    public String getMsg() {
        return msg;
    }

    public List<TargetUserData> getData() {
        return data;
    }

    public JsonRequestsTarget(int code, String msg, List<TargetUserData> data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRequestsTarget() {
        super();
    }
}
