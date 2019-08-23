package com.ailiaovideo.videoline.modle;

public class CuckooSubscribeModel {


    /**
     * id : 1
     * user_id : 100164
     * to_user_id : 100163
     * create_time : 1540735946
     * coin : 5
     * status : 0
     * avatar : http://p4ulgsz1p.bkt.clouddn.com/865ce201809192259104937.jpg
     * user_nickname : 女生
     */

    private String id;
    private String user_id;
    private String to_user_id;
    private String create_time;
    private String coin;
    private String status;
    private String avatar;
    private String user_nickname;
    private String status_msg;
    private String is_online;

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
