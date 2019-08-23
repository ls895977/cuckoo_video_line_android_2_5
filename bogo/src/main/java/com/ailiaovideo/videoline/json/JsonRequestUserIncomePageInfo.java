package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.CashBean;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/13.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestUserIncomePageInfo extends JsonRequestBase {


    /**
     * income : 0
     * list : [{"id":1,"user_id":100114,"income":74328,"status":0,"create_time":1520944680}]
     */

    private String income;
    private String money;
    private List<CashBean> list;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public List<CashBean> getList() {
        return list;
    }

    public void setList(List<CashBean> list) {
        this.list = list;
    }


}
