package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.RecommendAdaper;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonNormal;
import com.ailiaovideo.videoline.json.RecommendBean;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 为您推荐 lf
 */
public class RecommendActivity extends BaseActivity {

    private QMUITopBar qmuiTopBar;
    private RecyclerView recommendRv;
    private Button commendBtn;
    private RecommendAdaper recommendAdaper;
    private UserModel userModel;

    @Override
    protected Context getNowContext() {
        return RecommendActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        userModel = getIntent().getParcelableExtra("userModel");

        qmuiTopBar = findViewById(R.id.qmui_top_bar);
        recommendRv = findViewById(R.id.rv_content_list);
        commendBtn = findViewById(R.id.commend_btn);

        //layoutmanger
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        recommendRv.setLayoutManager(layoutManage);

        commendBtn.setOnClickListener(this);

        initTopBar();


    }


    private void initTopBar() {

//        qmuiTopBar.addLeftImageButton(R.drawable.icon_back_black, R.id.all_backbtn).setOnClickListener(this);
        qmuiTopBar.setTitle("为您推荐");


    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        requestUserData();
    }

    private void requestUserData() {
        Api.getRecommedListData(
                userModel.getId(),
                userModel.getToken(),
                new JsonCallback() {

                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        RecommendBean requestObj = (RecommendBean) RecommendBean.getJsonObj(s, RecommendBean.class);

                        if (requestObj.getCode() == 1) {
                            List<RecommendBean.RecommendChildBean> list = requestObj.getList();

                            recommendAdaper = new RecommendAdaper(RecommendActivity.this, list);
                            recommendRv.setAdapter(recommendAdaper);

                        } else {
                            showToastMsg("获取当前视频主播信息:" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.commend_btn:
                String commengStr = "";
                List<String> liveerList = new ArrayList<>();
                List<RecommendBean.RecommendChildBean> dataList = recommendAdaper.getDataList();
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).isChecked()) {
                        liveerList.add(dataList.get(i).getId() + "");

                    }
                }

                for (int i = 0; i < liveerList.size(); i++) {
                    if (i == liveerList.size() - 1) {
                        commengStr += liveerList.get(i);
                    } else {
                        commengStr += liveerList.get(i) + "&";
                    }
                }


                comment(commengStr);
                break;
        }

    }

    private void comment(String dataList) {

        if (TextUtils.isEmpty(dataList)) {
            LoginUtils.doLogin(getNowContext(), userModel);
            return;
        }

        Api.getCommendRecommedListData(
                dataList,
                userModel.getId(),
                userModel.getToken(),
                new JsonCallback() {

                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonNormal requestObj = JsonNormal.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            showToastMsg("一键关注成功");
                        } else {
                            showToastMsg("关注出现了点问题");
                        }

                        LoginUtils.doLogin(getNowContext(), userModel);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                }
        );
    }

    @Override
    protected void initPlayerDisplayData() {

    }

}
