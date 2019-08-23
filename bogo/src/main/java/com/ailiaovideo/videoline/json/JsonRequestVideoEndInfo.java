package com.ailiaovideo.videoline.json;

/**
 * Created by weipeng on 2018/2/28.
 */

public class JsonRequestVideoEndInfo extends JsonRequestBase {

    private String total;
    private String gift_total_coin;
    private String video_call_total_coin;
    private String is_follow;
    private String user_coin;

    public String getUser_coin() {
        return user_coin;
    }

    public void setUser_coin(String user_coin) {
        this.user_coin = user_coin;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGift_total_coin() {
        return gift_total_coin;
    }

    public void setGift_total_coin(String gift_total_coin) {
        this.gift_total_coin = gift_total_coin;
    }

    public String getVideo_call_total_coin() {
        return video_call_total_coin;
    }

    public void setVideo_call_total_coin(String video_call_total_coin) {
        this.video_call_total_coin = video_call_total_coin;
    }
}
