package com.ailiaovideo.videoline.helper;

/**
 * 常量类
 * Created by fly on 2017/12/25 0025.
 */

public class ContentUtils {

    /**
     * 常量字符串
     */
    public static class Str{
        //人民币
        public static final String rmb = "元";

        public static final String imgPath = "demonImg";
    }

    //Intent常用传参key值
    public static class INTENT{
        //player的id
        public static final String KEY_PLAYER_ID = "playerId";
        //状态传递值
        public static final String KEY_STATUS = "statusKey";

        //默认int参数值
        public static final int DEFLAULT_VALUE = 0;
    }

    //腾讯静态常量
    public static class TxContent{
        //account type 由腾讯分配
        public static int ACCOUNT_TYPE = 22738;
        //sdk appid 由腾讯分配
        public static int SDK_APPID = 0;
    }

}
