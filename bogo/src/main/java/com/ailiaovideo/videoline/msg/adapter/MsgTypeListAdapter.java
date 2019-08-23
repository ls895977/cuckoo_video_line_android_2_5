package com.ailiaovideo.videoline.msg.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;

import java.util.List;

/**
 * add-List列表适配器
 * Created by jiahengfei on 2018/1/15 0015.
 */

public class MsgTypeListAdapter extends RecyclerView.Adapter<MsgTypeListAdapter.ViewHolder> implements View.OnClickListener {
    private List<Integer> imgList = null;
    private List<String> strList = null;

    //构造方法,用于传入数据参数
    public MsgTypeListAdapter(List<Integer> imgList,List<String> strList) {
        this.imgList = imgList;
        this.strList = strList;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public MsgTypeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_msgfunction_list,viewGroup,false);
        return new MsgTypeListAdapter.ViewHolder(view);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        ImageView msgfunctionImg;
        TextView msgfunctionText;

        public ViewHolder(View view){
            super(view);
            itemLayout = view.findViewById(R.id.item_layout);
            msgfunctionImg = view.findViewById(R.id.msgfunction_img);
            msgfunctionText = view.findViewById(R.id.msgfunction_text);
        }
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(MsgTypeListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.itemLayout.setOnClickListener(this);
        viewHolder.itemLayout.setTag(position);
        viewHolder.msgfunctionImg.setBackgroundResource(imgList.get(position));
        viewHolder.msgfunctionText.setText(strList.get(position));
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return strList.size();
    }

    ////////////////////////////////////////////业务逻辑处理//////////////////////////////////////////


    ////////////////////////////以下为item点击处理///////////////////////////////
    private AdapterOnItemClick mOnItemClickListener = null;

    //提供给子类,使子类可以设置点击监听事件
    public void setOnItemClickListener(AdapterOnItemClick listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        //注意这里使用getTag方法获取数据
        int position = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                default:
                    mOnItemClickListener.onItemClick(v,null,position);
                    break;
            }
        }
    }
}
