package com.ailiaovideo.videoline.modle.custommsg;


import com.blankj.utilcode.util.TimeUtils;
import com.ailiaovideo.videoline.ICustomMsg;
import com.ailiaovideo.videoline.LiveConstant;
import com.tencent.imcore.MsgStatus;
import com.tencent.imsdk.TIMMessage;

public abstract class MsgModel
{

    /**
     * 私聊消息类型
     */
    private int privateMsgType = LiveConstant.PrivateMsgType.MSG_TEXT_LEFT;

    //
    private int customMsgType = LiveConstant.CustomMsgType.MSG_NONE;
    private CustomMsg customMsg;

    /**
     * true-本地通过EventBus直接发送的消息
     */
    private boolean isLocalPost = false;
    /**
     * 是否自己发送的
     */
    private boolean isSelf = false;
    /**
     * 会话的对方id或者群组Id
     */
    private String conversationtPeer;
    /**
     * 消息在腾讯服务端生成的时间戳
     */
    private long timestamp;
    /**
     * 消息在服务端生成的时间格式化
     */
    private String timestampFormat;
    /**
     * 该条消息对应的会话的未读
     */
    private long unreadNum;

    /*
    * 是否进行本地eventbus发送
    * */
    private boolean isLocalEventPost;

    private MsgStatus status = MsgStatus.kSendFail;
    private ConversationType conversationType = ConversationType.Invalid;

    /**
     * 是否是直播间列表显示的消息
     *
     * @return
     */
    public boolean isLiveChatMsg()
    {
        return false;
    }

    /**
     * 是否是私聊消息
     *
     * @return
     */
    public boolean isPrivateMsg()
    {
        boolean result = false;
        switch (customMsgType)
        {
            case LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL:
            case LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL_END:
            case LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL_REPLY:
            case LiveConstant.CustomMsgType.MSG_PRIVATE_GIFT:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }


    /**
     * 重写，用于删除本地缓存的消息
     */
    public abstract void remove();



    public int getCustomMsgType()
    {
        return customMsgType;
    }

    public void setCustomMsgType(int customMsgType)
    {
        this.customMsgType = customMsgType;
    }

    public boolean isLocalPost()
    {
        return isLocalPost;
    }

    public void setLocalPost(boolean isLocalPost)
    {
        this.isLocalPost = isLocalPost;
    }

    public <T extends ICustomMsg> T getCustomMsgReal()
    {
        T t = null;
        try
        {
            return (T) getCustomMsg();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public CustomMsg getCustomMsg()
    {
        return customMsg;
    }

    public void setCustomMsg(CustomMsg customMsg)
    {
        this.customMsg = customMsg;
        if (customMsg != null)
        {
            int type = customMsg.getType();
            setCustomMsgType(type);
            switch (type)
            {

                case LiveConstant.CustomMsgType.MSG_PRIVATE_GIFT:
                    // 私聊消息类型
                    if (isSelf())
                    {
                        setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_GIFT_RIGHT);
                    } else
                    {
                        setPrivateMsgType(LiveConstant.PrivateMsgType.MSG_GIFT_LEFT);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isSelf()
    {
        return isSelf;
    }

    public void setSelf(boolean isSelf)
    {
        this.isSelf = isSelf;
    }

    public String getConversationPeer()
    {
        if (conversationtPeer == null)
        {
            conversationtPeer = "";
        }
        return conversationtPeer;
    }

    public void setConversationPeer(String conversationtPeer)
    {
        this.conversationtPeer = conversationtPeer;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;

        String format = TimeUtils.getFriendlyTimeSpanByNow(timestamp * 1000);
        setTimestampFormat(format);
    }

    public String getTimestampFormat()
    {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat)
    {
        this.timestampFormat = timestampFormat;
    }

    public long getUnreadNum()
    {
        return unreadNum;
    }

    public void setUnreadNum(long unreadNum)
    {
        this.unreadNum = unreadNum;
    }

    public MsgStatus getStatus()
    {
        return status;
    }

    public void setStatus(MsgStatus status)
    {
        this.status = status;
    }

    public void setConversationType(ConversationType conversationType)
    {
        this.conversationType = conversationType;
    }

    public ConversationType getConversationType()
    {
        return conversationType;
    }


    public int getPrivateMsgType()
    {
        return privateMsgType;
    }

    public void setPrivateMsgType(int privateMsgType)
    {
        this.privateMsgType = privateMsgType;
    }

    // 腾讯im相关方法

    /**
     * 解析腾讯的消息实体
     *
     * @param timMessage
     */
    public abstract void setTimMessage(TIMMessage timMessage);

}
