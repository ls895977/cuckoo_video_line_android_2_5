package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailiaovideo.videoline.BuildConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestUserBase;
import com.ailiaovideo.videoline.modle.CuckooOpenInstallModel;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.blankj.utilcode.util.ToastUtils;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CuckooMobileLoginActivity extends BaseActivity {

    @BindView(R.id.tv_send_code)
    TextView tv_send_code;

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.iv_img_code)
    ImageView iv_img_code;

    @BindView(R.id.et_img_code)
    EditText et_img_code;

    private String uuid;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_mobile_login;
    }

    @Override
    protected void initView() {

        //QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getTopBar().setTitle(getString(R.string.login));
        iv_img_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImgCode();
            }
        });
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        uuid = Utils.getUniquePsuedoID();
    }

    private void loadImgCode() {
        if (!Utils.isMobile(et_mobile.getText().toString())) {
            showToastMsg("手机号码错误！");
            return;
        }
        iv_img_code.setImageResource(0);
        OkGo.get(BuildConfig.SERVER_URL + "/mapi/public/index.php/api/login_api/get_img_code/time/"
                + System.currentTimeMillis() + "/mobile/" + et_mobile.getText().toString())//
                .tag(this)//
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        // bitmap 即为返回的图片数据
                        iv_img_code.setImageBitmap(bitmap);
                    }
                });

//        Utils.loadHttpImg(BuildConfig.SERVER_URL + "/mapi/public/index.php/api/login_api/get_img_code/time/"
//                + System.currentTimeMillis() + "/mobile/" + et_mobile.getText().toString(), iv_img_code);
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.tv_send_code, R.id.btn_submit, R.id.tv_pass_login})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_code:
                clickSendCode();
                break;
            case R.id.btn_submit:
                clickDoLogin();
                break;
            case R.id.tv_pass_login:
                Intent intent = new Intent(this, CuckooPassLoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void clickDoLogin() {

        if (!tv_send_code.getText().toString().equals("")) {
            doPhoneLogin(et_mobile.getText().toString(), et_code.getText().toString());
        } else {
            showToastMsg(getString(R.string.mobile_login_code_not_empty));
        }
    }

    //发送验证码
    private void clickSendCode() {

        if (TextUtils.isEmpty(et_img_code.getText().toString())) {
            loadImgCode();
            showToastMsg("请填写图形验证码");
            return;
        }

        if (Utils.isMobile(et_mobile.getText().toString())) {
            sendCode(et_mobile.getText().toString());
            tv_send_code.setEnabled(false);

            new CountDownTimer(60 * 1000, 1000) {

                @Override
                public void onTick(long l) {
                    tv_send_code.setText("（" + (l / 1000) + "）");
                }

                @Override
                public void onFinish() {
                    tv_send_code.setText("发送验证码");
                    tv_send_code.setEnabled(true);
                }
            }.start();

        } else {
            showToastMsg(getString(R.string.mobile_login_mobile_error));
        }
    }


    //手机登录
    private void doPhoneLogin(final String mobile, final String code) {

        showLoadingDialog(getString(R.string.loading_login));

        //获取OpenInstall安装数据
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                //bindData = "{\"agent\":\"5001\"}";

                String inviteCode = "";
                String agent = "";
                if (!TextUtils.isEmpty(bindData)) {
                    CuckooOpenInstallModel data = JSON.parseObject(bindData, CuckooOpenInstallModel.class);
                    inviteCode = data.getInvite_code();
                    agent = data.getAgent();
                }

                Api.userLogin(mobile, code, inviteCode, agent, uuid, new JsonCallback() {
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

                                LoginUtils.doLogin(CuckooMobileLoginActivity.this, requestObj.getData());
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
        });

    }


    /**
     * 发送验证码
     */
    private void sendCode(String str) {
        String img_code = et_img_code.getText().toString();
        Api.sendCodeByRegister(str, img_code, new JsonCallback() {
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

    @Override
    protected boolean hasTopBar() {
        return true;
    }
}
