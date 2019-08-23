package com.ailiaovideo.videoline.modle;

import com.ailiaovideo.videoline.manage.RequestConfig;

/**
 * Created by 魏鹏 on 2018/3/2.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class RechargeRuleModel {

    private String id;
    private String name;
    private String coin;
    private String money;
    private String product_id;
    private String give;
    private String pay_pal_money;

    public String getPay_pal_money() {
        return pay_pal_money;
    }

    public void setPay_pal_money(String pay_pal_money) {
        this.pay_pal_money = pay_pal_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoin() {
        return coin;
    }

    public String getFormatCoin() {
        return coin + RequestConfig.getConfigObj().getCurrency();
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }
}
