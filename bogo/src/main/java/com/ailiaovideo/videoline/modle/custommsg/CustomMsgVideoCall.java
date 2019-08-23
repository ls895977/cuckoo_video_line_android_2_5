package com.ailiaovideo.videoline.modle.custommsg;


import com.ailiaovideo.videoline.LiveConstant;

public class CustomMsgVideoCall extends CustomMsg
{
    private String channel;//视频通道
    private String is_free;
    private int call_type;

    public int getCall_type() {
        return call_type;
    }

    public void setCall_type(int call_type) {
        this.call_type = call_type;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }

    public CustomMsgVideoCall()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
