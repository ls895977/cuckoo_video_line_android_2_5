package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.qmuiteam.qmui.widget.QMUIViewPager;

/**
 * 默认屏蔽左右滑动的viewPage
 * 继承自QMUIViewPager,也可以直接继承自ViewPage
 * Created by jiahengfei on 2017/12/28 0028.
 */
public class NoScrollViewPager extends QMUIViewPager {

    private boolean noScroll = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
