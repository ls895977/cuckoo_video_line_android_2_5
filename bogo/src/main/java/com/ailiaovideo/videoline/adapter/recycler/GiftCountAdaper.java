package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GiftCountAdaper extends RecyclerView.Adapter {

    private List<String> mGiftCountList = new ArrayList<>();
    private final LayoutInflater mInflater;
    private Context context;

    public GiftCountAdaper(Context context, List<String> list) {
        mGiftCountList.addAll(list);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gift_count_item,
                parent, false);

        BannerViewHolder viewHolder = new BannerViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;

        bannerViewHolder.giftCountTv.setText("x"+mGiftCountList.get(position) + "");

        bannerViewHolder.giftCountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemListener.onItemClickListener(position);
            }
        });

        if (position == mGiftCountList.size()-1) {
            bannerViewHolder.giftCountLine.setVisibility(View.GONE);
        } else {
            bannerViewHolder.giftCountLine.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return mGiftCountList.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private final TextView giftCountTv;
        private final View giftCountLine;

        public BannerViewHolder(View itemView) {
            super(itemView);

            giftCountTv = itemView.findViewById(R.id.gift_count_pop_item_tv);
            giftCountLine = itemView.findViewById(R.id.gift_count_pop_item_view);
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
