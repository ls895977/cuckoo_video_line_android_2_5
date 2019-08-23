package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ailiaovideo.chat.fragment.ConversationFragment;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;

/**
 * @dw 私信
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * */

public class PrivateChatActivity extends BaseActivity {

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_private_chat;
    }

    @Override
    protected void initView() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, new ConversationFragment());

        // addToBackStack添加到回退栈,addToBackStack与ft.add(R.id.fragment, new
        // MyFragment())效果相当
        // ft.addToBackStack("test");

        ft.commit();
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }
}
