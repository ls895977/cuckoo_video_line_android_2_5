package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.modle.RankModel;

import java.util.List;

/**
 * json解析排行
 * Created by jiahengfei on 2018/1/30 0030.
 */

public class JsonRequestRank extends JsonRequestBase {
    private String order_num;
    private List<RankModel> list;

    /**
     * 返回json解析
     * @param json json
     * @return JsonRequest<T>
     */
    public static JsonRequestRank getJsonObj(String json){
        return JSON.parseObject(json, JsonRequestRank.class);
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }


    public List<RankModel> getList() {
        return list;
    }

    public void setList(List<RankModel> list) {
        this.list = list;
    }
}
