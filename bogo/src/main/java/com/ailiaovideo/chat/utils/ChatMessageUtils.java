package com.ailiaovideo.chat.utils;

import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;

/**
 * Created by 魏鹏 on 2018/3/9.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class ChatMessageUtils {

    private UserModel userModel;

    public ChatMessageUtils() {

        userModel = SaveData.getInstance().getUserInfo();

    }


}
