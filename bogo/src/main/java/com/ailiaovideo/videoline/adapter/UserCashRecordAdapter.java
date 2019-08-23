package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.CashBean;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/13.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class UserCashRecordAdapter extends BaseQuickAdapter<CashBean,BaseViewHolder> {

    public UserCashRecordAdapter(@Nullable List<CashBean> data) {
        super(R.layout.item_user_cash_record,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashBean item) {

        helper.setText(R.id.item_tv_content,"提现数量:" + item.getIncome());
        helper.setText(R.id.item_tv_time,item.getCreate_time());
        helper.setText(R.id.item_tv_status,item.getStatus());

    }
}
