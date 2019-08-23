package com.ailiaovideo.videoline.json;

/**
 * Created by weipeng on 2018/2/27.
 */

public class JsonRequestDoGetVideo extends JsonRequestBase {
    private String video_url;
    private String is_follow;
    private String follow_num;
    private String avatar;
    private String user_nickname;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(String follow_num) {
        this.follow_num = follow_num;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
