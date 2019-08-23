package com.ailiaovideo.videoline.json;

/**
 * Created by 魏鹏 on 2018/3/3.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestDoGetWealthPage extends JsonRequestBase {
    private String coin;
    private String split;
    private String income;

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }
}
