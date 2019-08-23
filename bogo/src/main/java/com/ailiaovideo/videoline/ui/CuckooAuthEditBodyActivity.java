package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class CuckooAuthEditBodyActivity extends BaseActivity {


    @BindView(R.id.et_input)
    EditText et_input;

    private int resultCode;

    public static final String TITLE_LABEL = "TITLE_LABEL";


    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_auth_edit_body;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getTopBar().setTitle(getIntent().getStringExtra(TITLE_LABEL));
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickSubmit();
            }
        });
    }

    private void onClickSubmit() {
        String body = et_input.getText().toString();
        if(TextUtils.isEmpty(body)){
            showToastMsg(getString(R.string.not_empty));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(CuckooAuthFormActivity.USER_BODY,body);
        setResult(resultCode,intent);
        finish();
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        resultCode = getIntent().getIntExtra("RESULT_CODE",0);
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.ll_content})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_content:
                et_input.requestFocus();
                KeyboardUtils.showSoftInput(et_input);
                break;
        }
    }

    @Override
    protected boolean hasTopBar() {
        return true;
    }
}
