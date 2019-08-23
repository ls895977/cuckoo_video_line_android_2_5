package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.PayMenuModel;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/28.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */
public class PayMenuAdapter extends BaseQuickAdapter<PayMenuModel,BaseViewHolder> {

    private int selectPos = -1;

    public PayMenuAdapter(@Nullable List<PayMenuModel> data) {
        super(R.layout.item_pay_menu,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayMenuModel item) {
        helper.setText(R.id.tv_name,item.getPay_name());
        Utils.loadHttpImg(item.getIcon(), (ImageView) helper.getView(R.id.iv_pay_icon));

        if(selectPos == helper.getPosition()){
            helper.setChecked(R.id.cb_check,true);
        }else{

            helper.setChecked(R.id.cb_check,false);
        }
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
        notifyDataSetChanged();
    }
}
