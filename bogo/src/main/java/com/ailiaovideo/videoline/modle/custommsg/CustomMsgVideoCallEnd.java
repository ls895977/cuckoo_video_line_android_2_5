package com.ailiaovideo.videoline.modle.custommsg;


import com.ailiaovideo.videoline.LiveConstant;

public class CustomMsgVideoCallEnd extends CustomMsg {

    public CustomMsgVideoCallEnd() {
        super();
        setType(LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL_END);
    }

}
