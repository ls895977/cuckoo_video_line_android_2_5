package com.ailiaovideo.videoline.msg.ui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonAboutFans;
import com.ailiaovideo.videoline.json.JsonRequestDoLoveTheUser;
import com.ailiaovideo.videoline.json.jsonmodle.AboutAndFans;
import com.ailiaovideo.videoline.msg.adapter.AboutFansAdapter;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 关注和粉丝页面
 */

public class AboutFansActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView fansRecycler;
    private AboutFansAdapter aboutFansAdapter;

    private String title;
    private int type;
    private int page = 1;

    private List<AboutAndFans> mListAboutAndFans = new ArrayList<>();

    @Override
    protected Context getNowContext() {
        return AboutFansActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about_fans;
    }

    @Override
    protected void initView() {

        fansRecycler = findViewById(R.id.aboutandfans_recycler);
        fansRecycler.setLayoutManager(new LinearLayoutManager(getNowContext()));

    }

    @Override
    protected void initSet() {
        getTopBar().setTitle(title);
    }

    @Override
    protected boolean hasTopBar() {
        return true;
    }

    @Override
    protected void initData() {

        title = getIntent().getStringExtra("str");
        type = title.equals("关注") ? 0 : 1;
        requestGetData();
    }

    //请求数据
    private void requestGetData() {
        if (type == 0) {
            requestAboutList();
            initAdapter(0);
        } else {
            requestFansList();
            initAdapter(1);
        }
    }

    @Override
    protected void initPlayerDisplayData() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.all_backbtn:
                finishNow();
                break;

            default:
                break;
        }
    }

    /**
     * 刷新适配器列表
     */
    private void initAdapter(int type) {
        aboutFansAdapter = new AboutFansAdapter(this, type, mListAboutAndFans);

        aboutFansAdapter.setOnLoadMoreListener(this,fansRecycler);
        aboutFansAdapter.disableLoadMoreIfNotFullPage();
        aboutFansAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.aboutandfans_loveme:
                        //view.setVisibility(View.GONE);
                        loveThisPlayer(position);
                        break;
                    case R.id.aboutandans_img:
                        Common.jumpUserPage(AboutFansActivity.this, mListAboutAndFans.get(position).getId());
                        break;
                    default:
                        Common.startPrivatePage(getNowContext(), mListAboutAndFans.get(position).getId());
                        break;
                }
            }
        });
        fansRecycler.setAdapter(aboutFansAdapter);
    }

    //关注
    private void loveThisPlayer(final int position) {
        Api.doLoveTheUser(
                mListAboutAndFans.get(position).getId(),
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestDoLoveTheUser requestObj = (JsonRequestDoLoveTheUser) JsonRequestDoLoveTheUser.getJsonObj(s, JsonRequestDoLoveTheUser.class);
                        if (requestObj.getCode() == 1) {

                            mListAboutAndFans.get(position).setFocus(String.valueOf(requestObj.getFollow()));
                            if (requestObj.getFollow() == 1) {
                                showToastMsg("关注成功!");
                            } else {
                                showToastMsg("操作成功!");
                                if (type == 0) {
                                    mListAboutAndFans.remove(position);
                                }
                            }

                            aboutFansAdapter.notifyDataSetChanged();

                        } else {
                            log("关注当前player:" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    /**
     * 获取粉丝列表
     */
    private void requestFansList() {
        Api.getFansDataList(
                uId,
                uToken,
                page,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonAboutFans requestObj = JsonAboutFans.getJsonObj(s);
                        if (requestObj.getCode() == 1) {

                            if(page == 1){
                                mListAboutAndFans.clear();
                            }

                            if(requestObj.getData().size() == 0){
                                aboutFansAdapter.loadMoreEnd();
                            }else{
                                aboutFansAdapter.loadMoreComplete();
                            }

                            mListAboutAndFans.addAll(requestObj.getData());
                            if(page == 1){
                                aboutFansAdapter.setNewData(mListAboutAndFans);
                            }else{
                                aboutFansAdapter.notifyDataSetChanged();
                            }

                        } else {
                            showToastMsg("获取粉丝列表::" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    /**
     * 获取关注列表
     */
    private void requestAboutList() {
        Api.getAboutDataList(
                uId,
                uToken,
                page,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonAboutFans requestObj = JsonAboutFans.getJsonObj(s);
                        if (requestObj.getCode() == 1) {

                            if(page == 1){
                                mListAboutAndFans.clear();
                            }

                            if(requestObj.getData().size() == 0){
                                aboutFansAdapter.loadMoreEnd();
                            }else{
                                aboutFansAdapter.loadMoreComplete();
                            }

                            mListAboutAndFans.addAll(requestObj.getData());
                            if(page == 1){
                                aboutFansAdapter.setNewData(mListAboutAndFans);
                            }else{
                                aboutFansAdapter.notifyDataSetChanged();
                            }

                        } else {
                            showToastMsg("获取关注列表::" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    @Override
    public void onLoadMoreRequested() {
        page ++;
        requestGetData();
    }
}
