package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.RechargeRuleModel;
import com.ailiaovideo.videoline.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/2.
 *
 * @author 山东布谷鸟网络科技有限公司著
 * @dw 充值规则
 */

public class RecycleViewRechargeRuleAdapter extends BaseQuickAdapter<RechargeRuleModel, BaseViewHolder> {

    private Context mContext;
    private String selectId;
    private int listSize;

    public RecycleViewRechargeRuleAdapter(Context context, @Nullable List<RechargeRuleModel> data) {
        super(R.layout.item_recharge_rule, data);
        mContext = context;
    }

    public void setDataListSize(int size) {
        listSize = size;
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeRuleModel item) {

//        helper.setText(R.id.item_tv_txt,item.getMoney() + "(" +  item.getFormatCoin() + ")");
        helper.setText(R.id.item_tv_txt, item.getFormatCoin());
        helper.setText(R.id.item_tv_rmb, "￥" + item.getMoney());

        if (StringUtils.toInt(item.getGive()) != 0) {
            helper.setText(R.id.item_tv_give, "赠送：" + item.getGive());
            helper.setGone(R.id.item_tv_give, true);
        } else {

            helper.setGone(R.id.item_tv_give, false);
        }

        int adapterPosition = helper.getAdapterPosition();
        if (adapterPosition == listSize - 1) {
            helper.setVisible(R.id.item_tv_line, false);
        } else {
            helper.setVisible(R.id.item_tv_line, true);
        }

        if (StringUtils.toInt(selectId) == StringUtils.toInt(item.getId())) {
            helper.setTextColor(R.id.item_tv_txt, mContext.getResources().getColor(R.color.admin_color));
            helper.setBackgroundColor(R.id.item_tv_line, mContext.getResources().getColor(R.color.light_gray_no));
            helper.setTextColor(R.id.item_tv_rmb, mContext.getResources().getColor(R.color.admin_color));
        } else {
            helper.setTextColor(R.id.item_tv_txt, mContext.getResources().getColor(R.color.black));
            helper.setBackgroundColor(R.id.item_tv_line, mContext.getResources().getColor(R.color.light_gray_no));
            helper.setTextColor(R.id.item_tv_rmb, mContext.getResources().getColor(R.color.color_69));
        }
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
        notifyDataSetChanged();
    }


}
