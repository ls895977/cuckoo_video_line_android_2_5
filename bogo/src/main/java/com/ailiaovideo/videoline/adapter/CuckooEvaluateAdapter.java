package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.CuckooEvaluateModel;
import com.ailiaovideo.videoline.modle.CuckooUserEvaluateListModel;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class CuckooEvaluateAdapter extends BaseQuickAdapter<CuckooUserEvaluateListModel,BaseViewHolder>{

    private Context context;

    public CuckooEvaluateAdapter(Context context,@Nullable List<CuckooUserEvaluateListModel> data) {
        super(R.layout.item_evaluate,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CuckooUserEvaluateListModel item) {

        helper.setText(R.id.item_tv_name,item.getUser_nickname());
        Utils.loadHttpImg(MyApplication.getInstances(),item.getAvatar(), (ImageView) helper.getView(R.id.item_iv_avatar));
        final TagFlowLayout tagFlowLayout = helper.getView(R.id.id_flow_layout);


        tagFlowLayout.setAdapter(new TagAdapter(item.getLabel_list()) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.view_evaluate_label,
                        tagFlowLayout, false);
                tv.setText(item.getLabel_list().get(position));
                tv.setTextSize(ConvertUtils.dp2px(4));
                tv.setTextColor(context.getResources().getColor(R.color.white));
                tv.setBackgroundResource(R.drawable.bg_evaluate_select);
                return tv;
            }
        });

    }
}
