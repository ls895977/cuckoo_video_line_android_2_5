package com.ailiaovideo.videoline.json.jsonmodle;

/**
 * 新人
 * Created by jiahengfei on 2018/1/29 0029.
 */

public class NewPeople {
    private String id;//排行人id
    private String user_nickname;//排行人名称
    private String sex;//性别 0保密 1男2女
    private String is_online;//状态 0禁用 1正常 2未验证
    private String avatar;//用户头像
    private String address;//用户地址
    private String level;//用户级别

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {

        return id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public String getSex() {
        return sex;
    }


    public String getAvatar() {
        return avatar;
    }

    public String getAddress() {
        return address;
    }


    public NewPeople() {
        super();
    }

    public NewPeople(String id, String user_nickname, String sex, String is_online, String avatar, String address, String levelnamel) {
        this.id = id;
        this.user_nickname = user_nickname;
        this.sex = sex;
        this.is_online = is_online;
        this.avatar = avatar;
        this.address = address;
        this.level = levelnamel;
    }
}
