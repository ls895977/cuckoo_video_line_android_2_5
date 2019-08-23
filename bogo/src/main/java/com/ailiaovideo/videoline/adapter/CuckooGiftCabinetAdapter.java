package com.ailiaovideo.videoline.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.CuckooGiftCabinetModel;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CuckooGiftCabinetAdapter extends BaseQuickAdapter<CuckooGiftCabinetModel, BaseViewHolder> {
    public CuckooGiftCabinetAdapter(@Nullable List<CuckooGiftCabinetModel> data) {
        super(R.layout.item_gift_list_cabinet,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CuckooGiftCabinetModel item) {

        Utils.loadHttpImg(item.getImg(), (ImageView) helper.getView(R.id.item_iv_gift_icon));
        helper.setText(R.id.item_tv_name, item.getName());
        helper.setText(R.id.item_tv_count, "X" + item.getGift_count());
    }
}
