package com.ailiaovideo.videoline.json;

/**
 * Created by weipeng on 2018/2/28.
 */

public class JsonRequestDoGetVideoEndInfo extends JsonRequestBase {

    private String video_count;
    private String gift_count;
    private String total_count;
    private String is_follow;

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getVideo_count() {
        return video_count;
    }

    public void setVideo_count(String video_count) {
        this.video_count = video_count;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}
