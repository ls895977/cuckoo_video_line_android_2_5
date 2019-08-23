package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/10.
 */

public class JsonRequestBase {

    private int code;
    private String msg;


    /**
     * 返回json解析
     * @param json json
     */
    public static <T extends JsonRequestBase> JsonRequestBase getJsonObj(String json,Class<T> clz){

        JsonRequestBase jsonObj;
        try {
            jsonObj = JSON.parseObject(json, clz);
        }catch (Exception e){

            jsonObj = new JsonRequestBase();
            jsonObj.setCode(0);
            jsonObj.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonObj;
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
}
