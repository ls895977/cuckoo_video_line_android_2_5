package com.ailiaovideo.videoline.manage;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.tencent.imsdk.TIMGroupManager;

import java.util.Stack;

/**
 * Activity管理类
 * Created by jiahengfei on 2018/1/10 0010.
 */

public class AppManager {
    //Activity栈
    private static Stack<Activity> activityStack;
    //单例模式
    private static AppManager instance;
    //构造方法
    private AppManager() {}

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            //退出腾讯云大群
            //退出群组
            TIMGroupManager.getInstance().quitGroup(
                    RequestConfig.getConfigObj().getGroupId(),  //群组Id
                    null);      //回调(此处不适用回调)
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            assert activityMgr != null;
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
