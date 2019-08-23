package com.ailiaovideo.videoline.json;

/**
 * Created by weipeng on 2018/2/10.
 */

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.modle.UserModel;

/**
* @dw 发起电话请求
* */
public class JsonRequestsDoCall extends JsonRequestBase {

    private RquestCallVideoData data;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestsDoCall getJsonObj(String json){

        JsonRequestsDoCall jsonRquestsDoCall;
        try {
            jsonRquestsDoCall = JSON.parseObject(json, JsonRequestsDoCall.class);
        }catch (Exception e){

            jsonRquestsDoCall = new JsonRequestsDoCall();
            jsonRquestsDoCall.setCode(0);
            jsonRquestsDoCall.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonRquestsDoCall;
    }

    public RquestCallVideoData getData() {
        return data;
    }

    public void setData(RquestCallVideoData data) {
        this.data = data;
    }

    public class RquestCallVideoData{
        private String channel_id;
        private UserModel to_user_base_info;

        public UserModel getTo_user_base_info() {
            return to_user_base_info;
        }

        public void setTo_user_base_info(UserModel to_user_base_info) {
            this.to_user_base_info = to_user_base_info;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }
    }


}
