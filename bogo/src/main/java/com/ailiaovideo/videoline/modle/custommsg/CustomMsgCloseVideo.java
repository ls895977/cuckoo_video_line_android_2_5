package com.ailiaovideo.videoline.modle.custommsg;

import com.ailiaovideo.videoline.LiveConstant;

public class CustomMsgCloseVideo extends CustomMsg {
    private String msg_content;

    //0关闭，1警告
    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public CustomMsgCloseVideo()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_CLOSE_VIDEO_LINE);
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }
}
