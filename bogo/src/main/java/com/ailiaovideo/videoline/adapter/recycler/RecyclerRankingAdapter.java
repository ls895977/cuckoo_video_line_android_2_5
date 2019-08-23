package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.modle.RankModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.GradeShowLayout;

import java.util.List;

/**
 * RecyclerView-new people page适配器
 */
public class RecyclerRankingAdapter extends BaseQuickAdapter<RankModel,BaseViewHolder> {

    private int type = 0;//1财气,2魅力
    private Context context;

    public RecyclerRankingAdapter(@Nullable List<RankModel> data, Context context, int type) {
        super(R.layout.adapter_newpeople_list,data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, RankModel charmData) {

        //数据绑定
        if (ApiUtils.isTrueUrl(charmData.getAvatar())){
            Utils.loadHttpImg(Utils.getCompleteImgUrl(charmData.getAvatar()), (ImageView) helper.getView(R.id.people_img));

        }

        helper.setBackgroundRes(R.id.newpeople_bar_isonLine,SelectResHelper.getOnLineRes(StringUtils.toInt(charmData.getIs_online())));
        helper.setText(R.id.newpeople_bar_title,charmData.getUser_nickname());
        helper.setVisible(R.id.people_img_masking, StringUtils.toInt(charmData.getUser_status()) == 1);
        helper.setText(R.id.text_number,charmData.getOrder_num());
        helper.setText(R.id.money_new_text,charmData.getTotal());
        helper.setText(R.id.newpeople_bar_location_text,charmData.getAddress());
        helper.setVisible(R.id.money_new_img,true);

        //((FrameLayout)helper.getView(R.id.newpeople_bar_nowgrade)).addView(new GradeShowLayout(helper.getView(R.id.newpeople_bar_nowgrade).getContext(),charmData.getLevel(),Integer.valueOf(charmData.getSex())));

        if (type == 1){
            //财气
            helper.setImageResource(R.id.money_new_img,R.drawable.chat_coins);
            helper.setTextColor(R.id.money_new_text,context.getResources().getColor(R.color.orange));
            helper.setImageResource(R.id.newpeople_bar_location,R.drawable.location_hint_male);

            helper.setBackgroundRes(R.id.tv_level,R.drawable.bg_org_num);
            helper.setText(R.id.tv_level,"V " + charmData.getLevel());
        }
        if (type == 2){
            //魅力
            helper.setImageResource(R.id.money_new_img,R.drawable.integral);
            helper.setTextColor(R.id.money_new_text,context.getResources().getColor(R.color.admin_color));
            helper.setImageResource(R.id.newpeople_bar_location,R.drawable.location_hint_female);

            helper.setBackgroundRes(R.id.tv_level,R.drawable.bg_main_color_num);
            helper.setText(R.id.tv_level,"M " + charmData.getLevel());
        }

    }
}