package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.json.jsonmodle.NewPeople;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.GradeShowLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ailiaovideo.videoline.inter.AdapterOnItemClick.ViewName.HEAD_PORTRAIT;

/**
 * RecyclerView-new people page适配器
 */
public class RecyclerNewPeopleAdapter extends BaseQuickAdapter<NewPeople,BaseViewHolder> {


    public RecyclerNewPeopleAdapter(@Nullable List<NewPeople> data) {
        super(R.layout.adapter_newpeople_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewPeople newPeople) {

        //数据绑定
        if (ApiUtils.isTrueUrl(newPeople.getAvatar())){
            Utils.loadHttpImg(MyApplication.getInstances(),Utils.getCompleteImgUrl(newPeople.getAvatar()), (ImageView) helper.getView(R.id.people_img));
        }

        helper.setBackgroundRes(R.id.newpeople_bar_isonLine,SelectResHelper.getOnLineRes(StringUtils.toInt(newPeople.getIs_online())));
        helper.setText(R.id.newpeople_bar_title,newPeople.getUser_nickname());
        helper.setText(R.id.newpeople_bar_location_text,newPeople.getAddress());

        if(StringUtils.toInt(newPeople.getSex()) == 1){
            helper.setBackgroundRes(R.id.tv_level,R.drawable.bg_org_num);
            helper.setText(R.id.tv_level,"V " + newPeople.getLevel());
        }else{
            helper.setBackgroundRes(R.id.tv_level,R.drawable.bg_main_color_num);
            helper.setText(R.id.tv_level,"M " + newPeople.getLevel());
        }
    }


}