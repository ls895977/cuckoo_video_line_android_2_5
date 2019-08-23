package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class CuckooAuthPhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText et_phone;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_auth_phone;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getTopBar().setTitle(getString(R.string.bind_mobile));

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSubmit();
            }
        });
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.tv_send_code})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_send_code:

                clickSendCode();
                break;

            default:
                break;
        }
    }

    private void clickSubmit() {
        String phone = et_phone.getText().toString();

        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            showToastMsg(getString(R.string.plase_input_correct_mobile));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(CuckooAuthFormActivity.USER_PHONE, phone);
        setResult(CuckooAuthFormActivity.RESULT_PHONE, intent);
        finish();
    }

    private void clickSendCode() {

    }

    @Override
    protected boolean hasTopBar() {
        return true;
    }
}
