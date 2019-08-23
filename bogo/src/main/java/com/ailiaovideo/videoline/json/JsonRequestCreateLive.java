package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/20.
 */

public class JsonRequestCreateLive extends JsonRequestBase {

    private String push_url;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestCreateLive getJsonObj(String json){

        JsonRequestCreateLive jsonObj;
        try {
            jsonObj = JSON.parseObject(json, JsonRequestCreateLive.class);
        }catch (Exception e){

            jsonObj = new JsonRequestCreateLive();
            jsonObj.setCode(0);
            jsonObj.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonObj;
    }


    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }
}
