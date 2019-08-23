package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/21.
 */

public class JsonRequestUpdateLiveHeart extends JsonRequestBase {

    private String push_url;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestUpdateLiveHeart getJsonObj(String json){

        JsonRequestUpdateLiveHeart jsonObj;
        try {
            jsonObj = JSON.parseObject(json, JsonRequestUpdateLiveHeart.class);
        }catch (Exception e){

            jsonObj = new JsonRequestUpdateLiveHeart();
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
