package com.ailiaovideo.videoline.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.json.JsonRequestOneKeyCall;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.adapter.BannerAdaper;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.json.JsonRequestsDoCall;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.CuckooOrderActivity;
import com.ailiaovideo.videoline.ui.PlayerCallActivity;
import com.ailiaovideo.videoline.ui.VideoPlayerActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecyclerRecommendAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseListFragment;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestsImage;
import com.ailiaovideo.videoline.json.JsonRequestsTarget;
import com.ailiaovideo.videoline.json.jsonmodle.ImageData;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.WebViewActivity;
import com.ailiaovideo.videoline.utils.GlideImageLoader;
import com.ailiaovideo.videoline.utils.Utils;
import com.lzy.okgo.callback.StringCallback;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 推荐
 */

public class RecommendFragment extends BaseListFragment<TargetUserData> {

    //轮播图
    private Banner recommendRoll;
    //头部布局
    private View rollView;

    private TextView mTvOnlineEmceeCount;
    private RelativeLayout mRlOneKeyCall;
    private LinearLayout mLlOnlineEmcee;

    private ArrayList<String> rollImg = new ArrayList<>();
    private List<ImageData> imgs = new ArrayList<>();
    private RecyclerView mBannerRv;

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new RecyclerRecommendAdapter(getContext(), dataList);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManage() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    protected void initDate(View view) {

    }

    @Override
    protected void initSet(View view) {


        //引入头部信息
        rollView = LayoutInflater.from(getContext()).inflate(R.layout.view_roll_page, null);

        mBannerRv = rollView.findViewById(R.id.mBannerRv);

        mTvOnlineEmceeCount = rollView.findViewById(R.id.tv_online_count);
        mLlOnlineEmcee = rollView.findViewById(R.id.ll_online_emcee);

        mRlOneKeyCall = rollView.findViewById(R.id.rl_one_key_call);
        mRlOneKeyCall.setOnClickListener(this);
        recommendRoll = rollView.findViewById(R.id.mRollPagerView);
        recommendRoll.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                ImageData imageData = imgs.get(position);
                if (imageData.getUrl() != null && imageData.getTitle() != null) {

                    if (imageData.getUrl().equals("#order_page")) {

                        Intent intent = new Intent(getContext(), CuckooOrderActivity.class);
                        startActivity(intent);
                    } else {
                        WebViewActivity.openH5Activity(getContext(), true, imageData.getTitle(), imageData.getUrl());
                    }
                }
            }
        });

        if (SaveData.getInstance().getUserInfo() != null && StringUtils.toInt(SaveData.getInstance().getUserInfo().getSex()) == 1) {
            mRlOneKeyCall.setVisibility(View.VISIBLE);
        }

        //添加头部
        baseQuickAdapter.addHeaderView(rollView);
        baseQuickAdapter.notifyDataSetChanged();

        //rv
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBannerRv.setLayoutManager(linearLayoutManager);

        if (rollImg != null && rollImg.size() > 0) {
            //设置适配器
            BannerAdaper mAdapter = new BannerAdaper(getContext(), rollImg);
            mBannerRv.setAdapter(mAdapter);

            click(mAdapter);
        }

    }

    @Override
    protected void initDisplayData(View view) {

    }

    @Override
    public void fetchData() {
        //加载推荐页轮播图
        requestRecommendRoll();
        //加载数据源
        requestGetData(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_one_key_call:
                clickOneKeyCall();
                break;
            default:
                break;
        }
    }

    //一键约爱
    private void clickOneKeyCall() {

        showLoadingDialog("正在接通...");
        Api.doRequestOneKeyCall(
                SaveData.getInstance().getId(),
                SaveData.getInstance().getToken(),
                new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        hideLoadingDialog();

                        JsonRequestOneKeyCall requestObj = (JsonRequestOneKeyCall) JsonRequestOneKeyCall.getJsonObj(s,JsonRequestOneKeyCall.class);
                        if (requestObj.getCode() == 1) {
                            Common.callVideo(getActivity(),requestObj.getEmcee_id(),0);
                        } else {
                            LogUtils.i("拨打电话error:" + requestObj.getMsg());
                            ToastUtils.showLong(requestObj.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        hideLoadingDialog();
                    }
                }

        );
    }

    /**
     * 加载轮播图
     */
    private void requestRecommendRoll() {
        Api.getRollImage(
                uId,
                uToken,
                "1",
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonRequestsImage requestObj = JsonRequestsImage.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            rollImg.clear();
                            imgs = requestObj.getData();
                            for (ImageData img : imgs) {
                                rollImg.add(Utils.getCompleteImgUrl(img.getImage()));
                            }
                            refreshRollView();

//                            //设置适配器
//                            BannerAdaper mAdapter = new BannerAdaper(getContext(), rollImg);
//                            mBannerRv.setAdapter(mAdapter);
//
//                            click(mAdapter);


                        }
                    }
                }
        );
    }

    private void click(BannerAdaper mAdapter) {
        //banner点击
        mAdapter.setClickItemListener(new BannerAdaper.ClickItemListener() {
            @Override
            public void onItemClickListener(int posi) {
                ImageData imageData = imgs.get(posi);
                if (imageData.getUrl() != null && imageData.getTitle() != null) {

                    if (imageData.getUrl().equals("#order_page")) {

                        Intent intent = new Intent(getContext(), CuckooOrderActivity.class);
                        startActivity(intent);
                    } else {
                        WebViewActivity.openH5Activity(getContext(), true, imageData.getTitle(), imageData.getUrl());
                    }
                }
            }
        });
    }

    @Override
    protected void requestGetData(boolean isCache) {
        final int userHeadWidth = ConvertUtils.dp2px(30);
        Api.getRecommendUserList(
                uId,
                uToken,
                page,
                new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonRequestsTarget requestObj = JsonRequestsTarget.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            if (page == 1) {
                                mLlOnlineEmcee.removeAllViews();
                                //在线主播头像
                                for (UserModel user : requestObj.getOnline_emcee()) {
                                    CircleImageView iv = new CircleImageView(getContext());
                                    iv.setLayoutParams(new ViewGroup.LayoutParams(userHeadWidth, userHeadWidth));
                                    Utils.loadHttpImg(getContext(), Utils.getCompleteImgUrl(user.getAvatar()), iv);
                                    mLlOnlineEmcee.addView(iv);
                                }
                            }
                            mTvOnlineEmceeCount.setText(requestObj.getOnline_emcee_count() + "人正在接单");
                            onLoadDataResult(requestObj.getData());
                        } else {
                            onLoadDataError();
                            showToastMsg(getContext(), requestObj.getMsg());
                        }
                    }
                }
        );
    }

    /**
     * 刷新轮播
     */
    private void refreshRollView() {
        //设置图片加载器
        recommendRoll.setImageLoader(new GlideImageLoader());
        //设置图片集合
        recommendRoll.setImages(rollImg);
        //banner设置方法全部调用完毕时最后调用
        recommendRoll.start();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshRollView();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Common.jumpUserPage(getContext(), dataList.get(position).getId());
    }
}