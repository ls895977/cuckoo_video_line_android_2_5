package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.NewPeople;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;

import java.util.List;

/**
 * 新人解析json
 * Created by weipeng on 2018/1/29 0029.
 */

public class JsonRequestsPeople extends JsonRequestBase{
    private List<TargetUserData> data;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestsPeople getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestsPeople.class);
    }

    public void setData(List<TargetUserData> data) {
        this.data = data;
    }


    public List<TargetUserData> getData() {
        return data;
    }

    public JsonRequestsPeople() {
        super();
    }

}
