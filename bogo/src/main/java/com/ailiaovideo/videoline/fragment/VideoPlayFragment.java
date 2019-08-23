package com.ailiaovideo.videoline.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.inter.VideoDoClick;
import com.ailiaovideo.videoline.json.JsonRequest;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetVideoChatPageInfo;
import com.ailiaovideo.videoline.json.JsonRequestGetVideoLive;
import com.ailiaovideo.videoline.json.JsonRequestsImage;
import com.ailiaovideo.videoline.json.jsonmodle.ImageData;
import com.ailiaovideo.videoline.modle.SlideModel;
import com.ailiaovideo.videoline.modle.TabLiveListModel;
import com.ailiaovideo.videoline.ui.WebViewActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.GlideImageLoader;
import com.ailiaovideo.videoline.utils.SlideImageLoader;
import com.ailiaovideo.videoline.widget.VideoDoFragment;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 视频聊
 * Created by fly on 2017/12/28 0028.
 */
public class VideoPlayFragment extends BaseFragment{

    private static final int ALL_VIDEO = 0;
    private static final int FIRST_VIDEO = 1;
    private static final int TREE_VIDEO = 2;

    private LinearLayout videoPlayAllPush;//换一批
    private Banner mBanner;

    private VideoDoFragment leftVideo;//左侧视频
    private VideoDoFragment rightVideo;//右侧视频

    private QMUITopBar msgPageTopBar;//顶部bar
    private List<SlideModel> bannerList = new ArrayList<>();

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    private TabLiveListModel tabLiveListModelFirst,tabLiveListModelTree;

    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;


    //////////////////////////////////////////初始化操作//////////////////////////////////////////////
    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.view_videoplay_page, container,false);
    }

    @Override
    protected void initView(View view) {
        videoPlayAllPush = view.findViewById(R.id.videoplay_all_push);
        leftVideo = view.findViewById(R.id.left_view);
        rightVideo = view.findViewById(R.id.right_view);
        msgPageTopBar = view.findViewById(R.id.msg_page_topBar);
        mBanner = view.findViewById(R.id.banner);

        leftVideo.setOnClickListener(this);
        rightVideo.setOnClickListener(this);
        sw_refresh.setOnRefreshListener(this);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SlideModel imageData = bannerList.get(position);
                if(imageData.getUrl() != null && imageData.getTitle() != null){
                    WebViewActivity.openH5Activity(getContext(),true,imageData.getTitle(), imageData.getUrl());
                }
            }
        });
    }

    @Override
    protected void initDate(View view) {

        requestGetData();
    }

    @Override
    protected void initSet(View view) {
        msgPageTopBar.setTitle("视频聊");
        videoPlayAllPush.setOnClickListener(this);

        leftVideo.setVideoDoClick(new VideoDoClick() {
            @Override
            public void refreshVideo(RelativeLayout view) {
            }
        });

        setVideoView(leftVideo,FIRST_VIDEO);
        setVideoView(rightVideo,TREE_VIDEO);
    }



    @Override
    protected void initDisplayData(View view) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.videoplay_all_push:

                refreshVideo();
                break;

            case R.id.left_view:

                if(tabLiveListModelFirst != null){
                    Common.callVideo(getActivity(),tabLiveListModelFirst.getId(),0);
                }
                break;

            case R.id.right_view:
                if(tabLiveListModelTree != null){
                    Common.callVideo(getActivity(),tabLiveListModelTree.getId(),0);
                }
                break;
            default:
                break;
        }
    }

    private void refreshVideo() {
        leftVideo.showSearchMasking();
        rightVideo.showSearchMasking();
        requestNewOidByAll(ALL_VIDEO,2);
    }

    private void requestGetData() {

        Api.doRequestGetVideoChatPageData(new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestDoGetVideoChatPageInfo requestObj = (JsonRequestDoGetVideoChatPageInfo) JsonRequestBase.getJsonObj(s,JsonRequestDoGetVideoChatPageInfo.class);
                if (requestObj.getCode() == 1){
                    bannerList.clear();
                    bannerList.addAll(requestObj.getSlide());
                    refreshRollView();
                }else{
                    showToastMsg(getContext(),requestObj.getMsg());
                }
            }
        });
    }

    /**
     * 获取主播视频列表
     */
    private void requestNewOidByAll(final int type, final int count) {

        Api.doGetVideoList(uId,uToken,count,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                sw_refresh.setRefreshing(false);

                JsonRequestGetVideoLive jsonObj = JsonRequestGetVideoLive.getJsonObj(s);
                if(jsonObj.getCode() == 1){
                    List<TabLiveListModel> list = jsonObj.getList();
                    if(list.size() == 1){
                        if(ALL_VIDEO == type){
                            displayVideo(FIRST_VIDEO ,list.get(0));
                        }else{
                            displayVideo(type ,list.get(0));
                        }
                    }else if(list.size() == 2){
                        displayVideo(FIRST_VIDEO,list.get(0));
                        displayVideo(TREE_VIDEO,list.get(1));
                    }else{
                        LogUtils.i("视频数量:" +list.size());
                    }
                    ToastUtils.showShort("刷新数据成功");
                }else{
                    ToastUtils.showLong(jsonObj.getMsg());
                }

            }
        });
    }



    /**
     * 设置监听
     * @param videoView
     */
    private void setVideoView(final VideoDoFragment videoView,final int type){

        //关注监听
        videoView.setLoveBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id;
                if(type == FIRST_VIDEO && tabLiveListModelFirst != null){
                    id = tabLiveListModelFirst.getId();

                }else if(type == TREE_VIDEO && tabLiveListModelTree != null){
                    id = tabLiveListModelTree.getId();
                }else{
                    return;
                }

                Api.doLoveTheUser(
                        id,
                        uId,
                        uToken,
                        new StringCallback() {

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                JsonRequest requestObj = JsonRequest.getJsonObj(s);
                                if (requestObj.getCode() == 1){
                                    videoView.hideLoveBtn();
                                    showToastMsg(getContext(),"关注成功!");
                                }else{
                                    log("关注用户:"+requestObj.getMsg());
                                }
                            }
                        }
                );
            }
        });

        videoView.setVideoDoClick(new VideoDoClick() {
            @Override
            public void refreshVideo(RelativeLayout view) {

                if(type == FIRST_VIDEO){
                    leftVideo.showSearchMasking();

                }else if(type == TREE_VIDEO){
                    rightVideo.showSearchMasking();

                }
                requestNewOidByAll(type,1);
            }
        });

    }

    //显示视频
    private void displayVideo(int type,TabLiveListModel model) {

        switch (type){

            case FIRST_VIDEO:
                tabLiveListModelFirst = model;
                leftVideo.initDisplay(getContext(),model);
                break;

            case TREE_VIDEO:

                tabLiveListModelTree = model;
                rightVideo.initDisplay(getContext(),model);
                break;

            default:
                break;
        }
    }


    /**
     * 刷新轮播
     */
    private void refreshRollView(){

        //设置图片加载器
        mBanner.setImageLoader(new SlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerList);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @Override
    public void onRefresh() {
        refreshVideo();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestNewOidByAll(ALL_VIDEO,2);//请求参数
        onChangePageStatus(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        onChangePageStatus(true);
    }

    private void onChangePageStatus(boolean isBackground){
        if(leftVideo != null && rightVideo != null){

            leftVideo.runInBackground(isBackground);
            rightVideo.runInBackground(isBackground);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(leftVideo != null && rightVideo != null){
            leftVideo.onDestroy();
            rightVideo.onDestroy();
        }

    }

}
