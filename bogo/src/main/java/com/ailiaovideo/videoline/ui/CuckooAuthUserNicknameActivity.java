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

public class CuckooAuthUserNicknameActivity extends BaseActivity {

    @BindView(R.id.et_input)
    EditText et_input;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_auth_user_nickname;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getTopBar().setTitle(getString(R.string.set_nickname));

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickSubmit();
            }
        });
    }

    private void onClickSubmit() {

        String name = et_input.getText().toString();
        if(TextUtils.isEmpty(name)){
           showToastMsg(getString(R.string.nickname_not_empty));
           return;
        }

        Intent intent = new Intent();
        intent.putExtra(CuckooAuthFormActivity.USER_NICKNAME,name);
        setResult(CuckooAuthFormActivity.RESULT_NICKNAME_CODE,intent);
        finish();

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

    @Override
    protected boolean hasTopBar() {
        return true;
    }
}
