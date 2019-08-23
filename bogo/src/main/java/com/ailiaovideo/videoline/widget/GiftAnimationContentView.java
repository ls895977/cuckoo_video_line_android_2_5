package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.modle.GiftAnimationModel;
import com.ailiaovideo.videoline.utils.BGTimedTaskManage;
import com.ailiaovideo.videoline.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GiftAnimationContentView extends LinearLayout implements BGTimedTaskManage.BGTimeTaskRunnable {

    private List<GiftAnimationModel> list = new ArrayList<>();
    private BGTimedTaskManage bgTimedTaskManage;
    //当前显示动画数量
    private int nowGiftCount = 0;

    public GiftAnimationContentView(Context context) {
        super(context);
        init();
    }

    public GiftAnimationContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftAnimationContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        bgTimedTaskManage = new BGTimedTaskManage();
        setOrientation(VERTICAL);

    }

    public void addGift(GiftAnimationModel gift){
        list.add(gift);

    }

    //显示礼物动画
    private void showGiftAnimation(GiftAnimationModel gift){
        nowGiftCount ++;

        final View view = View.inflate(getContext(), R.layout.item_gift_animation,null);
        TextView tvName = view.findViewById(R.id.tv_user_nickname);
        tvName.setText(gift.getUserNickname());
        TextView tvText = view.findViewById(R.id.tv_text);
        tvText.setText(gift.getMsg());
        CircleImageView ivAvatar = view.findViewById(R.id.iv_avatar);
        CircleImageView ivGiftIcon = view.findViewById(R.id.iv_gift);
        Utils.loadHttpImg(getContext(),Utils.getCompleteImgUrl(gift.getUserAvatar()),ivAvatar);
        Utils.loadHttpImg(getContext(),Utils.getCompleteImgUrl(gift.getGiftIcon()),ivGiftIcon);

        view.setLayoutParams(new LinearLayout.LayoutParams(ConvertUtils.dp2px(230),ConvertUtils.dp2px(50)));
        addView(view);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"translationX",-ConvertUtils.dp2px(230),0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(nowGiftCount != 0){
                    nowGiftCount --;
                }

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        removeView(view);
                    }
                },1000);

            }
        });
        animator.setDuration(1000).start();
        if(list.size() != 0){
            list.remove(0);
        }
    }

    public void startHandel(){
        bgTimedTaskManage.setTime(1000);
        bgTimedTaskManage.setTimeTaskRunnable(this);
        bgTimedTaskManage.startRunnable(true);

    }

    @Override
    public void onRunTask() {
        if(list.size() == 0 || nowGiftCount == 2){
            return;
        }
        showGiftAnimation(list.get(0));
    }

    public void stopHandel(){

        removeAllViews();
        if(bgTimedTaskManage != null){
            bgTimedTaskManage.stopRunnable();
        }
    }
}
