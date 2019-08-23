package com.ailiaovideo.videoline.json.jsonmodle;

/**
 * 关注和粉丝对象
 * Created by jiahengfei on 2018/2/2 0002.
 */

public class AboutAndFans{

    private String id;//id
    private String avatar;//头像地址
    private String user_nickname;//用户名
    private String sex;//年龄
    private String level;//用户级别
    private String focus;//是否关注1关注2未关注

    public AboutAndFans(String id, String avatar, String user_nickname, String sex, String level, String focus) {
        this.id = id;
        this.avatar = avatar;
        this.user_nickname = user_nickname;
        this.sex = sex;
        this.level = level;
        this.focus = focus;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public AboutAndFans() {
        super();
    }
}
