package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.ailiaovideo.videoline.R;

public class BGLevelTextView extends TextView{


    public BGLevelTextView(Context context) {
        this(context,null);
    }

    public BGLevelTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BGLevelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BGLevelTextView, 0, 0);
        init(context);
    }

    private void init(Context context) {

        int dp = ConvertUtils.dp2px(1);
        setPadding(dp * 3,dp,dp * 3,dp);
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
    }

    public void setLevelInfo(int sex,String level){
        if(sex == 1){
            setBackgroundResource(R.drawable.bg_org_num);
            setText("V " + level);
        }else{
            setBackgroundResource(R.drawable.bg_main_color_num);
            setText("M " + level);
        }


    }
}
