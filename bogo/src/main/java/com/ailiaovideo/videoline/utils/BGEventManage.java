package com.ailiaovideo.videoline.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by weipeng on 2018/2/17.
 */

public class BGEventManage {

    public BGEventManage() {
    }

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

}
