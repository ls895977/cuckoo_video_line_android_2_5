package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CuckooSelectLabelActivity extends BaseActivity {

    @BindView(R.id.tab_flow)
    TagFlowLayout tab_flow;
    private List<String> label;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_select_label;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getTopBar().setTitle(getString(R.string.plase_select_label));
        label = new ArrayList<>();
        label.add("情感专家");
        label.add("清纯美女");
        label.add("宅男女神");
        label.add("邻家小妹");

        tab_flow.setAdapter(new TagAdapter(label) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) LayoutInflater.from(getNowContext()).inflate(R.layout.view_evaluate_label,
                        tab_flow, false);
                tv.setText(label.get(position));
                tv.setTextSize(ConvertUtils.dp2px(4));
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setBackgroundResource(R.drawable.bg_evaluate_un_select);
                return tv;
            }


            @Override
            public void onSelected(int position, View view) {

                view.setBackgroundResource(R.drawable.bg_evaluate_select);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void unSelected(int position, View view) {
                view.setBackgroundResource(R.drawable.bg_evaluate_un_select);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.white));
            }
        });


        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSubmit();
            }
        });
    }

    private void clickSubmit() {

        Integer pos[] = new Integer[tab_flow.getSelectedList().size()];
        tab_flow.getSelectedList().toArray(pos);

        if(pos.length == 0){
            finish();
            return;
        }

        if(pos.length > LiveConstant.DATA_DEFINE.MAX_SELECT_SELF_LABEL_NUM){
            ToastUtils.showLong(getString(R.string.max_select) + LiveConstant.DATA_DEFINE.MAX_SELECT_SELF_LABEL_NUM + getString(R.string.a_label));
            return;
        }

        StringBuilder labelListStr = new StringBuilder();

        for (int i = 0 ;i < pos.length; i++){
            labelListStr.append(label.get(pos[i]) + "-");
        }

        Intent intent = new Intent();
        intent.putExtra(CuckooAuthFormActivity.USER_LABEL,labelListStr.toString());
        setResult(CuckooAuthFormActivity.RESULT_SELF_LABEL,intent);
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
