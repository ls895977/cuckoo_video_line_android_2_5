package com.ailiaovideo.videoline.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ailiaovideo.chat.model.TextMessage;
import com.ailiaovideo.videoline.adapter.VideoChatAdapter;
import com.ailiaovideo.videoline.event.EventPrivateChatMessage;
import com.ailiaovideo.videoline.inter.MsgDialogClick;
import com.ailiaovideo.videoline.json.JsonDoRequestGetOssInfo;
import com.ailiaovideo.videoline.json.JsonRequestTarget;
import com.ailiaovideo.videoline.modle.VideoChatModel;
import com.ailiaovideo.videoline.utils.FileUtil;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.chat.model.CustomMessage;
import com.ailiaovideo.chat.model.Message;
import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.business.CuckooVideoLineTimeBusiness;
import com.ailiaovideo.videoline.dialog.GiftBottomDialog;
import com.ailiaovideo.videoline.event.EImOnCloseVideoLine;
import com.ailiaovideo.videoline.event.EImVideoCallEndMessages;
import com.ailiaovideo.videoline.event.EImOnPrivateMessage;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequest;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoPrivateSendGif;
import com.ailiaovideo.videoline.json.JsonRequestVideoEndInfo;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.GiftAnimationModel;
import com.ailiaovideo.videoline.modle.UserChatData;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsg;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsgPrivateGift;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.BGTimedTaskManage;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.GiftAnimationContentView;
import com.lzy.okgo.callback.StringCallback;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.droid.rtc.QNBeautySetting;
import com.qiniu.droid.rtc.QNCameraSwitchResultCallback;
import com.qiniu.droid.rtc.QNCaptureVideoCallback;
import com.qiniu.droid.rtc.QNCustomMessage;
import com.qiniu.droid.rtc.QNErrorCode;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNRTCEngineEventListener;
import com.qiniu.droid.rtc.QNRTCSetting;
import com.qiniu.droid.rtc.QNRenderVideoCallback;
import com.qiniu.droid.rtc.QNRoomState;
import com.qiniu.droid.rtc.QNStatisticsReport;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.QNTrackInfo;
import com.qiniu.droid.rtc.QNTrackKind;
import com.qiniu.droid.rtc.QNUtil;
import com.qiniu.droid.rtc.QNVideoFormat;
import com.qiniu.droid.rtc.model.QNAudioDevice;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.presentation.event.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.webrtc.VideoFrame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiRotation;
import cn.tillusory.tiui.TiPanelLayout;
import cn.tillusory.sdk.bean.TiRotation;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 山东布谷鸟网络科技有限公司
 * 视频通话页面
 */

public class VideoLineActivity extends BaseActivity implements GiftBottomDialog.DoSendGiftListen, CuckooVideoLineTimeBusiness.VideoLineTimeBusinessCallback, QNRTCEngineEventListener, QNRenderVideoCallback {


    //标记
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;


    public static final String IS_BE_CALL = "IS_BE_CALL";
    public static final String IS_NEED_CHARGE = "IS_NEED_CHARGE";
    public static final String VIDEO_DEDUCTION = "VIDEO_DEDUCTION";
    public static final String CALL_TYPE = "CALL_TYPE";
    public static final String ROOM_TOKEN = "ROOM_TOKEN";

    @BindView(R.id.video_chat_small_bac)
    FrameLayout smallVideoViewBac;//本地视图bac

    @BindView(R.id.video_chat_big_bac)
    FrameLayout bigVideoViewBac;//本地视图bac

    @BindView(R.id.local_video_surface_view)
    QNSurfaceView mLocalVideoSurfaceView;

    @BindView(R.id.remote_video_surface_view)
    QNSurfaceView mRemoteVideoSurfaceView;

    @BindView(R.id.close_video_chat)
    ImageView closeVideo;//关闭按钮

    @BindView(R.id.videochat_switch)
    ImageView cutCamera;//切换摄像头

    @BindView(R.id.videochat_voice)
    ImageView isSoundOut;//是否关闭声音

    @BindView(R.id.videochat_gift)
    ImageView videoGift;//礼物按钮

    //动画布局
    @BindView(R.id.ll_gift_content)
    GiftAnimationContentView mGiftAnimationContentView;

    //信息
    @BindView(R.id.videochat_unit_price)
    TextView chatUnitPrice;//收费金额

    @BindView(R.id.videochat_timer)
    Chronometer videoChatTimer;//通话计时时长

    //用户信息
    @BindView(R.id.this_player_img)
    CircleImageView headImage;//头像

    @BindView(R.id.this_player_number)
    TextView thisPlayerNumber;//关注数

    @BindView(R.id.this_player_name)
    TextView nickName;//昵称

    @BindView(R.id.this_player_loveme)
    TextView thisPlayerLoveme;//关注按钮

    @BindView(R.id.tv_time_info)
    TextView tv_time_info;

    @BindView(R.id.tv_reward)
    TextView tv_reward;

    @BindView(R.id.user_coin)
    TextView tv_userCoin;

    @BindView(R.id.rv_content_msg_list)
    RecyclerView rv_content_msg_list;

    @BindView(R.id.et_input_chat)
    EditText et_input_chat;

    @BindView(R.id.rl_input)
    RelativeLayout rl_input;

    @BindView(R.id.rl_root)
    RelativeLayout rl_root;

    @BindView(R.id.fl_remote_video_view)
    FrameLayout fl_remote_video_view;

    @BindView(R.id.fl_local_video_view)
    FrameLayout fl_local_video_view;

    private ImageView iv_close_camera;
    private ImageView iv_lucky;

    private UserChatData chatData;
    //创建 RtcEngine 对象
    //private RtcEngine mRtcEngine;// Tutorial Step 1

    //是否需要扣费
    private boolean isNeedCharge = false;
    private GiftBottomDialog giftBottomDialog;

    private int videoViewStatus = -1;
    private TIMConversation conversation;

    private BGTimedTaskManage getVideoTimeInfoTask;
    private CuckooVideoLineTimeBusiness cuckooVideoLineTimeBusiness;

    private VideoChatAdapter videoChatAdapter;

    private QNRTCEngine mEngine;
    private boolean isOpenCamera = true;
    private int callType;
    //分钟扣费金额
    private String videoDeduction = "";
    //是否是被呼叫用户
    private boolean isBeCall = false;

    private List<VideoChatModel> videoChatModelList = new ArrayList<>();
    //截屏时间间隔时长单位s
    private long screenshotTime = 20 * 1000;
    //上一次截屏时间
    private long lastScreenshotTime;

    private String roomToken;
    private JsonDoRequestGetOssInfo ossInfo;


    /*---------------------------美颜SDK-------------*/
    private TiSDKManager tiSDKManager;
    private boolean isFrontCamera = true;
    private QNTrackInfo remoteTrackInfo;
    private boolean setting_permission;
    /*---------------------------美颜SDK-------------*/


    @Override
    protected Context getNowContext() {
        return VideoLineActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_video_chat;
    }

    @Override
    protected void initView() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        isBeCall = getIntent().getBooleanExtra(IS_BE_CALL, false);
        isNeedCharge = getIntent().getBooleanExtra(IS_NEED_CHARGE, false);
        roomToken = getIntent().getStringExtra(ROOM_TOKEN);
        chatData = getIntent().getParcelableExtra("obj");
        //video_px = getIntent().getStringExtra("video_px");

        iv_close_camera = findViewById(R.id.iv_close_camera);
        iv_lucky = findViewById(R.id.videochat_lucky_corn);
        iv_lucky.setOnClickListener(this);

        mGiftAnimationContentView.startHandel();

        //通话类型
        callType = getIntent().getIntExtra(CALL_TYPE, 0);
        //分钟扣费金额
        videoDeduction = getIntent().getStringExtra(VIDEO_DEDUCTION);
        chatUnitPrice.setText(String.format(Locale.getDefault(), "%s%s/分钟", videoDeduction, RequestConfig.getConfigObj().getCurrency()));
        videoChatTimer.setTextColor(getResources().getColor(R.color.white));

        //开始计时
        videoChatTimer.start();

        //私聊adapter
        videoChatAdapter = new VideoChatAdapter(videoChatModelList);
        rv_content_msg_list.setLayoutManager(new LinearLayoutManager(this));
        rv_content_msg_list.setAdapter(videoChatAdapter);


        tv_time_info.setText(getString(R.string.video_line_charge) + videoDeduction);
        tv_reward.setText("礼物打赏:0");
        tv_userCoin.setText("对方余额:0");

        videoGift.setVisibility(View.VISIBLE);
        chatUnitPrice.setVisibility(View.GONE);

        //是否是用户
        if (!isNeedCharge) {
            //礼物和打赏收入
            tv_time_info.setVisibility(View.VISIBLE);
            tv_reward.setVisibility(View.VISIBLE);
            tv_userCoin.setVisibility(View.VISIBLE);
        }

        setOnclickListener(isSoundOut, closeVideo, cutCamera, videoGift, headImage, thisPlayerLoveme, iv_close_camera);

        tiSDKManager = new TiSDKManager();
        addContentView(new TiPanelLayout(this).init(tiSDKManager),
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    @Override
    protected void initData() {

        cuckooVideoLineTimeBusiness = new CuckooVideoLineTimeBusiness(this, isNeedCharge, chatData.getUserModel().getId(), this);

        conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatData.getUserModel().getId());

        //实时获取收益消费信息
        getVideoTimeInfoTask = new BGTimedTaskManage();
        //这里不让60s刷新一次了
        getVideoTimeInfoTask.setTime(10 * 6000);
        getVideoTimeInfoTask.setTimeTaskRunnable(new BGTimedTaskManage.BGTimeTaskRunnable() {
            @Override
            public void onRunTask() {
                requestGetVideoCallTimeInfo();
            }
        });

        getVideoTimeInfoTask.startRunnable(false);

        String msgAlert = ConfigModel.getInitData().getVideo_call_msg_alert();
        if (!TextUtils.isEmpty(msgAlert)) {
            ToastUtils.showLong(msgAlert);
        }

        //初始化截图时间
        screenshotTime = ConfigModel.getInitData().getScreenshots_time() * 1000;

        getUploadOssSign();
        requestUserData();
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    protected void initSet() {
        //初始化本地操作
        if (callType == 0) {
            initQiniuCloudSDK();
        } else {
            iv_close_camera.setVisibility(View.GONE);
        }
        checkPermissionStatus();
    }


    private static final int MY_PERMISSIONS_REQUEST = 0;

    //检查权限
    private void checkPermissionStatus() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
            setting_permission = true;
        }
    }


    private void initQiniuCloudSDK() {

        //只针对直播进行截屏鉴黄操作
        if (!isNeedCharge) {
            mLocalVideoSurfaceView.setRenderVideoCallback(this);
        }

        mLocalVideoSurfaceView.setZOrderMediaOverlay(true);
        QNRTCSetting setting = new QNRTCSetting();
        // 配置默认摄像头 ID，此处配置为前置摄像头
        setting.setCameraID(QNRTCSetting.CAMERA_FACING_ID.FRONT);

        // 配置音视频数据的编码方式，此处配置为硬编
        setting.setHWCodecEnabled(true);

        // 分辨率、码率、帧率配置为 640x480、15fps、400kbps
        setting.setVideoPreviewFormat(new QNVideoFormat(640, 480, 15))
                .setVideoEncodeFormat(new QNVideoFormat(640, 480, 15))
                .setVideoBitrate(400 * 1000);

        // 分辨率、码率、帧率配置为 352x288、15fps、300kbps
        //  setting.setVideoPreviewFormat(new QNVideoFormat(352, 288, 15))
        //          .setVideoEncodeFormat(new QNVideoFormat(352, 288, 15))
        //          .setVideoBitrate(300 * 1000);

        // 分辨率、码率、帧率配置为 960x544、15fps、700kbps
        // setting.setVideoPreviewFormat(new QNVideoFormat(960, 544, 15))
        //          .setVideoEncodeFormat(new QNVideoFormat(960, 544, 15))
        //          .setVideoBitrate(700 * 1000);

        // 分辨率、码率、帧率配置为 1280x720、15fps、1000kbps
        //  setting.setVideoPreviewFormat(new QNVideoFormat(1280, 720, 15))
        //          .setVideoEncodeFormat(new QNVideoFormat(1280, 720, 15))
        //          .setVideoBitrate(1000 * 1000);

        // 以上为我们的推荐配置组合，可结合实际情况选用，若需使用多 Track 可直接在创建 Track 时分开单独配置，即：
        // QNTrackInfo track = mEngine.createTrackInfoBuilder()
        //         .setVideoPreviewFormat(new QNVideoFormat(640, 480, 15))
        //         .setBitrate(400 * 1000)
        //         .setSourceType(QNSourceType.VIDEO_CAMERA)
        //         .create();

        // 创建 QNRTCEngine
        mEngine = QNRTCEngine.createEngine(getApplicationContext(), setting);

//        QNBeautySetting beautySetting = new QNBeautySetting(0.5f, 0.5f, 0.5f);
//        beautySetting.setEnable(true);
//        mEngine.setBeauty(beautySetting);

        mEngine.setCaptureVideoCallBack(new QNCaptureVideoCallback() {
            @Override
            public void onCaptureStarted() {

            }

            @Override
            public void onRenderingFrame(VideoFrame.TextureBuffer textureBuffer, long l) {

                int tex = tiSDKManager.renderTexture2D(
                        textureBuffer.getTextureId(),
                        textureBuffer.getWidth(),
                        textureBuffer.getHeight(),
                        isFrontCamera ? TiRotation.CLOCKWISE_ROTATION_270 : TiRotation.CLOCKWISE_ROTATION_90,
                        isFrontCamera);
                textureBuffer.setTextureId(tex);
            }

            @Override
            public void onPreviewFrame(byte[] bytes, int width, int height, int rotation, int fmt, long timestampNs) {
            }

            @Override
            public void onCaptureStopped() {
                tiSDKManager.destroy();
            }
        });
        // 设置回调监听
        mEngine.setEventListener(this);

        // 设置预览窗口
        mEngine.setCapturePreviewWindow(mLocalVideoSurfaceView);

        // 加入房间，加入房间过程中会触发 QNRTCEngineEventListener#onRoomStateChanged 回调
        //一加
        mEngine.joinRoom(roomToken);
        //小米
        //mEngine.joinRoom("fe-37HEsiNTNA-6qSIB57ZTPBuy-FNdrUEpe8SZM:aBR-DtVpyGW-7N9Tkn8BVyHh_Lc=:eyJhcHBJZCI6ImU5c2JlNXZseiIsInJvb21OYW1lIjoiY2VzaGkiLCJ1c2VySWQiOiIxMjMiLCJleHBpcmVBdCI6MTU2MTA5ODcxMCwicGVybWlzc2lvbiI6InVzZXIifQ==");

        mLocalVideoSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSwitchView();
            }
        });
    }

    private void clickSwitchView() {
        if (videoViewStatus == -1) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLocalVideoSurfaceView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            mLocalVideoSurfaceView.setLayoutParams(params);

            FrameLayout.LayoutParams paramsRemote = (FrameLayout.LayoutParams) mRemoteVideoSurfaceView.getLayoutParams();
            paramsRemote.width = ConvertUtils.dp2px(150);
            paramsRemote.height = ConvertUtils.dp2px(200);
            mRemoteVideoSurfaceView.setLayoutParams(paramsRemote);
            mLocalVideoSurfaceView.setZOrderMediaOverlay(false);
            mRemoteVideoSurfaceView.setZOrderMediaOverlay(true);

            fl_local_video_view.removeAllViews();
            fl_remote_video_view.removeAllViews();

            fl_remote_video_view.addView(mLocalVideoSurfaceView);
            fl_local_video_view.addView(mRemoteVideoSurfaceView);
            videoViewStatus = 1;
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRemoteVideoSurfaceView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            mRemoteVideoSurfaceView.setLayoutParams(params);

            FrameLayout.LayoutParams paramsRemote = (FrameLayout.LayoutParams) mLocalVideoSurfaceView.getLayoutParams();
            paramsRemote.width = ConvertUtils.dp2px(150);
            paramsRemote.height = ConvertUtils.dp2px(200);
            mLocalVideoSurfaceView.setLayoutParams(paramsRemote);
            mRemoteVideoSurfaceView.setZOrderMediaOverlay(false);
            mLocalVideoSurfaceView.setZOrderMediaOverlay(true);

            fl_local_video_view.removeAllViews();
            fl_remote_video_view.removeAllViews();

            fl_remote_video_view.addView(mRemoteVideoSurfaceView);
            fl_local_video_view.addView(mLocalVideoSurfaceView);
            videoViewStatus = -1;
        }

    }


    @OnClick({R.id.btn_send, R.id.iv_chat})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_chat:
                rl_input.setVisibility(View.VISIBLE);
                et_input_chat.requestFocus();
                KeyboardUtils.showSoftInput(et_input_chat);
                break;
            case R.id.btn_send:
                clickSendChat();
                break;
            case R.id.videochat_gift:
                clickOpenGiftDialog();
                break;
            case R.id.videochat_screen:
                //TODO:截屏
                break;
            case R.id.this_player_loveme:
                doLoveHer();
                break;
            case R.id.close_video_chat:
                doLogoutChat();
                break;
            case R.id.videochat_switch:
                doCutCamera();
                break;
            case R.id.videochat_voice:
                onLocalAudioMuteClicked();
                break;
            case R.id.this_player_img:
                Common.jumpUserPage(VideoLineActivity.this, chatData.getUserModel().getId());
                break;
            case R.id.iv_close_camera:
                closeCamera();
                break;
            case R.id.videochat_lucky_corn:
                Intent intent = new Intent(this, DialogH5Activity.class);
                intent.putExtra("uri", ConfigModel.getInitData().getApp_h5().getTurntable_url());
                startActivity(intent);
                break;
        }
    }

    /**
     * 余额不足操作
     */
    private void doBalance() {
        hangUpVideo();
        ToastUtils.showShort(R.string.money_insufficient);
    }

    private void clickSendChat() {
        final String content = et_input_chat.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("内容不能为空！");
            return;
        }

        Message message = new TextMessage(content);
        conversation.sendMessage(message.getMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表;
                ToastUtils.showShort("发送失败：" + desc + " code:" + code);
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                //发送消息成功,消息状态已在sdk中修改，此时只需更新界面
                MessageEvent.getInstance().onNewMessage(null);
                VideoChatModel videoChatModel = new VideoChatModel();
                videoChatModel.setContent("我：" + content);
                videoChatModelList.add(videoChatModel);
                videoChatAdapter.notifyDataSetChanged();
                rv_content_msg_list.scrollToPosition(videoChatAdapter.getItemCount() - 1);
                et_input_chat.setText("");

            }
        });
        //message对象为发送中状态
        MessageEvent.getInstance().onNewMessage(message.getMessage());
        rl_input.setVisibility(View.GONE);
        KeyboardUtils.hideSoftInput(this);
    }

    //开关摄像头
    private void closeCamera() {
        if (isOpenCamera) {
            //关闭摄像头
            smallVideoViewBac.setVisibility(View.GONE);
            iv_close_camera.setImageResource(R.mipmap.ic_close_camera);
            ToastUtils.showLong("摄像头已关闭");
        } else {
            smallVideoViewBac.setVisibility(View.VISIBLE);
            iv_close_camera.setImageResource(R.mipmap.ic_open_camera);
            ToastUtils.showLong("摄像头已打开");
        }

        isOpenCamera = !isOpenCamera;
        // mute 本地视频
        mEngine.muteLocalVideo(isOpenCamera);
    }

    //本地音频静音
    public void onLocalAudioMuteClicked() {
        if (isSoundOut.isSelected()) {
            isSoundOut.setSelected(false);
            isSoundOut.setImageResource(R.drawable.icon_call_unmute);
        } else {
            isSoundOut.setSelected(true);
            isSoundOut.setImageResource(R.drawable.icon_call_muted);
        }
        // mute 本地音频
        mEngine.muteLocalAudio(isSoundOut.isSelected());
    }

    /**
     * 执行切换前后相机
     */
    private void doCutCamera() {
        // 切换摄像头
        mEngine.switchCamera(new QNCameraSwitchResultCallback() {
            @Override
            public void onCameraSwitchDone(final boolean isFrontCamera) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        VideoLineActivity.this.isFrontCamera = isFrontCamera;
                        //button.setImageDrawable(isFrontCamera ? getResources().getDrawable(R.mipmap.camera_switch_front) : getResources().getDrawable(R.mipmap.camera_switch_end));
                    }
                });
            }

            @Override
            public void onCameraSwitchError(String errorMessage) {

            }
        });
    }


    //销毁操作
    private void leaveChannel() {
        // 离开房间
        mEngine.leaveRoom();
        // 释放资源
        mEngine.destroy();
    }

    //关闭通话
    private void hangUpVideo() {
        showLoadingDialog(getString(R.string.loading_huang_up));
        cuckooVideoLineTimeBusiness.doHangUpVideo();

        if (getVideoTimeInfoTask != null) {
            getVideoTimeInfoTask.stopRunnable();
        }
    }

    //礼物弹窗
    private void clickOpenGiftDialog() {
        if (giftBottomDialog == null) {

            giftBottomDialog = new GiftBottomDialog(this, chatData.getUserModel().getId());
            giftBottomDialog.setType(1);
            giftBottomDialog.setChanelId(chatData.getChannelName());
            giftBottomDialog.setDoSendGiftListen(this);
        }

        if (!isNeedCharge) {
            giftBottomDialog.hideMenu();
        }

        giftBottomDialog.show();
    }


    //添加礼物消息
    private void pushGiftMsg(CustomMsgPrivateGift giftCustom) {

        GiftAnimationModel giftAnimationModel = new GiftAnimationModel();

        giftAnimationModel.setUserAvatar(giftCustom.getSender().getAvatar());
        giftAnimationModel.setUserNickname(giftCustom.getSender().getUser_nickname());
        giftAnimationModel.setMsg(giftCustom.getFrom_msg());
        giftAnimationModel.setGiftIcon(giftCustom.getProp_icon());
        if (mGiftAnimationContentView != null) {
            mGiftAnimationContentView.addGift(giftAnimationModel);
        }
    }

    /**
     * 退出聊天
     */
    private void doLogoutChat() {
        DialogHelp.getConfirmDialog(this, getString(R.string.is_huang_call), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hangUpVideo();
            }
        }).show();
    }


    @Override
    protected void doLogout() {
        super.doLogout();
        leaveChannel();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVideoCallEndThread(EImVideoCallEndMessages var1) {

        LogUtils.i("收到消息一对一视频结束请求消息:" + var1.msg.getCustomMsg().getSender().getUser_nickname());

        try {
            CustomMsg customMsg = var1.msg.getCustomMsg();
            //showLiveLineEnd(1);
            hangUpVideo();
        } catch (Exception e) {
            LogUtils.i("收到消息一对一视频结束请求消息错误error" + e.getMessage());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCloseVideoEvent(EImOnCloseVideoLine var1) {

        DialogHelp.getMessageDialog(this, var1.customMsgCloseVideo.getMsg_content()).show();
        if (var1.customMsgCloseVideo.getAction() == 1) {
            return;
        }

        //后台结束通话警示信息
        hangUpVideo();
        LogUtils.i("收到后台关闭视频消息:" + var1.customMsgCloseVideo.getMsg_content());
    }
    

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPrivateGiftEvent(EImOnPrivateMessage var1) {

        pushGiftMsg(var1.customMsgPrivateGift);
        LogUtils.i("收到消息发送礼物消息:" + var1.customMsgPrivateGift.getFrom_msg());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPrivateMsgEvent(EventPrivateChatMessage var1) {

        VideoChatModel videoChatModel = new VideoChatModel();
        videoChatModel.setContent(chatData.getUserModel().getUser_nickname() + "：" + var1.getMsg());
        videoChatModelList.add(videoChatModel);
        videoChatAdapter.notifyDataSetChanged();
        rv_content_msg_list.scrollToPosition(videoChatAdapter.getItemCount() - 1);
        LogUtils.i("收到私信消息:" + var1.getMsg());

    }

    //赠送礼物
    @Override
    public void onSuccess(JsonRequestDoPrivateSendGif sendGif) {

        final CustomMsgPrivateGift gift = new CustomMsgPrivateGift();
        gift.fillData(sendGif.getSend());
        Message message = new CustomMessage(gift, LiveConstant.CustomMsgType.MSG_PRIVATE_GIFT);
        conversation.sendMessage(message.getMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                LogUtils.i("一对一视频礼物消息发送失败");
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {

                pushGiftMsg(gift);
                LogUtils.i("一对一视频礼物消息发送SUCCESS");
            }
        });
    }


    //实时获取的受益消费信息
    private void requestGetVideoCallTimeInfo() {

        Api.doRequestGetVideoCallTimeInfo(SaveData.getInstance().getId(), chatData.getChannelName(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestVideoEndInfo data = (JsonRequestVideoEndInfo) JsonRequestBase.getJsonObj(s, JsonRequestVideoEndInfo.class);
                if (StringUtils.toInt(data.getCode()) == 1) {
                    tv_time_info.setText("通话消费:" + data.getVideo_call_total_coin());
                    tv_reward.setText("礼物打赏:" + data.getGift_total_coin());
                    tv_userCoin.setText("对方余额:" + data.getUser_coin());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }


    /**
     * 关注目标主播
     */
    private void doLoveHer() {
        Api.doLoveTheUser(
                chatData.getUserModel().getId(),
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequest requestObj = JsonRequest.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            concealView(thisPlayerLoveme);//隐藏关注按钮
                            showToastMsg("关注成功!");
                        }
                    }
                }
        );
    }

    /**
     * 获取当前视频主播信息
     */
    private void requestUserData() {

        Api.getUserData(
                chatData.getUserModel().getId(),
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestTarget requestObj = JsonRequestTarget.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            TargetUserData targetUserData = requestObj.getData();
                            if (ApiUtils.isTrueUrl(targetUserData.getAvatar())) {
                                Utils.loadHttpImg(VideoLineActivity.this, Utils.getCompleteImgUrl(targetUserData.getAvatar()), headImage);
                            }
                            nickName.setText(targetUserData.getUser_nickname());
                            thisPlayerNumber.setText(getString(R.string.follow) + ": " + targetUserData.getAttention_all());
                            thisPlayerLoveme.setVisibility("0".equals(targetUserData.getAttention()) ? View.VISIBLE : View.GONE);

                            requestGetVideoCallTimeInfo();
                        } else {
                            showToastMsg("获取当前视频主播信息:" + requestObj.getMsg());
                        }
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        doLogoutChat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mEngine.startCapture();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //mEngine.stopCapture();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGiftAnimationContentView != null) {
            mGiftAnimationContentView.stopHandel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cuckooVideoLineTimeBusiness != null) {
            cuckooVideoLineTimeBusiness.stop();
        }

        if (getVideoTimeInfoTask != null) {
            getVideoTimeInfoTask.stopRunnable();
        }

        if (DialogH5Activity.instance != null) {
            DialogH5Activity.instance.finish();
        }

        // 需要及时销毁 QNRTCEngine 以释放资源
        leaveChannel();
        //todo ----- tillusory start -----
        //tiSDKManager.destroy();
        //todo ----- tillusory end -----
    }

    /**
     * 跳转视频结束页面
     */
    private void showLiveLineEnd(int isFabulous) {
        if (DialogH5Activity.instance != null) {
            DialogH5Activity.instance.finish();
        }
        Intent intent = new Intent(this, VideoLineEndActivity.class);
        intent.putExtra(VideoLineEndActivity.USER_HEAD, chatData.getUserModel().getAvatar());
        intent.putExtra(VideoLineEndActivity.USER_NICKNAME, chatData.getUserModel().getUser_nickname());
        intent.putExtra(VideoLineEndActivity.LIVE_LINE_TIME, videoChatTimer.getText());
        intent.putExtra(VideoLineEndActivity.LIVE_CHANNEL_ID, chatData.getChannelName());
        intent.putExtra(VideoLineEndActivity.IS_CALL_BE_USER, !isNeedCharge);
        intent.putExtra(VideoLineEndActivity.USER_ID, chatData.getUserModel().getId());
        intent.putExtra(VideoLineEndActivity.IS_FABULOUS, isFabulous);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCallbackChargingSuccess() {

    }

    @Override
    public void onCallbackNotBalance() {
        doBalance();
    }

    @Override
    public void onCallbackCallRecordNotFount() {

        showToastMsg("通话记录不存在");
        finishNow();
    }

    @Override
    public void onCallbackCallNotMuch(String msg) {
        DialogHelp.getConfirmDialog(VideoLineActivity.this, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RechargeActivity.startRechargeActivity(VideoLineActivity.this);
            }
        }).show();
    }

    @Override
    public void onCallbackEndVideo(String msg) {
        showToastMsg(msg);
        cuckooVideoLineTimeBusiness.doHangUpVideo();
    }

    @Override
    public void onHangUpVideoSuccess(int isFabulous) {
        hideLoadingDialog();
        showLiveLineEnd(isFabulous);
    }

    @Override
    public void onRoomStateChanged(QNRoomState qnRoomState) {
        switch (qnRoomState) {
            case CONNECTED:
                // 加入房间成功后发布音视频数据，发布成功会触发 QNRTCEngineEventListener#onLocalPublished 回调
                mEngine.publish();
                break;
        }
    }

    @Override
    public void onRemoteUserJoined(String s, String s1) {

    }

    @Override
    public void onRemoteUserLeft(String s) {

    }

    @Override
    public void onLocalPublished(List<QNTrackInfo> list) {
        mEngine.enableStatistics();
    }

    @Override
    public void onRemotePublished(String s, List<QNTrackInfo> list) {
    }

    @Override
    public void onRemoteUnpublished(String s, List<QNTrackInfo> list) {
    }

    @Override
    public void onRemoteUserMuted(String s, List<QNTrackInfo> trackInfoList) {
        for (QNTrackInfo track : trackInfoList) {
            if (track.getTrackKind().equals(QNTrackKind.VIDEO)) {
                if (track.isVideo()) {
                    bigVideoViewBac.setVisibility(View.GONE);
                } else {
                    bigVideoViewBac.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onSubscribed(String s, List<QNTrackInfo> trackInfoList) {
        // 筛选出视频 Track 以渲染到窗口
        for (QNTrackInfo track : trackInfoList) {
            if (track.getTrackKind().equals(QNTrackKind.VIDEO)) {
                remoteTrackInfo = track;
                // 设置渲染窗口
                mEngine.setRenderWindow(track, mRemoteVideoSurfaceView);
            }
        }
    }

    @Override
    public void onKickedOut(String s) {

    }

    @Override
    public void onStatisticsUpdated(QNStatisticsReport qnStatisticsReport) {

    }

    @Override
    public void onAudioRouteChanged(QNAudioDevice qnAudioDevice) {

    }

    @Override
    public void onCreateMergeJobSuccess(String s) {

    }

    @Override
    public void onError(int errorCode, String s) {
        switch (errorCode) {
            case QNErrorCode.ERROR_TOKEN_INVALID:
                // RoomToken 无效，建议重新获取 RoomToken 后再加入房间
                Toast.makeText(getApplicationContext(), "RoomToken Error", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_AUTH_FAIL:
                // RoomToken 鉴权失败，建议收到此错误代码时尝试重新获取 RoomToken 后再次加入房间。
                Toast.makeText(getApplicationContext(), "RoomToken Error", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_PUBLISH_FAIL:
                // 发布失败，建议收到此错误代码时检查当前连接状态并尝试重新发布
                if (QNRoomState.CONNECTED.equals(mEngine.getRoomState()) || QNRoomState.RECONNECTED.equals(mEngine.getRoomState())) {
                    mEngine.publish();
                }
                break;
            case QNErrorCode.ERROR_DEVICE_CAMERA:
                // 相机错误：打开失败、没有权限或者被其他程序占用等
                Toast.makeText(getApplicationContext(), "Camera Error", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_TOKEN_ERROR:
                // RoomToken 错误，建议收到此错误代码时尝试重新获取 RoomToken 后再次加入房间
                Toast.makeText(getApplicationContext(), "RoomToken Error", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_TOKEN_EXPIRED:
                // RoomToken 过期，建议重新获取 RoomToken 后再加入房间
                Toast.makeText(getApplicationContext(), "RoomToken Error", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_RECONNECT_TOKEN_ERROR:
                // SDK 重连时间过长，内部的重连 Token 已失效，建议重新加入房间
                Toast.makeText(getApplicationContext(), "RoomToken Error", Toast.LENGTH_SHORT).show();
                mEngine.joinRoom("");
                break;
            case QNErrorCode.ERROR_ROOM_CLOSED:
                // 房间已被管理员关闭
                Toast.makeText(getApplicationContext(), "Room closed by admin", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_ROOM_FULL:
                // 房间已超过限制，可在在七牛管理控制台中设置房间内最大人数
                Toast.makeText(getApplicationContext(), "Room is full", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_PLAYER_ALREADY_EXIST:
                // 用户已存在，可能是同一用户在其他设备加入了房间，可在七牛管理控制台中设置是否允许同一个身份用户重复加入房间
                Toast.makeText(getApplicationContext(), "Already login on other device", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_NO_PERMISSION:
                // 权限不足，一般在踢人、合流等需要权限的操作中会出现
                Toast.makeText(getApplicationContext(), "You can not do this operation", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_INVALID_PARAMETER:
                // 参数错误，一般在踢人、合流等操作中会出现，开发者检查业务代码逻辑
                Toast.makeText(getApplicationContext(), "Parameters error", Toast.LENGTH_SHORT).show();
                break;
            case QNErrorCode.ERROR_MULTI_MASTER_VIDEO_OR_AUDIO:
                // 一次通话中只能有一路 master 视频 Track 和一路 master 音频 Track，若超过这个数量则会触发此错误码
                Toast.makeText(getApplicationContext(), "Publish master track error", Toast.LENGTH_SHORT).show();
                break;
            default:
                // 除上面的错误码外其他是 SDK 内部会进行处理的错误码，可以直接忽略
                //Log.i(TAG, "errorCode = " + errorCode + " description = " + description);
        }
    }

    @Override
    public void onMessageReceived(QNCustomMessage qnCustomMessage) {

    }

    @Override
    public void onRenderingFrame(VideoFrame videoFrame) {
        if (lastScreenshotTime == 0) {
            lastScreenshotTime = System.currentTimeMillis();
            return;
        }

        //计算截屏间隔时间
        long intervalTime = (System.currentTimeMillis() - lastScreenshotTime);

//       LogUtils.i("截屏时间间隔：" + intervalTime + "lastScreenshotTime:" + lastScreenshotTime + "" +
//                " currentTimeMillis：" + System.currentTimeMillis());

        if (intervalTime > screenshotTime) {
            final String filePath = FileUtil.getSDRoot() + "/" + getPackageName() + "/save_img/";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }

            //重新赋值上一次的截屏时间为当前时间
            lastScreenshotTime = System.currentTimeMillis();

            final String imgFilePath = filePath + "save-img.jpeg";
            QNUtil.saveFrame(videoFrame, imgFilePath, new QNUtil.FrameSavedCallback() {
                @Override
                public void onSaveSuccess() {
                    LogUtils.i("截图保存成功");
                    uploadScreenshotImg(imgFilePath);
                }

                @Override
                public void onSaveError(String s) {
                    LogUtils.i("截图保存失败" + s);
                }
            });
        }
    }

    private boolean isUploadScreenshotImgSuccess = true;


    //获取云存储签名信息
    private void getUploadOssSign() {
        Api.doRequestGetOSSInfo(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                ossInfo = (JsonDoRequestGetOssInfo) JsonRequestBase.getJsonObj(s, JsonDoRequestGetOssInfo.class);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    //上传截屏图片
    private void uploadScreenshotImg(String filePath) {

        if (!isUploadScreenshotImgSuccess) {
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        //设置上传后文件的key
        String fileName = "screenshot_img_" + System.currentTimeMillis() + "_" + SaveData.getInstance().getId() + "_" + file.getName();

        final String upKey = LiveConstant.VIDEO_SCREEN_SHOT_IMG_DIR + fileName;

        String token = ossInfo.getToken();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, upKey, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        isUploadScreenshotImgSuccess = true;
                        String uploadImgUrl = ossInfo.getDomain() + "/" + upKey;
                        LogUtils.i("截图上传成功" + uploadImgUrl + "--------" + info.error);
                        //ToastUtils.showLong("截图上传成功" + uploadImgUrl);
                    }

                }, null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ToastUtils.showLong("拒绝存储权限，无法继续使用，请在设置中打开！");
                    hangUpVideo();
                }
                break;
            }

            default:
                break;

        }
    }

}
