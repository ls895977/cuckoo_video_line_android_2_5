package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/19.
 */

public class JsonRequestHangUpVideoCall extends JsonRequestBase {
    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestHangUpVideoCall getJsonObj(String json){

        JsonRequestHangUpVideoCall jsonObj;
        try {
            jsonObj = JSON.parseObject(json, JsonRequestHangUpVideoCall.class);
        }catch (Exception e){

            jsonObj = new JsonRequestHangUpVideoCall();
            jsonObj.setCode(0);
            jsonObj.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonObj;
    }
}
