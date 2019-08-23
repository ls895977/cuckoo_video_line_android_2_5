package com.ailiaovideo.videoline.msg.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.helper.ImageUtil;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.msg.modle.Msg;
import com.ailiaovideo.videoline.msg.modle.MsgModle;
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 聊天列表适配器
 * Created by jiahengfei on 2018/1/15 0015.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements View.OnClickListener {
    private List<MsgModle> msgs = null;//msgModle列表
    private Bitmap toUserBitmap;//对方头像
    private Bitmap fromUserBitmap;//自身头像

    //构造方法,用于传入数据参数
    public ChatAdapter(Bitmap toUserBitmap, Bitmap fromUserBitmap, List<MsgModle> msgs) {
        this.toUserBitmap = toUserBitmap;
        this.fromUserBitmap = fromUserBitmap;
        this.msgs = msgs;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_chat_list,viewGroup,false);
        return new ChatAdapter.ViewHolder(view);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout leftLayout,rightLayout;
        TextView leftTextMsg,rightTextMsg;
        CircleImageView leftImg,rightImg;
        ImageView leftImgMsg,rightImgMsg;
        QMUILinkTextView linkText;
        TextView rightSend;
        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftTextMsg = view.findViewById(R.id.left_text_msg);
            rightTextMsg = view.findViewById(R.id.right_text_msg);
            leftImg = view.findViewById(R.id.left_circle_img);
            rightImg = view.findViewById(R.id.right_circle_img);
            leftImgMsg = view.findViewById(R.id.left_img_msg);
            rightImgMsg = view.findViewById(R.id.right_img_msg);
            linkText = view.findViewById(R.id.link_tip_msg);
            rightSend = view.findViewById(R.id.right_donot_send);
        }
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder viewHolder, int position) {
        MsgModle.Type type = msgs.get(position).getType();
        switch (type){
            case Tip:
                showTipMsg(viewHolder,msgs.get(position).getMsg());
                break;
            case Left:
                showLeftMsg(viewHolder,position,msgs.get(position).getMsg());
                break;
            case Right:
                showRightMsg(viewHolder,position,msgs.get(position).getMsg());
                break;
        }
        viewHolder.rightSend.setTag(position);
        viewHolder.rightSend.setOnClickListener(this);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return msgs.size();
    }

    ////////////////////////////////////////////业务逻辑处理//////////////////////////////////////////

    /**
     * 根据position索引获取一个msgMoudle
     * @param position 当前索引
     * @return MsgMoudle
     */
    public MsgModle getModle(int position){
        return msgs.get(position);
    }

    /**
     * 显示左侧信息
     */
    private void showLeftMsg(ViewHolder viewHolder, int position, Msg msg){
        //显示控件
        viewHolder.leftLayout.setVisibility(View.VISIBLE);
        //Log.d(TAG, "showLeftMsg: "+msg);
        int msgType = msg.getType();
        switch (msgType){
            case 0://文本
                viewHolder.leftTextMsg.setVisibility(View.VISIBLE);
                viewHolder.leftTextMsg.setText(msg.getMsg());
                viewHolder.leftImg.setImageBitmap(toUserBitmap);
                break;
            case 1://图片
                viewHolder.leftImgMsg.setVisibility(View.VISIBLE);
                Bitmap now = ImageUtil.getBitMapByString(msg.getMsg());
//                if (ApiUtils.isTrueUrl(msg.getMsg())){
//                    ApiUtils.setUrlBitmap(msg.getMsg(),viewHolder.leftImgMsg);
//                }
                viewHolder.leftImgMsg.setImageBitmap(now);
                viewHolder.leftImg.setImageBitmap(toUserBitmap);
                break;
        }
    }

    /**
     * 显示右侧信息
     */
    private void showRightMsg(ViewHolder viewHolder, int position, Msg msg){
        //显示控件
        viewHolder.rightLayout.setVisibility(View.VISIBLE);
        //Log.d(TAG, "showRightMsg: "+msg);
        int msgType = msg.getType();
        switch (msgType){
            case 0://文本
                viewHolder.rightTextMsg.setVisibility(View.VISIBLE);
                viewHolder.rightTextMsg.setText(msg.getMsg());
                viewHolder.rightImg.setImageBitmap(fromUserBitmap);
                break;
            case 1://图片
                viewHolder.rightImgMsg.setVisibility(View.VISIBLE);
//                if (ApiUtils.isTrueUrl(msg.getMsg())){
//                    ApiUtils.setUrlBitmap(msg.getMsg(),viewHolder.rightImgMsg);
//                }
                Bitmap now = ImageUtil.getBitMapByString(msg.getMsg());
                viewHolder.rightImgMsg.setImageBitmap(now);
                viewHolder.rightImg.setImageBitmap(fromUserBitmap);
                break;
        }
        viewHolder.rightSend.setVisibility(msg.getI() == 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 显示提示信息
     */
    private void showTipMsg(ViewHolder viewHolder, Msg msg){
        //显示控件
        viewHolder.linkText.setVisibility(View.VISIBLE);
        viewHolder.linkText.setText(msg.getMsg());
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
            switch (v.getId()){
                case R.id.right_donot_send:
                    //点击重发按钮
                    mOnItemClickListener.onItemClick(v, AdapterOnItemClick.ViewName.RETRY,position);
                    break;
                default:
                    //默认点击根布局
                    mOnItemClickListener.onItemClick(v,null,position);
                    break;
            }
        }
    }
}
