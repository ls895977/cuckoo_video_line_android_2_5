package com.ailiaovideo.videoline.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecycleUserHomePhotoAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestTarget;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.ui.PrivatePhotoActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;


public class CuckooHomePageImageFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private TargetUserData targetUserData;//当前目标用户信息
    public static final String TO_USER_ID = "TO_USER_ID";

    private int evaluatePage = 1;
    private String toUserId;
    private RecycleUserHomePhotoAdapter recycleUserHomePhotoAdapter;
    private ArrayList<TargetUserData.PicturesBean> photoList = new ArrayList<>();

    public static CuckooHomePageImageFragment getInstance(String toUserId) {

        CuckooHomePageImageFragment cuckooHomePageVideoFragment = new CuckooHomePageImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TO_USER_ID, toUserId);
        cuckooHomePageVideoFragment.setArguments(bundle);
        return cuckooHomePageVideoFragment;
    }

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_cuckoo_home_page_video, null);
    }

    @Override
    protected void initView(View view) {

        //设置发布的私照列表
//        LinearLayoutManager listPhotoLayoutManage = new LinearLayoutManager(getContext());
//        listPhotoLayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_content_list.setLayoutManager(new GridLayoutManager(getContext(),2));

        recycleUserHomePhotoAdapter = new RecycleUserHomePhotoAdapter(getContext(), photoList);
        recycleUserHomePhotoAdapter.setOnItemClickListener(this);
//        mShortVideoAdapter.setOnLoadMoreListener(this, rv_content_list);
//        mShortVideoAdapter.setOnItemClickListener(this);
//        mShortVideoAdapter.disableLoadMoreIfNotFullPage(rv_content_list);
    }

    @Override
    protected void initDate(View view) {
        toUserId = getArguments().getString(TO_USER_ID);
        requestGetImageList();
    }

    @Override
    protected void initSet(View view) {

    }

    @Override
    protected void initDisplayData(View view) {

    }


    /**
     * @dw 获取当前用户的私照列表
     */
    private void requestGetImageList() {
        Api.getUserData(
                toUserId,
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JsonRequestTarget jsonTargetUser = JsonRequestTarget.getJsonObj(s);
                        if (jsonTargetUser.getCode() == 1) {
                            targetUserData = jsonTargetUser.getData();

                            //填充发布的私照列表
                            if (targetUserData.getPictures() != null) {
                                photoList.addAll(targetUserData.getPictures());
                                recycleUserHomePhotoAdapter.notifyDataSetChanged();
                            }


                            rv_content_list.setAdapter(recycleUserHomePhotoAdapter);

                        } else {
                            //请求失败
                            showToastMsg(getContext(), jsonTargetUser.getMsg());
                        }
                    }
                }
        );
    }

    @Override
    public void onLoadMoreRequested() {
        evaluatePage++;
        requestGetImageList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PrivatePhotoActivity.startPrivatePhotoActivity(getContext(), toUserId, "", 0);

    }
}
