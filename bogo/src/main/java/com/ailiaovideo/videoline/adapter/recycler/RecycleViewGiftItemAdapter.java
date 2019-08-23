package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.modle.GiftModel;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/6.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class RecycleViewGiftItemAdapter extends BaseQuickAdapter<GiftModel, BaseViewHolder> {

    private int itemWidth = 0;
    private int selected = -1;
    private Context context;
    private int isGiftLl;

    public RecycleViewGiftItemAdapter(Context context, @Nullable List<GiftModel> data, int isGiftDialog) {
        super(R.layout.item_gift, data);
        itemWidth = ScreenUtils.getScreenWidth() / 4;
        this.context = context;
        isGiftLl = isGiftDialog;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftModel item) {
        View view = helper.getConvertView();
        view.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
        Utils.loadHttpImg(context, Utils.getCompleteImgUrl(item.getImg()), (ImageView) helper.getView(R.id.iv_img));
        helper.setText(R.id.tv_name, item.getName());

        if (isGiftLl == 1) {
            helper.setVisible(R.id.tv_coin, true);
            helper.setVisible(R.id.tv_num, false);
            helper.setText(R.id.tv_coin, item.getCoin() + RequestConfig.getConfigObj().getCurrency());
        } else {

            helper.setVisible(R.id.tv_coin, false);
            helper.setVisible(R.id.tv_num, true);
            helper.setText(R.id.tv_num, "x" + item.getGiftnum());
        }


        helper.setVisible(R.id.iv_selected, helper.getPosition() == selected);

    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }
}
