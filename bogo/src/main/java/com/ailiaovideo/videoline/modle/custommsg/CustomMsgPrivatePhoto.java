package com.ailiaovideo.videoline.modle.custommsg;

import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.json.JsonRequestDoPrivateSendGif;

/**
 * 私聊私照消息
 */
public class CustomMsgPrivatePhoto extends CustomMsg
{

    private String id;
    private String img;
    public CustomMsgPrivatePhoto()
    {
        super();
        setType(LiveConstant.CustomMsgType.MSG_PRIVATE_IMG);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
