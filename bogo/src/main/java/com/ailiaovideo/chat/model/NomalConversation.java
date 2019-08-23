package com.ailiaovideo.chat.model;

import android.content.Context;

import com.ailiaovideo.chat.ui.ChatActivity;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.ui.common.Common;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {


    private TIMConversation conversation;
    private String avatar;
    private String name;



    //最后一条消息
    private Message lastMessage;


    public NomalConversation(TIMConversation conversation){
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
    }


    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public String getAvatar() {
        /*switch (type){
            case C2C:
                return R.drawable.head_other;
            case Group:
                return R.drawable.head_group;
        }*/
        return avatar;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        //ChatActivity.navToChat(context,identify,name,avatar,type);
        Common.startPrivatePage(context,identify);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary(){
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            TextMessage textMessage = new TextMessage(ext.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return MyApplication.getInstances().getString(R.string.conversation_draft) + textMessage.getSummary();
            }else{
                return lastMessage.getSummary();
            }
        }else{
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        //TODO 获取会话名称
        /*if (type == TIMConversationType.Group){
            name=GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        }else{
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            name=profile == null?identify:profile.getName();
        }*/
        //return name;

        return name == null ? identify : name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum(){
        if (conversation == null) return 0;
        TIMConversationExt ext = new TIMConversationExt(conversation);
        return ext.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage(){
        if (conversation != null){
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return ext.getDraft().getTimestamp();
            }else{
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType(){
        return conversation.getType();
    }
}
