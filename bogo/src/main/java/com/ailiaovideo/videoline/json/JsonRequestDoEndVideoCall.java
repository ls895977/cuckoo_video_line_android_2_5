package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/19.
 */

public class JsonRequestDoEndVideoCall extends JsonRequestBase {

    private String total_profit;
    private String total_coin;
    private String fabulous;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestDoEndVideoCall getJsonObj(String json){

        JsonRequestDoEndVideoCall jsonObj;
        try {
            jsonObj = JSON.parseObject(json, JsonRequestDoEndVideoCall.class);
        }catch (Exception e){

            jsonObj = new JsonRequestDoEndVideoCall();
            jsonObj.setCode(0);
            jsonObj.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonObj;
    }

    public String getFabulous() {
        return fabulous;
    }

    public void setFabulous(String fabulous) {
        this.fabulous = fabulous;
    }

    public String getTotal_profit() {
        return total_profit;
    }

    public void setTotal_profit(String total_profit) {
        this.total_profit = total_profit;
    }

    public String getTotal_coin() {
        return total_coin;
    }

    public void setTotal_coin(String total_coin) {
        this.total_coin = total_coin;
    }
}
