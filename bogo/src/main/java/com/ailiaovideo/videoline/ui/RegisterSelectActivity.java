package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestUserBase;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.ailiaovideo.videoline.widget.FullScreenVideoView;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登录选择
 * Created by fly on 2017/12/22 0022.
 */

public class RegisterSelectActivity extends BaseActivity implements PlatformActionListener {
    //功能
    private FullScreenVideoView registerVideo;
    private EditText phoneNumberEdtext;//手机号输入框
    private EditText phoneCodeEdtext;//验证码输入框

    //按钮
    private Button codePhoneBtn;//验证按钮
    private Button registerPhoneBtn;//登录按钮
    private ImageView regiterFromPhone;//手机号登录
    private String inviteCode = "";

    /////////////////////////////////////////////初始化操作/////////////////////////////////////////
    @Override
    protected Context getNowContext() {
        return RegisterSelectActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register_page;
    }

    @Override
    protected void initView() {
        regiterFromPhone = findViewById(R.id.regiter_from_phone);

        codePhoneBtn = findViewById(R.id.code_phone_btn);
        registerPhoneBtn = findViewById(R.id.register_phone_btn);

        phoneNumberEdtext = findViewById(R.id.phone_number_edtext);
        phoneCodeEdtext = findViewById(R.id.phone_code_edtext);

        registerVideo = findViewById(R.id.register_video);
        doPhoneRegister();
    }

    @Override
    protected void initSet() {
        setOnclickListener(regiterFromPhone, codePhoneBtn, registerPhoneBtn);
        setOnclickListener(R.id.terms_of_service, R.id.register_weixin_btn, R.id.regiter_from_weixin);
        phoneNumberEdtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (com.ailiaovideo.videoline.utils.Utils.isMobile(s.toString())) {
                    codePhoneBtn.setBackgroundResource(R.drawable.bg_pink_btn);
                    codePhoneBtn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneCodeEdtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {

                    registerPhoneBtn.setBackgroundResource(R.drawable.bg_pink_btn);
                    registerPhoneBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerVideo.start();
    }

    @Override
    protected void initData() {
        registerVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mox));
        registerVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                registerVideo.start();
            }
        });
    }

    @Override
    protected void initPlayerDisplayData() {
    }

    /////////////////////////////////////////////公共监听事件处理//////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //服务和隐私条款
            case R.id.terms_of_service:
                doReadService();
                break;
            //选择手机登录
            case R.id.regiter_from_phone:
                doPhoneRegister();
                break;
            //选择微信登录
            case R.id.regiter_from_weixin:
                doPhoneRegister();
                break;
            //微信登录
            case R.id.register_weixin_btn:
                doLogin(1);
                break;
            //发送验证信息
            case R.id.code_phone_btn:
                doSendCode();
                break;
            //登录按钮
            case R.id.register_phone_btn:
                doLogin(0);
                break;

            default:
                break;
        }
    }

    /////////////////////////////////////////////业务逻辑处理/////////////////////////////////////////


    //执行登录操作
    private void doRegister() {
        showLoadingDialog(getString(R.string.loading_login));
        //doYunxinLogin();//云信的登录操作
        if (!phoneCodeEdtext.getText().toString().equals("")) {
            doPhoneLogin(phoneNumberEdtext.getText().toString(), phoneCodeEdtext.getText().toString(),"");
        } else {
            hideLoadingDialog();
            showToastMsg(getString(R.string.code_not_empty));
        }
    }

    //微信一键登录
    private void doWeiChat() {
        //ToastUtils.showLong("暂未开放!");

        Platform plat = ShareSDK.getPlatform(Wechat.NAME);

        //执行登录，登录后在回调里面获取用户资料
        plat.showUser(null);
        plat.SSOSetting(false);  //设置false表示使用SSO授权方式
        plat.setPlatformActionListener(this);
        plat.removeAccount(true);
    }

    /**
     * 接口登录
     */
    private void doLogin(final int type) {

        //获取OpenInstall安装数据
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                if (!TextUtils.isEmpty(bindData)) {
                    JSONObject data = JSON.parseObject(bindData);
                    inviteCode = data.getString("invite_code");

                }

                if (type == 0) {
                    doRegister();
                } else {
                    doWeiChat();
                }
                Log.d("OpenInstall", "getInstall : installData = " + appData.toString());
            }
        });

    }

    //手机登录
    private void doPhoneLogin(String mobile, String code,String agent) {
        Api.userLogin(mobile, code, inviteCode, agent,"",new JsonCallback() {
            @Override
            public Context getContextToJson() {
                return getNowContext();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {

                tipD.dismiss();
                JsonRequestUserBase requestObj = JsonRequestUserBase.getJsonObj(s);
                if (requestObj.getCode() == 1) {

                    //是否完善资料
                    if (requestObj.getData().getIs_reg_perfect() == 1) {

                        LoginUtils.doLogin(RegisterSelectActivity.this, requestObj.getData());
                        finish();
                    } else {
                        Intent intent = new Intent(getNowContext(), PerfectRegisterInfoActivity.class);
                        intent.putExtra(PerfectRegisterInfoActivity.USER_LOGIN_INFO, requestObj.getData());
                        startActivity(intent);
                        finish();
                    }
                }
                showToastMsg(requestObj.getMsg());
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                tipD.dismiss();
            }
        });
    }


    //三方授权登录
    private void doPlatLogin(String platId) {

        showLoadingDialog(getString(R.string.loading_login));

        Api.doPlatAuthLogin(platId, inviteCode, "","",new JsonCallback() {
            @Override
            public Context getContextToJson() {
                return getNowContext();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestUserBase requestObj = JsonRequestUserBase.getJsonObj(s);
                if (requestObj.getCode() == 1) {

                    //是否完善资料
                    if (requestObj.getData().getIs_reg_perfect() == 1) {

                        LoginUtils.doLogin(RegisterSelectActivity.this, requestObj.getData());
                    } else {
                        Intent intent = new Intent(getNowContext(), PerfectRegisterInfoActivity.class);
                        intent.putExtra(PerfectRegisterInfoActivity.USER_LOGIN_INFO, requestObj.getData());
                        startActivity(intent);
                        finish();
                    }
                }
                showToastMsg(requestObj.getMsg());
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }


    //发送验证码
    private void doSendCode() {
        if (com.ailiaovideo.videoline.utils.Utils.isMobile(phoneNumberEdtext.getText().toString())) {
            sendCode(phoneNumberEdtext.getText().toString());
            codePhoneBtn.setEnabled(false);
            timer.start();
        } else {
            showToastMsg(getString(R.string.mobile_login_mobile_error));
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode(String str) {
        Api.sendCodeByRegister(str,"", new JsonCallback() {
            @Override
            public Context getContextToJson() {
                return getNowContext();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s, call, response);
                showToastMsg(ApiUtils.getJsonObj2(s).getString("msg"));
            }
        });
    }

    //阅读服务条款
    private void doReadService() {

        WebViewActivity.openH5Activity(this, false, "隐私条款", ConfigModel.getInitData().getApp_h5().getPrivate_clause_url());
    }

    //执行手机登录切换
    private void doPhoneRegister() {
        //改变状态
        showOrConceal(R.id.register_weixin_btn, R.id.register_phone_menu, R.id.regiter_from_phone, R.id.regiter_from_weixin);
    }


    //////////////////////////////////////////延时操作处理////////////////////////////////////////////
    /**
     * 延时处理
     * 参数:总体延时时间/间隔操作时间
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //每一次刷新操作
            codePhoneBtn.setText(getString(R.string.re_send) + "(" + millisUntilFinished / 1000 + "s)");
            codePhoneBtn.setEnabled(false);
        }

        @Override
        public void onFinish() {
            //延时结束之后操作
            codePhoneBtn.setEnabled(true);
            codePhoneBtn.setText(R.string.check);
            timer.cancel();//关闭计时器
        }
    };

    /////////////////////////////////////////////页面设置操作/////////////////////////////////////////
    @Override
    protected boolean currentPageFinsh() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerVideo.start();
    }

    @Override
    public void onComplete(final Platform platform, int action, HashMap<String, Object> hashMap) {

        //用户资源都保存到res
        //通过打印res数据看看有哪些数据是你想要的
        if (action == Platform.ACTION_USER_INFOR) {
            final PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
            //platDB.getToken();
            //platDB.getUserGender();
            //platDB.getUserIcon();
            platDB.getUserId();
            //platDB.getUserName();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doPlatLogin(platDB.getUserId());
                }
            });
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

        ToastUtils.showShort(R.string.login_fail);
    }

    @Override
    public void onCancel(Platform platform, int i) {

        ToastUtils.showShort(R.string.login_cancel);
    }
}
