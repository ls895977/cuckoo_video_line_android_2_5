package com.ailiaovideo.videoline.modle.custommsg;


import com.ailiaovideo.videoline.LiveConstant;

public class CustomMsgVideoCallReply extends CustomMsg
{
    private String channel;//视频通道
    private String reply_type;//回复状态 1接受 2拒绝

    public CustomMsgVideoCallReply()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL_REPLY);
    }

    public String getReply_type() {
        return reply_type;
    }

    public void setReply_type(String reply_type) {
        this.reply_type = reply_type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
