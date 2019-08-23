package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.modle.TabLiveListModel;

import java.util.List;

/**
 * Created by weipeng on 2018/2/20.
 */

public class JsonRequestGetVideoLive extends JsonRequestBase {


    private List<TabLiveListModel> list;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestGetVideoLive getJsonObj(String json){

        JsonRequestGetVideoLive jsonObj;
        try {
            jsonObj = JSON.parseObject(json, JsonRequestGetVideoLive.class);
        }catch (Exception e){

            jsonObj = new JsonRequestGetVideoLive();
            jsonObj.setCode(0);
            jsonObj.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonObj;
    }


    public List<TabLiveListModel> getList() {
        return list;
    }

    public void setList(List<TabLiveListModel> list) {
        this.list = list;
    }


}
