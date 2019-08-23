package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequestTarget;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 发起视频通话
 * Created by fly on 2017/12/25 0025.
 */

public class CallPlayerActivity extends BaseActivity {
    //数据
    private CircleImageView callPlayerImg;//player头像信息  !
    private TextView callPlayerName;//标题
    private TextView repulseCallMsg;//拒绝/取消
    private FrameLayout callPlayerVideo;//本地图像

    private String channelName;//聊天通道
    private int videoUid;//聊天uid

    //创建 RtcEngine 对象
    private RtcEngine callRtcEngine;

    @Override
    protected Context getNowContext() {
        return CallPlayerActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_call_player;
    }

    @Override
    protected void initView() {
        callPlayerImg = findViewById(R.id.call_player_img);
        callPlayerName = findViewById(R.id.call_player_name);
        repulseCallMsg = findViewById(R.id.repulse_call_msg);
        callPlayerVideo = findViewById(R.id.call_player_video);
    }

    @Override
    protected void initSet() {
        setOnclickListener(R.id.repulse_call_btn);
        initAgoraEngineAndJoinChannel();
        callRtcEngine.joinChannel(null, channelName, null, videoUid);//发起会话
        requestCallPlayer();//请求服务器
    }

    @Override
    protected void initData() {
        requestTarUser();
        channelName = Utils.getUUID();
        videoUid = (int)(new Date().getTime());
    }

    private void requestTarUser() {
        Api.getUserData(
                getIntent().getStringExtra("str"),
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JsonRequestTarget requestObj = JsonRequestTarget.getJsonObj(s);
                        if (requestObj.getCode() == 1){
                            TargetUserData targetUserData = requestObj.getData();
                            if (ApiUtils.isTrueUrl(targetUserData.getAvatar())){

                                Utils.loadHttpImg(CallPlayerActivity.this,Utils.getCompleteImgUrl(targetUserData.getAvatar()),callPlayerImg);
                            }
                            callPlayerName.setText(targetUserData.getUser_nickname());
                        }else{
                            showToastMsg("获取当前视频主播信息:"+requestObj.getMsg());
                        }
                    }
                }
        );
    }

    @Override
    protected void initPlayerDisplayData() {
        concealView(R.id.accept_call);
        repulseCallMsg.setText(R.string.admin_no);
    }

    /**
     * 初始化设置视图
     */
    private void initAgoraEngineAndJoinChannel() {
        //初始化RtcEngine对象
        try {
            callRtcEngine = RtcEngine.create(getBaseContext(), RequestConfig.getConfigObj().getApp_qgorq_key(), new IRtcEngineEventHandler() {});
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        //初始化设置信息
        callRtcEngine.enableVideo();//打开视频模式
        callRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_480P, false);//设置视频分辨率

        //创建视频渲染视图, 设置本地视频视图##初始化本地视图
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        callPlayerVideo.addView(surfaceView);
        callRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, 0));
    }

    /////////////////////////////////////////监听事件处理/////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.repulse_call_btn:
                finish();
                break;
        }
    }

    //////////////////////////////////////////业务处理///////////////////////////////////////////////
    /**
     * 服务器请求拨打电话通知
     */
    private void requestCallPlayer() {
        Api.getPushCallVideo(
                uId,
                uToken,
                getIntent().getStringExtra("str"),
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                    }
                }
        );
    }
}
