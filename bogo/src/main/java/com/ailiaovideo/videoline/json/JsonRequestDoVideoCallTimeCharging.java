package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/19.
 */

public class JsonRequestDoVideoCallTimeCharging extends JsonRequestBase {

    private String charging_coin;

    public String getCharging_coin() {
        return charging_coin;
    }

    public void setCharging_coin(String charging_coin) {
        this.charging_coin = charging_coin;
    }

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestDoVideoCallTimeCharging getJsonObj(String json){

        JsonRequestDoVideoCallTimeCharging jsonObj;
        try {
            jsonObj = JSON.parseObject(json, JsonRequestDoVideoCallTimeCharging.class);
        }catch (Exception e){

            jsonObj = new JsonRequestDoVideoCallTimeCharging();
            jsonObj.setCode(0);
            jsonObj.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonObj;
    }
}
