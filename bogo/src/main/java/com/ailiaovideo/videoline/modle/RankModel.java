package com.ailiaovideo.videoline.modle;

/**
 * Created by 魏鹏 on 2018/3/5.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class RankModel {

    private String id;
    private String total;//获取的总金币
    private String user_nickname;//排行人名称
    private String sex;//性别 0保密 1男2女
    private String user_status;//状态 0禁用 1正常 2未验证,
    private String avatar;//用户头像
    private String address;//用户地址
    private String level;//用户级别
    private String sum;//用户排名数
    private String is_online;
    private String order_num;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public String getSex() {
        return sex;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAddress() {
        return address;
    }

    public String getSum() {
        return sum;
    }


    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

}
