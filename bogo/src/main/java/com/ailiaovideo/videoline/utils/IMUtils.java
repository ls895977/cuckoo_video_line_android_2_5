package com.ailiaovideo.videoline.utils;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import java.util.List;

public class IMUtils {

    public static int getIMUnReadMessageCount(){
        //获取会话扩展实例
        List<TIMConversation> list = TIMManagerExt.getInstance().getConversationList();

        int count = 0;
        for (TIMConversation conversation : list){
            TIMConversationExt conExt = new TIMConversationExt(conversation);
            if(conExt.getUnreadMessageNum() > 0 && conversation.getType() != TIMConversationType.System){
                count += conExt.getUnreadMessageNum();
            }
        }

        return count;
    }

    public static void doLoginIM() {

    }
}
