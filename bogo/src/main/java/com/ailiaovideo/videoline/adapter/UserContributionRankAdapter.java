package com.ailiaovideo.videoline.adapter;

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
 * Created by 魏鹏 on 2018/3/19.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class UserContributionRankAdapter extends BaseQuickAdapter<RankModel,BaseViewHolder> {

    private Context context;

    public UserContributionRankAdapter(Context context,@Nullable List<RankModel> data) {
        super(R.layout.adapter_newpeople_list,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RankModel charmData) {

        //数据绑定
        if (ApiUtils.isTrueUrl(charmData.getAvatar())){
            Utils.loadHttpImg(context,Utils.getCompleteImgUrl(charmData.getAvatar()), (ImageView) helper.getView(R.id.people_img));

        }

        helper.setBackgroundRes(R.id.newpeople_bar_isonLine, SelectResHelper.getOnLineRes(StringUtils.toInt(charmData.getIs_online())));
        helper.setText(R.id.newpeople_bar_title,charmData.getUser_nickname());
        helper.setVisible(R.id.people_img_masking, StringUtils.toInt(charmData.getUser_status()) == 1);
        helper.setText(R.id.text_number,charmData.getSum());
        helper.setText(R.id.money_new_text,charmData.getTotal());
        helper.setText(R.id.newpeople_bar_location_text,charmData.getAddress());
        helper.setVisible(R.id.money_new_img,true);

        helper.setBackgroundRes(R.id.tv_level,R.drawable.bg_org_num);
        helper.setText(R.id.tv_level,"V " + charmData.getLevel());

        //((FrameLayout)helper.getView(R.id.newpeople_bar_nowgrade)).addView(new GradeShowLayout(helper.getView(R.id.newpeople_bar_nowgrade).getContext(),charmData.getLevel(),Integer.valueOf(charmData.getSex())));
        //财气
        helper.setImageResource(R.id.money_new_img,R.drawable.chat_coins);
        helper.setTextColor(R.id.money_new_text,context.getResources().getColor(R.color.orange));
        helper.setImageResource(R.id.newpeople_bar_location,R.drawable.location_hint_male);

    }
}
