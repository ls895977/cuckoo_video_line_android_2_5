package com.ailiaovideo.videoline.msg.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.json.jsonmodle.AboutAndFans;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.BGLevelTextView;
import com.ailiaovideo.videoline.widget.GradeShowLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注和粉丝页适配器适配器
 */

public class AboutFansAdapter extends BaseQuickAdapter<AboutAndFans,BaseViewHolder>{
    List<AboutAndFans> aboutAndFans = new ArrayList<>();
    int type = 0;//状态,0默认关注,1粉丝
    private Context context;

    public AboutFansAdapter(Context context,int type, @Nullable List<AboutAndFans> data) {
        super(R.layout.adapter_aboutandfanse, data);
        this.aboutAndFans = data;
        this.type = type;
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, AboutAndFans item) {

        if (ApiUtils.isTrueUrl(item.getAvatar())){
            Utils.loadHttpImg(Utils.getCompleteImgUrl(item.getAvatar()),(ImageView) helper.getView(R.id.aboutandans_img));
        }

        helper.setVisible(R.id.aboutandfans_loveme,true);
        if(StringUtils.toInt(item.getFocus()) == 1){
            helper.setText(R.id.aboutandfans_loveme,"取消关注");
        }else{
            helper.setText(R.id.aboutandfans_loveme,"+ 关注");
        }
        //((ViewGroup)helper.getView(R.id.aboutandans_nowgrade)).addView(new GradeShowLayout(context,item.getLevel(), StringUtils.toInt(item.getSex())));
        BGLevelTextView tv_level =  helper.getView(R.id.tv_level);
        tv_level.setLevelInfo(StringUtils.toInt(item.getSex()),item.getLevel());

        helper.addOnClickListener(R.id.aboutandans_img);
        helper.addOnClickListener(R.id.aboutandfans_loveme);
        helper.setText(R.id.aboutandans_text,item.getUser_nickname());
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return aboutAndFans.size();
    }
}
