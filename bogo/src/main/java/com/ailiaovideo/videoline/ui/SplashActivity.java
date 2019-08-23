package com.ailiaovideo.videoline.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.event.CuckooOnLoginTimSuccessEvent;
import com.ailiaovideo.videoline.helper.TxLogin;
import com.ailiaovideo.videoline.inter.MsgDialogClick;
import com.ailiaovideo.videoline.json.JsonRequestConfig;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.utils.BGTimedTaskManage;
import com.ailiaovideo.videoline.utils.Utils;
import com.idescout.sql.SqlScoutServer;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 闪屏页/程序入口
 */
public class SplashActivity extends BaseActivity implements BGTimedTaskManage.BGTimeTaskRunnable {
    private ImageView splashImg;
    private TextView splashBottomText;
    private LinearLayout timeBtn;
    private TextView timeText;
    //else
    private boolean isJumpOver = false;//标识是否跳过闪屏页面

    private boolean isLoadingConfigSuccess = false;

    private BGTimedTaskManage bgTimedTaskManage;
    private static final int MY_PERMISSIONS_REQUEST = 0;

    private int requestStatus = 0;

    private boolean SETTING_PERMISSION = false;

    @Override
    protected Context getNowContext() {
        return SplashActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash_page;
    }

    @Override
    protected boolean currentPageFinsh() {
        return true;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        splashImg = findViewById(R.id.splash_img);
        splashBottomText = findViewById(R.id.splash_bottom_text);
        timeBtn = findViewById(R.id.text_timebtn);
        timeText = findViewById(R.id.time_text);

        splashImg.setOnClickListener(this);
    }

    @Override
    protected void initSet() {
        setOnclickListener(timeBtn);
    }

    @Override
    protected void initData() {

        LogUtils.i(sHA1(this));
        Utils.getSign(this);
        //SqlScoutServer.create(this, getPackageName());
        bgTimedTaskManage = new BGTimedTaskManage();
        bgTimedTaskManage.setTimeTaskRunnable(this);
        bgTimedTaskManage.setTime(3 * 1000);
        bgTimedTaskManage.startRunnable(true);
        //获取配置信息
        requestConfigData();
    }

    @Override
    protected void initPlayerDisplayData() {
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * 延时处理
     * 参数:总体延时时间/间隔操作时间
     */
    private CountDownTimer timer = new CountDownTimer(4000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //每一次刷新操作
            timeText.setText("" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            //延时结束之后操作
            if (!isJumpOver) {
                doJumpMainActivity();
            }
        }
    };

    /**
     * 判断是否存在用户登录信息
     *
     * @return 存在返回true, 不存在返回false
     */
    private void doJumpMainActivity() {
        if (!isLoadingConfigSuccess) {
            return;
        }

        //检查是否有必须存在的权限
        checkPermissionStatus();

    }

    //跳转主页面
    private void startMainActivity() {
        //检查是否存在登录信息
        if (SaveData.getInstance().isIsLogin()) {
            doTXYunLogin();
            doJumpOver(true);
        } else {

            doJumpOver(false);
        }

    }

    //检查权限
    private void checkPermissionStatus() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            SETTING_PERMISSION = true;
        } else {

            startMainActivity();
        }
    }


    /**
     * 执行跳过广告页之后的操作
     */
    private void doJumpOver(boolean isRegister) {

        isJumpOver = true;
        timer.cancel();//关闭计时器
        if (isRegister) {

            goActivity(MainActivity.class).finish();
        } else {
            goActivity(CuckooLoginSelectActivity.class).finish();
        }
    }

    /**
     * 执行进入广告页操作
     */
    private void doJumpImg() {
        //广告页
        if (RequestConfig.getConfigObj().getSplashUrl() == null || TextUtils.isEmpty(RequestConfig.getConfigObj().getSplashUrl())) {
            return;
        }
        WebViewActivity.openH5Activity(this, false, "", RequestConfig.getConfigObj().getSplashUrl());
    }

    /**
     * 获取系统配置信息
     */
    private void requestConfigData() {
        Api.getConfigData(uId, uToken,
                new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestConfig requestObj = JsonRequestConfig.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            ConfigModel configObj = requestObj.getData();
                            //showToastMsg("获取配置信息成功");

                            //存储初始化信息到数据库中
                            ConfigModel.saveInitData(configObj);

                            //设置底部商标
                            splashBottomText.setText(configObj.getSplash_content());

                            //显示广告
                            if (configObj.getSplash_img_url() != null) {
                                Utils.loadHttpImg(SplashActivity.this, configObj.getSplash_img_url(), splashImg);
                            }

                            isLoadingConfigSuccess = true;

                            //做延时操作
                            timer.start();
                            requestStatus = 1;
                        } else {
                            showToastMsg(requestObj.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        requestStatus = 2;

                        ToastUtils.showLong(R.string.request_service_error);
                    }
                }
        );
    }

    /**
     * 使用预存储信息登录腾讯云
     */
    private void doTXYunLogin() {
        TxLogin.loginIm(
                SaveData.getInstance().getId(),
                SaveData.getInstance().getUserSig(),
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        //登录失败
                        log("腾讯云登录有误!" + s);
                    }

                    @Override
                    public void onSuccess() {
                        //登录成功
                        log("腾讯云登录成功!");
                        CuckooOnLoginTimSuccessEvent event = new CuckooOnLoginTimSuccessEvent();
                        EventBus.getDefault().post(event);

//                        TIMGroupManager.getInstance().applyJoinGroup(RequestConfig.getConfigObj().getGroupId(), "androidGo", new TIMCallBack() {
//                            @java.lang.Override
//                            public void onError(int code, String desc) {
//                                showToastMsg(getString(R.string.join_tencent_group_fail));
//                                log("加入广播大群失败!code==" + code);
//                            }
//
//                            @java.lang.Override
//                            public void onSuccess() {
//                                log("加入广播大群成功!");
//                            }
//                        });
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //跳过广告
            case R.id.text_timebtn:
                doJumpMainActivity();
                break;
            //跳转到广告页
            case R.id.splash_img:
                doJumpImg();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SETTING_PERMISSION) {
            checkPermissionStatus();
        }
    }

    @Override
    public void onRunTask() {
        if (requestStatus == 2) {

            requestConfigData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startMainActivity();
                } else {
                    showMsgDialog(getString(R.string.tips), getString(R.string.check_camera_audio_permission), new MsgDialogClick() {
                        @Override
                        public void doYes(QMUIDialog dialog, int index) {
                            Intent intent = new
                                    Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivity(intent);
                        }

                        @Override
                        public void doNo(QMUIDialog dialog, int index) {
                            showToastMsg(getString(R.string.check_camera_audio_permission));
                        }
                    });
                }
                break;
            }

            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgTimedTaskManage != null) {
            bgTimedTaskManage.stopRunnable();
        }
        bgTimedTaskManage = null;
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
