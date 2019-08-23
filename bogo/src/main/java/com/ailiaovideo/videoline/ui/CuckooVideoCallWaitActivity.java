package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.dialog.DoCallVideoWaitDialog;
import com.ailiaovideo.videoline.event.CuckooCallVideoEvent;
import com.ailiaovideo.videoline.event.EImVideoCallReplyMessages;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestCheckIsCharging;
import com.ailiaovideo.videoline.json.JsonRequestReplyCallVideo;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserChatData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsg;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsgVideoCallReply;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.BGEventManage;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.utils.im.IMHelp;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class CuckooVideoCallWaitActivity extends BaseActivity {

    public static final String CALL_USER_INFO = "CALL_USER_INFO";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String IS_USE_FREE = "IS_USE_FREE";
    public static final String CALL_TYPE = "CALL_TYPE";

    private String channelId;
    private String isUseFree;
    private UserModel callUserInfo;
    private UserModel mUserInfo;

    private CircleImageView mIvHead;
    private ImageView iv_bg;
    private TextView mTvName;
    private TextView mTvText;
    private MediaPlayer mediaPlayer;
    private DoCallVideoWaitDialog.OnDialogClick onDialogClick;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_video_call_wait;
    }

    @Override
    protected void initView() {

        MyApplication.getInstances().setInVideoCallWait(true);

        mUserInfo = SaveData.getInstance().getUserInfo();

        this.callUserInfo = getIntent().getParcelableExtra(CALL_USER_INFO);
        this.channelId = getIntent().getStringExtra(CHANNEL_ID);
        this.isUseFree = getIntent().getStringExtra(IS_USE_FREE);

        iv_bg = findViewById(R.id.iv_bg);
        findViewById(R.id.repulse_call).setOnClickListener(this);
        findViewById(R.id.accept_call).setOnClickListener(this);
        mIvHead = findViewById(R.id.call_player_img);
        mTvName = findViewById(R.id.call_player_name);
        mTvText = findViewById(R.id.tv_text);

        Utils.loadHttpImg(this, callUserInfo.getAvatar(), mIvHead);
        Utils.loadHttpImgBlue(this, callUserInfo.getAvatar(), iv_bg, 0);

        mTvName.setText(callUserInfo.getUser_nickname());

        if (StringUtils.toInt(isUseFree) == 1) {
            mTvText.setText("一键约爱随机来电，接通只获系统最低奖励");
        }

    }

    @Override
    protected void initSet() {

        mediaPlayer = MediaPlayer.create(this, R.raw.call);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null && !isFinishing()) {
                    mediaPlayer.start();
                }
            }
        });
        mediaPlayer.start();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }


    public void setOnDialogClick(DoCallVideoWaitDialog.OnDialogClick onDialogClick) {
        this.onDialogClick = onDialogClick;
    }

    /**
     * 接通
     */
    private void doAcceptCall() {
        if (StringUtils.toInt(channelId) == 1111111) {
            Common.showRechargeDialog(this, "充值后再撩主播！");
            //finish();
            return;
        }
        ToastUtils.showLong("已接通");
        doRequestReplyCall("1");
    }

    /**
     * 拒接
     */
    private void doRepulseCall() {
        if (StringUtils.toInt(channelId) == 1111111) {
            Common.showRechargeDialog(this, "充值后再撩主播！");
            //finish();
            return;
        }
        ToastUtils.showLong("拒接");
        doRequestReplyCall("2");
    }

    /**
     * 应答视频通话
     */
    private void doRequestReplyCall(final String type) {

        showLoadingDialog("操作中...");
        Api.doReplyVideoCall(mUserInfo.getId(), mUserInfo.getToken(), callUserInfo.getId(), channelId, type, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestReplyCallVideo jsonData = JsonRequestReplyCallVideo.getJsonObj(s);
                if (jsonData.getCode() == 1) {

                    IMHelp.sendReplyVideoCallMsg(jsonData.getData().getChannel(), type, jsonData.getData().getTo_user_id(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            hideLoadingDialog();
                            LogUtils.i("IM 一对一回复消息推送失败！");
                            ToastUtils.showLong("回复通话失败！");
                            finish();
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            hideLoadingDialog();
                            LogUtils.i("IM 一对一回复消息推送成功！");
                            if (StringUtils.toInt(type) == 2) {
                                finish();
                                return;
                            }
                            jumpVideoCallActivity();
                            finish();
                        }
                    });

                    CuckooCallVideoEvent cuckooCallVideoEvent = new CuckooCallVideoEvent();
                    EventBus.getDefault().post(cuckooCallVideoEvent);

                } else {
                    hideLoadingDialog();
                    ToastUtils.showLong(jsonData.getMsg());
                    finish();
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                hideLoadingDialog();
                finish();
            }
        });
    }


    //跳转视频通话页面
    private void jumpVideoCallActivity() {

        Api.doCheckIsNeedCharging(SaveData.getInstance().getId(), callUserInfo.getId(),channelId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestCheckIsCharging jsonObj = (JsonRequestCheckIsCharging) JsonRequestBase.getJsonObj(s, JsonRequestCheckIsCharging.class);
                if (jsonObj.getCode() == 1) {
                    if (channelId != null && !TextUtils.isEmpty(channelId)) {
                        //组装拨打电话信息跳转通话页面
                        UserChatData userChatData = new UserChatData();
                        userChatData.setChannelName(channelId);
                        userChatData.setUserModel(callUserInfo);
                        Intent intent;
                        int type = getIntent().getIntExtra(CALL_TYPE, 0);
                        if (type == 0) {
                            intent = new Intent(CuckooVideoCallWaitActivity.this, VideoLineActivity.class);
                        } else {
                            intent = new Intent(CuckooVideoCallWaitActivity.this, CuckooVoiceCallActivity.class);
                        }
                        intent.putExtra("obj", userChatData);
                        intent.putExtra("video_px", jsonObj.getResolving_power());
                        intent.putExtra(VideoLineActivity.IS_NEED_CHARGE, jsonObj.getIs_need_charging() == 1);
                        intent.putExtra(VideoLineActivity.IS_BE_CALL, true);
                        intent.putExtra(VideoLineActivity.VIDEO_DEDUCTION, jsonObj.getVideo_deduction());
                        intent.putExtra(VideoLineActivity.CALL_TYPE, type);
                        intent.putExtra(VideoLineActivity.ROOM_TOKEN, jsonObj.getRoom_token());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    ToastUtils.showLong(jsonObj.getMsg());
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.repulse_call:
                doRepulseCall();
//                if(onDialogClick != null){
//                    onDialogClick.onLeftClick();
//                }
                break;

            case R.id.accept_call:
                doAcceptCall();
//                if(onDialogClick != null){
//                    onDialogClick.onRightClick();
//                }
                break;

            default:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVideoCallThread(EImVideoCallReplyMessages var1) {

        LogUtils.i("收到消息一对一视频回复消息:" + var1.msg.getCustomMsg().getSender().getUser_nickname());

        try {
            CustomMsg customMsg = var1.msg.getCustomMsg();
            CustomMsgVideoCallReply customMsgVideoCallReply = ((CustomMsgVideoCallReply) customMsg);
            //挂断通话关闭弹窗
            if (StringUtils.toInt(customMsgVideoCallReply.getReply_type()) == 2) {
                finish();
            }
        } catch (Exception e) {
            LogUtils.i("跳转接通电话页面错误error" + e.getMessage());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }


    @Override
    public void onBackPressed() {
        doRepulseCall();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        MyApplication.getInstances().setInVideoCallWait(false);
    }

    public interface OnDialogClick {
        void onLeftClick();

        void onRightClick();
    }

}
