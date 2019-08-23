package com.ailiaovideo.videoline.json.jsonmodle;

import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.modle.RechargeRuleModel;
import com.ailiaovideo.videoline.utils.StringUtils;

import java.util.List;

/**
 * Created by weipeng on 2018/2/10.
 */

public class UserCenterData {

    private int sex;
    private String user_nickname;
    private String avatar;
    private String coin;//余额
    private String user_status;//状态
    private String level;//等级
    private String split;//分成比例
    private String attention_fans;//
    private String attention_all;//
    private String user_auth_status;//认证状态
    private String is_open_do_not_disturb;

    public String getIs_open_do_not_disturb() {
        return is_open_do_not_disturb;
    }

    public void setIs_open_do_not_disturb(String is_open_do_not_disturb) {
        this.is_open_do_not_disturb = is_open_do_not_disturb;
    }

    public boolean isOpenDoNotDisturb(){
        return StringUtils.toInt(is_open_do_not_disturb) == 1;
    }

    private List<UserCenterData.TeacherBean> teacher;//师傅
    private List<RechargeRuleModel> pay_coin;//充值列表个人中心

    public String getUser_auth_status() {
        return user_auth_status;
    }

    public void setUser_auth_status(String user_auth_status) {
        this.user_auth_status = user_auth_status;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getAttention_fans() {
        return attention_fans;
    }

    public void setAttention_fans(String attention_fans) {
        this.attention_fans = attention_fans;
    }

    public String getAttention_all() {
        return attention_all;
    }

    public void setAttention_all(String attention_all) {
        this.attention_all = attention_all;
    }

    public List<TeacherBean> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<TeacherBean> teacher) {
        this.teacher = teacher;
    }

    public List<RechargeRuleModel> getPay_coin() {
        return pay_coin;
    }

    public void setPay_coin(List<RechargeRuleModel> pay_coin) {
        this.pay_coin = pay_coin;
    }

    public static class TeacherBean {
        /**
         * sum : 30410
         * avatar : /api/public/upload/image/20180126/8a84983780b8b67eaef2674f44ef1458.jpg
         */

        private int sum;
        private String avatar;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class PayCoinBean {
        /**
         * id : 129
         * money : 100.00
         * coin : 10000
         */

        private String id;
        private String money;
        private String coin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCoin() {
            return coin + RequestConfig.getConfigObj().getCurrency();
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }
    }
}
