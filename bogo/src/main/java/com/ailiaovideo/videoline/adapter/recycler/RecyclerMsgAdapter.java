package com.ailiaovideo.videoline.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * RecyclerView-msgPage适配器
 * Created by fly on 2017/12/28 0028.
 */
public class RecyclerMsgAdapter extends RecyclerView.Adapter<RecyclerMsgAdapter.ViewHolder> implements View.OnClickListener {
    public List<String> textList = null;
    public List<Integer> numberList = null;
    public List<Integer> drawableList = null;

    //构造方法,用于传入数据参数
    public RecyclerMsgAdapter(List<String> textList, List<Integer> numberList, List<Integer> drawableList) {
        if (textList != null)
            this.textList = textList;
        if (numberList != null)
            this.numberList = numberList;
        if (drawableList != null)
            this.drawableList = drawableList;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_msg_list,viewGroup,false);
        return new ViewHolder(view);
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout adapterMsgList;//整体布局
        TextView textView;//文字标识
        CircleImageView msgListIcon;//图标
        TextView msgListItem;//消息角标

        public ViewHolder(View view){
            super(view);
            adapterMsgList = view.findViewById(R.id.adapter_msg_list);
            textView = view.findViewById(R.id.msg_list_title);
            msgListIcon = view.findViewById(R.id.msg_list_icon);
            msgListItem = view.findViewById(R.id.msg_list_item);
        }
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //初始化数据显示
        viewHolder.textView.setText(textList.get(position));
        if (numberList.get(position) == 0)
            viewHolder.msgListItem.setVisibility(View.GONE);
        else{
            viewHolder.msgListItem.setVisibility(View.VISIBLE);
            viewHolder.msgListItem.setText(numberList.get(position)+"");
        }
        viewHolder.msgListIcon.setImageResource(drawableList.get(position));
        //初始化点击标识
        viewHolder.adapterMsgList.setTag(position);
        //初始化点击事件监听
        viewHolder.adapterMsgList.setOnClickListener(this);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return textList.size();
    }

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
            mOnItemClickListener.onItemClick(v, null, position);
        }
    }
}