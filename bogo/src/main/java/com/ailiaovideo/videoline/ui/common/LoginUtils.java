package com.ailiaovideo.videoline.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.helper.TxLogin;
import com.ailiaovideo.videoline.manage.AppManager;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.CuckooLoginSelectActivity;
import com.ailiaovideo.videoline.ui.MainActivity;
import com.ailiaovideo.videoline.ui.RegisterSelectActivity;
import com.ailiaovideo.videoline.utils.SharedPreferencesUtils;
import com.fm.openinstall.OpenInstall;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

/**
 * Created by 魏鹏 on 2018/3/26.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class LoginUtils {

    public static void doLogin(final Context context, final UserModel userData) {

        TxLogin.loginIm(
                userData.getId(),
                userData.getUser_sign(),
                new TIMCallBack() {

                    @Override
                    public void onError(int i, String s) {
                        //登录失败
                        LogUtils.i("腾讯云登录有误!");
                        ToastUtils.showLong(s);

                    }

                    @Override
                    public void onSuccess() {

                        TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
                        //开启离线推送
                        settings.setEnabled(true);
                        //设置收到 C2C 离线消息时的提示声音，这里把声音文件放到了 res/raw 文件夹下
                        settings.setC2cMsgRemindSound(Uri.parse("android.resource://" + MyApplication.getInstances().getPackageName() + "/" + R.raw.call));
                        //设置收到群离线消息时的提示声音，这里把声音文件放到了 res/raw 文件夹下
                        //settings.setGroupMsgRemindSound(Uri.parse("android.resource://" + MyApplication.getInstances().getPackageName() + "/" + R.raw.call));
                        //网易云登录
                        //通知SaveData类登录成功
                        SaveData.loginSuccess(userData);

                        //umeng
                        PushAgent mPushAgent = PushAgent.getInstance(context);
                        mPushAgent.addAlias(userData.getId(), "buguniao", new UTrack.ICallBack() {
                            @Override
                            public void onMessage(boolean isSuccess, String message) {
                                LogUtils.i("umeng推送添加别名：" + isSuccess + "---" + message);
                            }
                        });
                        OpenInstall.reportRegister();
                        //统计友盟注册用户数据
                        MobclickAgent.onProfileSignIn(userData.getId());

                        //跳转页面
                        ToastUtils.showLong(R.string.login_success);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }
                }
        );

    }

    //退出登录
    public static void doLoginOut(Context context) {

        TxLogin.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

                LogUtils.i("退出登录腾讯云失败,error:" + s);
            }

            @Override
            public void onSuccess() {
                LogUtils.i("退出登录腾讯云成功");
            }
        });

        //umeng
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.deleteAlias(SaveData.getInstance().getId(), "buguniao", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {

                LogUtils.i("umeng推送删除别名：" + isSuccess + "---" + message);
            }
        });

        //统计友盟退出用户数据
        MobclickAgent.onProfileSignOff();

        AppManager.getAppManager().finishAllActivity();
        //清除登录信息
        SaveData.getInstance().clearData();

        //清除sp信息
        SharedPreferencesUtils.clear(context);

        Intent intent = new Intent(context, CuckooLoginSelectActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
