package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ClipboardUtils;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.InviteUserListAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonRequestGetInvitePage;
import com.ailiaovideo.videoline.modle.InviteUserListModel;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class InviteActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private QMUITopBar qmuiTopBarLayout;
    private RecyclerView mRvContentList;

    private TextView mTvIncomeTotal;
    private TextView mTvDayIncomeTotal;
    private TextView mTvInviteCount;
    private QMUIRoundButton mBtnInviteCopyCode,mBtnInviteCopyUrl;
    private InviteUserListAdapter mInviteUserListAdapter;

    private String inviteCode = "";
    private List<InviteUserListModel> mInviteUserListModels = new ArrayList<>();
    private int page = 1;
    

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_invite;
    }

    @Override
    protected void initView() {
        qmuiTopBarLayout = findViewById(R.id.qmui_top_bar);
        mBtnInviteCopyCode = findViewById(R.id.btn_invite_copy);
        mBtnInviteCopyUrl = findViewById(R.id.btn_invite_url_copy);
        mRvContentList = findViewById(R.id.rv_content_list);
        mTvIncomeTotal = findViewById(R.id.tv_total_income);
        mTvDayIncomeTotal = findViewById(R.id.tv_day_income);
        mTvInviteCount = findViewById(R.id.tv_invite_count);
        mBtnInviteCopyUrl.setOnClickListener(this);
        mBtnInviteCopyCode.setOnClickListener(this);
        initTopBar();
    }

    @Override
    protected void initSet() {
        mInviteUserListAdapter = new InviteUserListAdapter(this,mInviteUserListModels);
        mRvContentList.setLayoutManager(new LinearLayoutManager(this));
        mRvContentList.setAdapter(mInviteUserListAdapter);
        mInviteUserListAdapter.setOnLoadMoreListener(this,mRvContentList);
        mInviteUserListAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    protected void initData() {

        requestGetPageInfo();
    }


    private void initTopBar() {
        qmuiTopBarLayout.addLeftImageButton(R.drawable.icon_back_black,R.id.all_backbtn).setOnClickListener(this);
        qmuiTopBarLayout.setTitle("邀请好友");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.all_backbtn:

                finish();
                break;
            case R.id.btn_invite_copy:

                ClipboardUtils.copyText(inviteCode);
                showToastMsg("复制邀请码成功:" + inviteCode);
                break;
            case R.id.btn_invite_url_copy:
                String url = RequestConfig.getConfigObj().getInviteShareRegUrl() + "?invite_code=" + inviteCode;
                ClipboardUtils.copyText(url);
                showToastMsg("复制邀请链接成功");
                break;
            default:

                break;
        }
    }

    @Override
    protected void initPlayerDisplayData() {

    }


    private void requestGetPageInfo() {

        Api.doRequestGetInviteData(uId,uToken,page,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetInvitePage jsonObj =
                        (JsonRequestGetInvitePage) JsonRequestGetInvitePage.getJsonObj(s,JsonRequestGetInvitePage.class);
                if(jsonObj.getCode() == 1){

                    inviteCode = jsonObj.getInvite_code();

                    if(page > 1){
                        mInviteUserListAdapter.loadMoreComplete();
                    }else{

                        mBtnInviteCopyCode.setText("点击复制邀请码:" + jsonObj.getInvite_code());
                        mInviteUserListModels.clear();
                        //邀请当日总收益
                        mTvDayIncomeTotal.setText(jsonObj.getDay_income_total());
                        //邀请总收益
                        mTvIncomeTotal.setText(jsonObj.getIncome_total());
                        //邀请总人数
                        mTvInviteCount.setText(jsonObj.getInvite_user_count());
                    }

                    //是否加载完毕
                    if(jsonObj.getInvite_user_list().size() == 0){

                        mInviteUserListAdapter.loadMoreEnd();
                    }else{

                        mInviteUserListAdapter.loadMoreComplete();
                    }
                    mInviteUserListModels.addAll(jsonObj.getInvite_user_list());
                    mInviteUserListAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public static void startInviteAcitivty(Context context) {
        Intent intent = new Intent(context,InviteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
        page ++;
        requestGetPageInfo();
    }
}
