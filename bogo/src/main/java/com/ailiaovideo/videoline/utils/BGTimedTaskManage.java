package com.ailiaovideo.videoline.utils;

import android.os.Handler;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * 定时任务
 * Created by weipeng on 2018/2/18.
 */

public class BGTimedTaskManage {

    private Handler handler;
    private long time = 1000;//定时执行时间单位s
    private boolean isPause = false;//是否暂停执行
    private BGTimeTaskRunnable timeTaskRunnable;

    public BGTimedTaskManage() {
        this.handler = new Handler();
    }


    public void setTimeTaskRunnable(BGTimeTaskRunnable timeTaskRunnable) {
        this.timeTaskRunnable = timeTaskRunnable;
    }

    public void startRunnable(@NotNull BGTimeTaskRunnable timeTaskRunnable, boolean isNowStart){

        isPause = false;
        if(this.timeTaskRunnable == null){
            this.timeTaskRunnable = timeTaskRunnable;

        }

        if(isNowStart){
            runnable.run();
        }else{
            handler.postDelayed(runnable,time);
        }
    }

    public void startRunnable(boolean isNowStart){

        if(isNowStart){
            runnable.run();
        }else{
            handler.postDelayed(runnable,time);
        }
    }

    public void stopRunnable(){
        handler.removeCallbacks(runnable);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!isPause && timeTaskRunnable != null){

                timeTaskRunnable.onRunTask();
            }
            handler.postDelayed(runnable,time);
        }
    };

    public interface BGTimeTaskRunnable{

        void onRunTask();
    }
}
