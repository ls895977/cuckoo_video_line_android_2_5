package com.ailiaovideo.videoline.adapter.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ailiaovideo.videoline.R;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/7.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class RecyclerGiftCount extends BaseQuickAdapter<String,BaseViewHolder> {

    private int selected = -1;
    private Context mContext;

    public RecyclerGiftCount(Context context,@Nullable List<String> data) {
        super(R.layout.item_gift_count,data);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        if(selected == helper.getPosition()){
            helper.setBackgroundRes(R.id.item_tv_count,R.drawable.bg_gift_count_selected);
        }else{
            helper.setBackgroundRes(R.id.item_tv_count,R.drawable.bg_gift_count);
        }
        helper.setText(R.id.item_tv_count,item);
        helper.setOnClickListener(R.id.item_tv_count, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(helper.getPosition());
            }
        });
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public int getSelected() {
        return selected;
    }
}
