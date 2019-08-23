package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/2.
 * @author 山东布谷鸟网络科技有限公司著
 * @dw 短视频
 */

public class RecycleViewShortVideoAdapter extends BaseQuickAdapter<VideoModel,BaseViewHolder> {

    private Context mContext;

    public RecycleViewShortVideoAdapter(Context context,@Nullable List<VideoModel> data) {
        super(R.layout.adapter_video_list,data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoModel item) {

        //图片背景和标题
        Utils.loadHttpImg(mContext,Utils.getCompleteImgUrl(item.getImg()), (ImageView) helper.getView(R.id.adapter_video_image));
        helper.setText(R.id.adapter_video_title,item.getTitle());
        //数据显示
        helper.setText(R.id.left_start_number,item.getViewed());
        helper.setText(R.id.left_love_number,item.getShare());
        //是否付费
        helper.setVisible(R.id.videolist_masking,item.getStatus().equals("2"));
        if(StringUtils.toInt(item.getType()) == 0){
            helper.setText(R.id.tv_status,"审核中");
        }else if(StringUtils.toInt(item.getType()) == 2){
            helper.setText(R.id.tv_status,"审核不通过");
        }

        if(item.getStatus().equals("2")){
            Utils.loadHttpImgBlue(MyApplication.getInstances(),item.getImg(), (ImageView) helper.getView(R.id.adapter_video_image),0);
        }else{
            Utils.loadHttpImg(MyApplication.getInstances(),Utils.getCompleteImgUrl(item.getImg()), (ImageView) helper.getView(R.id.adapter_video_image));
        }

        helper.setVisible(R.id.rl_status,!(StringUtils.toInt(item.getType()) == 1));
    }
}
