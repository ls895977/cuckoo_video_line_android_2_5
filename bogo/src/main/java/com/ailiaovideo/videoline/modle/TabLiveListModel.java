package com.ailiaovideo.videoline.modle;

public class TabLiveListModel {
    /**
     * pull_url : rtmp://pull.m396n.cn/5showcam/100115_1519125558
     * id : 100115
     * user_nickname : 阿狸
     * avatar : http://p2ftrp6p5.bkt.clouddn.com/04ab920180208202056403.jpg
     */

    private String pull_url;
    private String id;
    private String user_nickname;
    private String avatar;
    private String sex;
    private String is_follow;

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPull_url() {
            return pull_url;
        }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}