package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.UserModel;

/**
 * Created by 魏鹏 on 2018/3/3.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestDoPrivateChat extends JsonRequestBase {
    private UserModel user_info;
    private int is_pay;
    private String pay_coin;
    private int sex;
    private int is_auth;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public String getPay_coin() {
        return pay_coin;
    }

    public void setPay_coin(String pay_coin) {
        this.pay_coin = pay_coin;
    }

    public int getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(int is_pay) {
        this.is_pay = is_pay;
    }

    public UserModel getUser_info() {
        return user_info;
    }

    public void setUser_info(UserModel user_info) {
        this.user_info = user_info;
    }
}
