package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailiaovideo.videoline.ui.common.Common;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.UserContributionRankAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonRequestRank;
import com.ailiaovideo.videoline.modle.RankModel;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * @dw 个人贡献榜单
 */
public class UserContribuionRankActivity extends BaseActivity {

    private QMUITopBar qmuiTopBar;

    private RecyclerView mRvContentList;
    private String toUserId;
    private List<RankModel> mRankList = new ArrayList<>();

    public static final String TO_USER_ID = "TO_USER_ID";
    private UserContributionRankAdapter userContributionRankAdapter;

    @Override
    protected Context getNowContext() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_contribuion_rank;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mRvContentList = findViewById(R.id.rv_content_list);


        mRvContentList.setLayoutManager(new LinearLayoutManager(this));
        userContributionRankAdapter = new UserContributionRankAdapter(this, mRankList);
        mRvContentList.setAdapter(userContributionRankAdapter);
        userContributionRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Common.jumpUserPage(UserContribuionRankActivity.this, mRankList.get(position).getId());
            }
        });

        initTopBar();
    }

    private void initTopBar() {

        qmuiTopBar = findViewById(R.id.qmui_top_bar);
        qmuiTopBar.addLeftImageButton(R.drawable.icon_back_black, R.id.all_backbtn).setOnClickListener(this);
        qmuiTopBar.setTitle(getString(R.string.contribution));

    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        toUserId = getIntent().getStringExtra(TO_USER_ID);
        requestGetData();
    }

    @Override
    protected void initPlayerDisplayData() {

    }


    private void requestGetData() {

        Api.doRequestGetUserContributionRank(uId, uToken, toUserId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestRank requestRank = (JsonRequestRank) JsonRequestRank.getJsonObj(s, JsonRequestRank.class);
                if (requestRank.getCode() == 1) {
                    mRankList.clear();
                    mRankList.addAll(requestRank.getList());
                    userContributionRankAdapter.notifyDataSetChanged();
                } else {
                    showToastMsg(requestRank.getMsg());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.all_backbtn:
                finish();
                break;
            default:
                break;
        }
    }
}
