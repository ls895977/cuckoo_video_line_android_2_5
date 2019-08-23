package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestUserBase;
import com.ailiaovideo.videoline.modle.CuckooOpenInstallModel;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CuckooPassLoginActivity extends BaseActivity {

    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @BindView(R.id.et_pass)
    EditText et_pass;

    private String uuid;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_pass_login;
    }

    @Override
    protected void initView() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getTopBar().setTitle(getString(R.string.login));
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        uuid = Utils.getUniquePsuedoID();
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.btn_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                clickDoLogin();
                break;
            default:
                break;
        }
    }

    private void clickDoLogin() {

        String user = et_mobile.getText().toString();
        String pass = et_pass.getText().toString();

        if (!TextUtils.isEmpty(user) || !TextUtils.isEmpty(pass)) {
            doPhoneLogin(user, pass);
        } else {
            showToastMsg("账号或密码不能为空！");
        }
    }

    private void doPhoneLogin(final String user, final String pass) {
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

                Api.userPassLogin(user, pass, inviteCode, agent, uuid, new JsonCallback() {
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
                                LoginUtils.doLogin(CuckooPassLoginActivity.this, requestObj.getData());
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

    @Override
    protected boolean hasTopBar() {
        return true;
    }
}
