package com.ailiaovideo.videoline.msg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.msg.modle.ChatData;
import com.ailiaovideo.videoline.msg.modle.Msg;
import com.ailiaovideo.videoline.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 聊天列表页适配器
 * Created by jiahengfei on 2018/2/2 0001.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> implements View.OnClickListener {

    private List<ChatData> msgs = new ArrayList<>();//msgModle列表
    private Context context;
    //构造方法,用于传入数据参数
    public ChatListAdapter(Context context,List<ChatData> msgs) {
        this.msgs = msgs;
        this.context = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_chat_chatlist,viewGroup,false);
        return new ChatListAdapter.ViewHolder(view);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headImg;
        TextView textTitle,textMsg,textDate,textNumber;
        RelativeLayout layout;

        public ViewHolder(View view){
            super(view);
            headImg = view.findViewById(R.id.head_img);
            textTitle = view.findViewById(R.id.text_title);
            textMsg = view.findViewById(R.id.text_msg);
            textDate = view.findViewById(R.id.text_date);
            textNumber = view.findViewById(R.id.text_number);
            layout = view.findViewById(R.id.layout);
        }
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ChatListAdapter.ViewHolder viewHolder, int position) {
        ChatData chatData = msgs.get(position);
        Msg msg = chatData.getLastMsg();
        Utils.loadHttpImg(context,Utils.getCompleteImgUrl(chatData.getBitmap()),viewHolder.headImg);

        viewHolder.textDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(msg.getDate()));

        int a = msg.getType();
        if (a == 0){
            viewHolder.textMsg.setText(msg.getMsg());
        }else if (a == 1){
            viewHolder.textMsg.setText("[图片]");
        }else if (a == 2){
            viewHolder.textMsg.setText("[语音消息]");
        }else if (a == 2){
            viewHolder.textMsg.setText("[视频消息]");
        }else if (a == 4){
            viewHolder.textMsg.setText("[视频通话]");
        }
        if (chatData.getPosition() != 0){
            viewHolder.textNumber.setVisibility(View.VISIBLE);
            viewHolder.textNumber.setText(String.valueOf(chatData.getPosition()));
        }
        viewHolder.textTitle.setText(chatData.getName());
        viewHolder.layout.setTag(position);
        viewHolder.layout.setOnClickListener(this);
        viewHolder.headImg.setTag(position);
        viewHolder.headImg.setOnClickListener(this);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return msgs.size();
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
                case R.id.head_img:
                    //点击头像按钮
                    mOnItemClickListener.onItemClick(v, AdapterOnItemClick.ViewName.HEAD_PORTRAIT,position);
                    break;
                default:
                    //默认点击根布局
                    mOnItemClickListener.onItemClick(v,null,position);
                    break;
            }
        }
    }
}
