package com.ailiaovideo.videoline.inter;

import android.view.View;

/**
 * 适配器点击接口
 * Created by fly on 2017/12/29 0029.
 */

public interface AdapterOnItemClick {

    //定义枚举匹配类型
    enum ViewName{
        ROOT_LAYOUT,//标识点击的是根布局-默认即为根布局
        HEAD_PORTRAIT,//新人页头像
        RETRY,//消息重发按钮
        Close,//关闭按钮
        lastItem,//最后一个
        loveBtn,//关注按钮
        Masking//蒙版
    }

    //适配器点击方法
    void onItemClick(View view, AdapterOnItemClick.ViewName viewName, int position);
}
