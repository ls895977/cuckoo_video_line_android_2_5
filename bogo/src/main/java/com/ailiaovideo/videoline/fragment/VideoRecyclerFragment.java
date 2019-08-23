package com.ailiaovideo.videoline.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ailiaovideo.videoline.ApiConstantDefine;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecyclerVideoSmallAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestsVideo;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.ui.CuckooVideoTouchPlayerActivity;
import com.ailiaovideo.videoline.ui.VideoPlayerActivity;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.qcloud.sdk.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 小视频通用列表,根据type的不同刷新不同的数据
 */
public class VideoRecyclerFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    //用来标识视频列表类型
    private String type;
    //下拉刷新
    private SwipeRefreshLayout waveSwipeRefresh;
    private RecyclerView recommendRecycler;
    private GridLayoutManager mLayoutManager;
    private RecyclerVideoSmallAdapter recommendAdapter;
    private int page = 1;
    //视频
    private ArrayList<VideoModel> videos = new ArrayList<>();

    /**
     * 标识数据的刷新方式
     */
    public enum Refresh {
        DEFAULT,//初始数据
        UP,//上拉刷新
        DOWN//下拉刷新
    }

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    protected void initView(View view) {
        waveSwipeRefresh = view.findViewById(R.id.wave_swipe_refresh);
        recommendRecycler = view.findViewById(R.id.recommend_recycler);

        //创建网格布局GridLayoutManager
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recommendRecycler.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recommendRecycler.setHasFixedSize(true);
        //设置下拉刷新
        waveSwipeRefresh.setOnRefreshListener(this);

        //创建并设置Adapter
        recommendAdapter = new RecyclerVideoSmallAdapter(videos);
        //设置适配器
        recommendRecycler.setAdapter(recommendAdapter);
        //adapter监听
        recommendAdapter.setOnItemClickListener(this);
        recommendAdapter.setOnLoadMoreListener(this, recommendRecycler);
        recommendAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    protected void initDate(View view) {

    }

    @Override
    protected void initSet(View view) {


    }

    @Override
    protected void initDisplayData(View view) {
    }

    @Override
    public void fetchData() {

        //加载数据源
        getData(Refresh.DEFAULT);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        //加载假数据源
        getData(Refresh.DOWN);
        //视角移动到顶端
        recommendRecycler.scrollToPosition(0);
    }


    @Override
    public void onLoadMoreRequested() {

        if (Utils.isHasNextPage(videos.size())) {
            page++;
            //加载数据源
            getData(Refresh.DEFAULT);
        } else {
            recommendAdapter.loadMoreEnd();
        }
    }

    /**
     * 获取数据
     *
     * @param refresh 刷新数据的方式/初始化数据-下拉刷新-上拉刷新
     */
    private void getData(Refresh refresh) {

        if (ApiUtils.VideoType.reference.equals(type)) {
            //推荐视频
            getRecommendData(refresh);
        } else if (ApiUtils.VideoType.attention.equals(type)) {
            //关注
            getLoveData(refresh);
        } else if (ApiUtils.VideoType.latest.equals(type)) {
            //最新
            getNewData(refresh);
        } else if (ApiUtils.VideoType.near.equals(type)) {
            //附近
            getNearByData(refresh);
        }
    }

    //获取推荐页数据
    private void getRecommendData(Refresh refresh) {
        if (refresh == Refresh.DEFAULT) {
            requestVideoData(null, null);
        } else {
            requestVideoData(null, null);
        }
    }

    //获取关注页数据
    private void getLoveData(Refresh refresh) {
        if (refresh == Refresh.DEFAULT) {
            requestVideoData(null, null);
        } else {
            requestVideoData(null, null);
        }
    }

    //获取最新页数据
    private void getNewData(Refresh refresh) {
        if (refresh == Refresh.DEFAULT) {
            requestVideoData(null, null);
        } else {
            requestVideoData(null, null);
        }
    }

    //获取附近数据
    private void getNearByData(Refresh refresh) {
        Map<String, String> location = MyApplication.getInstances().getLocation();
        if (refresh == Refresh.DEFAULT) {
            requestVideoData(location.get("lat"), location.get("lng"));
        } else {
            requestVideoData(location.get("lat"), location.get("lng"));
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Intent intent = new Intent(getContext(), CuckooVideoTouchPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_TYPE, type);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_LIST, videos);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_POS, position);
        intent.putExtra(CuckooVideoTouchPlayerActivity.VIDEO_LIST_PAGE, page);

        startActivity(intent);
    }


    /**
     * 获取视频数据
     *
     * @param lat 维度
     * @param lng 经度
     */
    private void requestVideoData(String lat, String lng) {
        Api.getVideoPageList(
                uId,
                uToken,
                type,
                page,
                lat,
                lng,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        waveSwipeRefresh.setRefreshing(false);
                        JsonRequestsVideo requestObj = JsonRequestsVideo.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            if (page == 1) {
                                videos.clear();
                            }

                            if (requestObj.getData().size() == 0) {
                                recommendAdapter.loadMoreEnd();
                            } else {
                                recommendAdapter.loadMoreComplete();
                            }

                            videos.addAll(requestObj.getData());
                            if (page == 1) {
                                recommendAdapter.setNewData(videos);
                            } else {
                                recommendAdapter.notifyDataSetChanged();
                            }
                        } else {
                            recommendAdapter.loadMoreComplete();
                        }
                    }
                }
        );
    }


    /**
     * 设置视频列表类型
     *
     * @param type 类型值
     */
    public void setType(String type) {
        this.type = type;
    }
}
