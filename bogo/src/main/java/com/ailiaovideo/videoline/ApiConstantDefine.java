package com.ailiaovideo.videoline;

/**
 * Created by weipeng on 2018/2/23.
 */

public class ApiConstantDefine {


    public static class ApiCode{
        public static final int REQUEST_SUCCESS = 1;
        public static final int LOGIN_INFO_ERROR = 10001;//登录信息错误
        public static final int BALANCE_NOT_ENOUGH = 10002;//余额不足
        public static final int VIDEO_CALL_RECORD_NOT_FOUNT = 10012;//视频通话记录不存在
        public static final int VIDEO_CALL_RECORD_NOT_BALANCE = 10013;//视频通话记录不存在
        public static final int VIDEO_NOT_PAY = 10020;//视频未付费
        public static final int PHOTO_NOT_PAY = 10031;//视频未付费

        public static final int VIDEO_USER_NOT_ONLINE = 10017;//不在线
        public static final int VIDEO_USER_BUSY = 10018;//忙碌
        public static final int VIDEO_DO_NOT_DISTURB = 10019;//勿擾

    }

}
