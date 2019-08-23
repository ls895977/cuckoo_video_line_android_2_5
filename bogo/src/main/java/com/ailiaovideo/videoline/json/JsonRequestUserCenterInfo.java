package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.json.jsonmodle.UserCenterData;

/**
 * @dw 个人中心页面数据
 * Created by weipeng on 2018/2/10.
 */

public class JsonRequestUserCenterInfo extends JsonRequestBase {

    private UserCenterData data;


    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestUserCenterInfo getJsonObj(String json){

        JsonRequestUserCenterInfo jsonRequestUserCenterInfo;
        try {
            jsonRequestUserCenterInfo = JSON.parseObject(json, JsonRequestUserCenterInfo.class);
        }catch (Exception e){

            jsonRequestUserCenterInfo = new JsonRequestUserCenterInfo();
            jsonRequestUserCenterInfo.setCode(0);
            jsonRequestUserCenterInfo.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonRequestUserCenterInfo;
    }

    public UserCenterData getData() {
        return data;
    }

    public void setData(UserCenterData data) {
        this.data = data;
    }



}
