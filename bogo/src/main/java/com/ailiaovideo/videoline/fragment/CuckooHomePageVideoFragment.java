package com.ailiaovideo.videoline.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecycleViewShortVideoAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestGetShortVideoList;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.ui.CuckooVideoTouchPlayerActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;


public class CuckooHomePageVideoFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private RecycleViewShortVideoAdapter mShortVideoAdapter;
    private ArrayList<VideoModel> mVideoList = new ArrayList<>();

    public static final String TO_USER_ID = "TO_USER_ID";

    private int page = 1;
    private String toUserId;

    public static CuckooHomePageVideoFragment getInstance(String toUserId){

        CuckooHomePageVideoFragment cuckooHomePageVideoFragment = new CuckooHomePageVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TO_USER_ID,toUserId);
        cuckooHomePageVideoFragment.setArguments(bundle);
        return cuckooHomePageVideoFragment;
    }

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_cuckoo_home_page_video,null);
    }

    @Override
    protected void initView(View view) {
        rv_content_list.setLayoutManager(new GridLayoutManager(getContext(),2));

        mShortVideoAdapter = new RecycleViewShortVideoAdapter(getContext(),mVideoList);
        rv_content_list.setAdapter(mShortVideoAdapter);
        mShortVideoAdapter.setOnLoadMoreListener(this,rv_content_list);
        mShortVideoAdapter.setOnItemClickListener(this);
        mShortVideoAdapter.disableLoadMoreIfNotFullPage(rv_content_list);
    }

    @Override
    protected void initDate(View view) {
        toUserId = getArguments().getString(TO_USER_ID);
        requestGetVideoList();
    }

    @Override
    protected void initSet(View view) {

    }

    @Override
    protected void initDisplayData(View view) {

    }


    /**
     * @dw 获取当前用户的视频列表
     * */
    private void requestGetVideoList() {

        Api.doGetOtherUserShortVideoList(uId,uToken,toUserId,page,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetShortVideoList jsonObj = (JsonRequestGetShortVideoList) JsonRequestBase.getJsonObj(s,JsonRequestGetShortVideoList.class);
                if(jsonObj.getCode() == 1){

                    if(page == 1){
                        mVideoList.clear();
                    }
                    mVideoList.addAll(jsonObj.getList());

                    if(jsonObj.getList().size() == 0){
                        mShortVideoAdapter.loadMoreEnd();
                    }else{
                        mShortVideoAdapter.loadMoreComplete();
                    }
                }else{
                    showToastMsg(getContext(),jsonObj.getMsg());
                    mShortVideoAdapter.loadMoreEnd();
                }

                mShortVideoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        page ++;
        requestGetVideoList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Intent intent = new Intent(getContext(), CuckooVideoTouchPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_TYPE,0);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_LIST,mVideoList);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_POS,position);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_LIST_PAGE,page);

        startActivity(intent);
    }
}
