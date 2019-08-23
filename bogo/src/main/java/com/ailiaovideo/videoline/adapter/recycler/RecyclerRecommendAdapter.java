package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.widget.BGLevelTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * RecyclerView-recommendPage适配器
 * Created by wp on 2017/12/28 0028.
 */
public class RecyclerRecommendAdapter extends BaseQuickAdapter<TargetUserData, BaseViewHolder> {
    private Context context;
    private int itemWidth;
    private int itemHeight;
    private int dp1;
    private int margin;
    //private final int margin;

    //构造方法,用于传入数据参数
    public RecyclerRecommendAdapter(Context context, @Nullable List<TargetUserData> data) {
        super(R.layout.adapter_user, data);
        this.context = context;

        dp1 = ConvertUtils.dp2px(1);
        margin = dp1 * 4;
        itemWidth = ScreenUtils.getScreenWidth() / 2 - dp1 * 8;
//        itemHeight = itemWidth + itemWidth / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, TargetUserData item) {

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);
        helper.getConvertView().setLayoutParams(params);
        //初始化数据显示
        if (ApiUtils.isTrueUrl(item.getAvatar())) {
            Utils.loadHttpImg(context, Utils.getCompleteImgUrl(item.getAvatar()), (ImageView) helper.getView(R.id.pagemsg_background));
        }
        helper.setImageResource(R.id.pagemsg_view_dian, SelectResHelper.getOnLineRes(StringUtils.toInt(item.getIs_online())));
        helper.setText(R.id.pagemsg_view_isonline, StringUtils.toInt(item.getIs_online()) == 1 ? "在线" : "离线");
        helper.setText(R.id.pagemsg_view_name, item.getUser_nickname());

        //TODO 需要改成按照认证区分，服务端接口需要配合修改
        if (StringUtils.toInt(item.getSex()) == 2) {
            helper.setText(R.id.pagemsg_view_nice, item.getCharging_coin() + ConfigModel.getInitData().getCurrency_name() + "/分钟");
        }

        helper.setGone(R.id.pagemsg_view_nice, StringUtils.toInt(item.getSex()) == 2);

        if (!TextUtils.isEmpty(item.getSign())) {
            helper.setText(R.id.pagemsg_view_sign, item.getSign());
        } else {
            helper.setText(R.id.pagemsg_view_sign, "暂未设置签名");
        }

        helper.setText(R.id.tv_time,item.getLast_online_time());
        ((BGLevelTextView) helper.getView(R.id.tv_level)).setLevelInfo(item.getSex(), item.getLevel());
    }

}