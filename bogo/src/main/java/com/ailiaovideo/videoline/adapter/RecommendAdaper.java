package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.json.RecommendBean;
import com.ailiaovideo.videoline.msg.adapter.MsgTypeListAdapter;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendAdaper extends RecyclerView.Adapter {

    public Context context;
    public List<RecommendBean.RecommendChildBean> dataList;

    public RecommendAdaper(Context context, List<RecommendBean.RecommendChildBean> list) {
        this.context = context;
        dataList = new ArrayList<>();
        dataList.addAll(list);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recommend_adapter_item, parent, false);
        return new RecommendAdaper.RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RecommendAdaper.RecommendViewHolder recommendViewHolder = (RecommendAdaper.RecommendViewHolder) holder;

        RecommendBean.RecommendChildBean recommendChildBean = dataList.get(position);

        recommendViewHolder.nameTv.setText(recommendChildBean.getUser_nickname());

        if (recommendChildBean.isChecked()) {
            recommendViewHolder.isCommend.setChecked(true);
        } else {
            recommendViewHolder.isCommend.setChecked(false);
        }

        if (!TextUtils.isEmpty(recommendChildBean.getAvatar())) {
            Utils.loadHttpImg(context, Utils.getCompleteImgUrl(recommendChildBean.getAvatar()), (ImageView) recommendViewHolder.iconImgIv, 0);
        }

        recommendViewHolder.isCommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recommendViewHolder.isCommend.isChecked()){
                    dataList.get(position).setChecked(true);
                }else {
                    dataList.get(position).setChecked(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView iconImgIv;
        private final TextView nameTv;
        private final CheckBox isCommend;

        public RecommendViewHolder(View itemView) {
            super(itemView);

            iconImgIv = itemView.findViewById(R.id.recommend_item_img);
            nameTv = itemView.findViewById(R.id.recommend_item_name);
            isCommend = itemView.findViewById(R.id.recommend_item_ischeck);
        }
    }

    public List<RecommendBean.RecommendChildBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<RecommendBean.RecommendChildBean> dataList) {
        this.dataList = dataList;
    }
}
