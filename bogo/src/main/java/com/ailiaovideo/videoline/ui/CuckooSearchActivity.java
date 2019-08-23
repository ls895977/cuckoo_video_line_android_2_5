package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.CuckooSearchListAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonDoRequestGetSearchList;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CuckooSearchActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.et_key_word)
    EditText et_key_word;

    @BindView(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private ArrayList<UserModel> list = new ArrayList<>();
    private CuckooSearchListAdapter cuckooSearchListAdapter;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_search;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        rv_content_list.setLayoutManager(new LinearLayoutManager(this));
        cuckooSearchListAdapter = new CuckooSearchListAdapter(list);
        rv_content_list.setAdapter(cuckooSearchListAdapter);
        cuckooSearchListAdapter.setOnItemClickListener(this);
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

    @OnClick({R.id.tv_search, R.id.iv_back, R.id.tv_back})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_search:

                clickSearch();
                break;
            case R.id.iv_back:
            case R.id.tv_back:

                finish();
                break;
            default:
                break;
        }
    }

    private void clickSearch() {
        String keyWord = et_key_word.getText().toString();
        if (TextUtils.isEmpty(keyWord)) {
            ToastUtils.showLong(R.string.plase_input_search_content);
            return;
        }

        Api.doRequestSearch(SaveData.getInstance().getId(), keyWord, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonDoRequestGetSearchList data = (JsonDoRequestGetSearchList) JsonRequestBase.getJsonObj(s, JsonDoRequestGetSearchList.class);
                if (StringUtils.toInt(data.getCode()) == 1) {
                    list.clear();
                    list.addAll(data.getList());
                    cuckooSearchListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showLong(data.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showLong(R.string.search_error);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Common.jumpUserPage(CuckooSearchActivity.this, list.get(position).getId());
    }
}
