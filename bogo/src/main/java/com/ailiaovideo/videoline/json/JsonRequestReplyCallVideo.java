package com.ailiaovideo.videoline.json;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

/**
 * Created by weipeng on 2018/2/18.
 */

public class JsonRequestReplyCallVideo extends JsonRequestBase {


    private RquestReplyCallVideoData data;

    /**
     * 返回json解析
     * @param json json
     */
    public static JsonRequestReplyCallVideo getJsonObj(String json){

        JsonRequestReplyCallVideo jsonRequestReplyCallVideo;
        try {
            jsonRequestReplyCallVideo = JSON.parseObject(json, JsonRequestReplyCallVideo.class);
        }catch (Exception e){

            jsonRequestReplyCallVideo = new JsonRequestReplyCallVideo();
            jsonRequestReplyCallVideo.setCode(0);
            jsonRequestReplyCallVideo.setMsg(e.getMessage());
            LogUtils.i(">>>>>>>>>>>>>>>数据解析异常" + e.getMessage());
        }

        return jsonRequestReplyCallVideo;
    }

    public RquestReplyCallVideoData getData() {
        return data;
    }

    public void setData(RquestReplyCallVideoData data) {
        this.data = data;
    }

    public class RquestReplyCallVideoData{
        private String to_user_id;
        private String channel;

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }

}
