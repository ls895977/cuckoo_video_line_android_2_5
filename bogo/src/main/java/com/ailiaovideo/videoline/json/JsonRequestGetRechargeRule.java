package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.PayMenuModel;
import com.ailiaovideo.videoline.modle.RechargeRuleModel;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/2.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestGetRechargeRule extends JsonRequestBase {
    private List<RechargeRuleModel> list;
    private List<PayMenuModel> pay_list;

    public List<PayMenuModel> getPay_list() {
        return pay_list;
    }

    public void setPay_list(List<PayMenuModel> pay_list) {
        this.pay_list = pay_list;
    }

    public List<RechargeRuleModel> getList() {
        return list;
    }

    public void setList(List<RechargeRuleModel> list) {
        this.list = list;
    }
}
