package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 可以和ScrollViewGrid兼容嵌套的GridView
 * Created by jiahengfei on 2018/1/9 0009.
 */
public class ForScrollViewGridView extends GridView{
    public ForScrollViewGridView(Context context) {
        super(context);
    }

    public ForScrollViewGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForScrollViewGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
