package com.ailiaovideo.videoline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.ArrayList;

import static com.tencent.qalsdk.service.QalService.context;

public class BannerAdaper extends RecyclerView.Adapter {

    private ArrayList<String> rollImgList = new ArrayList<>();
    private final LayoutInflater mInflater;
    private Context context;

    public BannerAdaper(Context context, ArrayList<String> rollImg) {
        rollImgList.addAll(rollImg);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.banner_item,
                parent, false);

        BannerViewHolder viewHolder = new BannerViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
        if (ApiUtils.isTrueUrl(rollImgList.get(position))) {
            Utils.loadHttpImg(context, Utils.getCompleteImgUrl(rollImgList.get(position)), (ImageView) bannerViewHolder.imageIv);
        }

        bannerViewHolder.imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemListener.onItemClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return rollImgList.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageIv;

        public BannerViewHolder(View itemView) {
            super(itemView);

            imageIv = itemView.findViewById(R.id.id_index_gallery_item_image);
        }
    }

    private ClickItemListener clickItemListener;

    public void setClickItemListener(ClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    public interface ClickItemListener {
        void onItemClickListener(int posi);
    }
}
