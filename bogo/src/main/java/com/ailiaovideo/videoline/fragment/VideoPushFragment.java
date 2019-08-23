package com.ailiaovideo.videoline.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestDoLoveTheUser;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.OnlineUserAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.json.JsonRequestGetOnlineUser;
import com.ailiaovideo.videoline.json.JsonRequestUpdateLiveHeart;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.modle.OnlineUserModel;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.BGTimedTaskManage;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 魏鹏 on 2018/3/26.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class VideoPushFragment extends BaseFragment implements BGTimedTaskManage.BGTimeTaskRunnable, KSYStreamer.OnErrorListener {


    private GLSurfaceView glSurfaceView;
    private RecyclerView mRvContentList;
    private Button mBtnChange;
    private TextView mTvOnlineUserCount;


    private List<OnlineUserModel> onlineUserModels = new ArrayList<>();
    private KSYStreamer mStreamer;

    private BGTimedTaskManage liveHeartRunnable;
    private boolean mVideoPublish = false;
    private OnlineUserAdapter onlineUserAdapter;

    private String pushUrl = "";
    private View view;


    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {

        if(view == null){
            view = inflater.inflate(R.layout.fragment_video_push,null);
        }
        return view;
    }

    @Override
    protected void initView(View view) {
        mRvContentList = view.findViewById(R.id.rv_content_list);
        glSurfaceView = view.findViewById(R.id.video_view);
        mBtnChange = view.findViewById(R.id.btn_change);
        mTvOnlineUserCount = view.findViewById(R.id.tv_online_count);
        mBtnChange.setOnClickListener(this);

        mRvContentList.setLayoutManager(new GridLayoutManager(getContext(),3));

        onlineUserAdapter = new OnlineUserAdapter(getContext(),onlineUserModels);
        mRvContentList.setAdapter(onlineUserAdapter);
        onlineUserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Common.callVideo(getActivity(),onlineUserModels.get(position).getId(),0);
            }
        });

        onlineUserAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()){
                    case R.id.tv_follow:
                        followUser(position);
                        break;
                    default:
                        break;
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

        initLive();
    }

    @Override
    protected void initDisplayData(View view) {

    }

    private void initLive() {

        //设置刷新心跳任务
        liveHeartRunnable = new BGTimedTaskManage();
        liveHeartRunnable.setTime(StringUtils.toInt(RequestConfig.getConfigObj().getTabLiveHeartTime(),30) * 1000);
        liveHeartRunnable.setTimeTaskRunnable(this);

        if(mStreamer == null){
            // 创建KSYStreamer实例
            mStreamer = new KSYStreamer(getContext());
            // 设置预览View
            mStreamer.setDisplayPreview(glSurfaceView);
            mStreamer.setOnErrorListener(this);
            mStreamer.setVoiceVolume(0f);
           //mStreamer.setUrl(pushUrl);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){

            LogUtils.i("娱乐场视频页面可见");

            startPushStream();
            if(liveHeartRunnable != null){
                liveHeartRunnable.startRunnable(true);
            }

        }else{
            if(mStreamer != null){
                mStreamer.stopStream();
            }
            if(liveHeartRunnable != null){
                liveHeartRunnable.stopRunnable();

            }
            mVideoPublish = false;
        }
    }

    private void startPushStream() {
        LogUtils.i("推流地址:" + pushUrl);

        if(!mVideoPublish){
            if(mStreamer != null && !mStreamer.isRecording() && !TextUtils.isEmpty(pushUrl)){
                mStreamer.startStream();
                mVideoPublish = true;
            }
            //mStreamer.startStream();
            LogUtils.i("开始推流:" + pushUrl);
        }
    }


    private void stopPublishRtmp() {

        if(mStreamer != null){
            mStreamer.stopStream();
            mStreamer.stopBgm();
            mStreamer.stopCameraPreview();
        }
    }


    private void followUser(final int position) {
        String followId = onlineUserModels.get(position).getId();
        Api.doLoveTheUser(
                followId,
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getContext();
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestDoLoveTheUser requestObj = (JsonRequestDoLoveTheUser) JsonRequestDoLoveTheUser.getJsonObj(s,JsonRequestDoLoveTheUser.class);
                        if (requestObj.getCode() == 1){

                            onlineUserModels.get(position).setIs_follow("1");
                            showToastMsg(getContext(),"关注成功!");
                            onlineUserAdapter.notifyDataSetChanged();

                        }
                    }
                }
        );
    }

    //刷新直播心跳时间
    private void doRefreshLiveHeart() {

        Api.doUpdateTabLiveHeart(uId,uToken,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestUpdateLiveHeart jsonObj = JsonRequestUpdateLiveHeart.getJsonObj(s);
                if(jsonObj.getCode() == 1){

                    if(TextUtils.isEmpty(pushUrl)){
                        pushUrl = jsonObj.getPush_url();
                        mStreamer.setUrl(pushUrl);
                    }
                    startPushStream();
                }else{
                    mStreamer.stopStream();
                }
            }
        });
    }

    //随机获取在线用户
    private void requestGetData() {

        Api.doGetOnlineUser(uId,uToken,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetOnlineUser jsonObj = (JsonRequestGetOnlineUser) JsonRequestGetOnlineUser.getJsonObj(s,JsonRequestGetOnlineUser.class);
                if(jsonObj.getCode() == 1){

                    if(jsonObj.getList().size() == 0){
                        //showToastMsg(getContext(),"暂无可推荐用户!");
                    }
                    mTvOnlineUserCount.setText(String.format(Locale.CHINA,"推荐用户(%s人在线)",jsonObj.getOnline_count()));
                    onlineUserModels.clear();
                    onlineUserModels.addAll(jsonObj.getList());
                    onlineUserAdapter.notifyDataSetChanged();

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mStreamer.onResume();
        mStreamer.startCameraPreview();

    }

    @Override
    public void onPause() {
        super.onPause();
        mStreamer.onPause();
        mStreamer.stopCameraPreview();
    }

    @Override
    public void onRunTask() {

        doRefreshLiveHeart();
        LogUtils.i("刷新视频直播心跳时间");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change:

                requestGetData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(int i, int i1, int i2) {
        LogUtils.i("推流error:" + i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopPublishRtmp();
        if(mStreamer != null){
            mStreamer.release();
        }
    }
}
