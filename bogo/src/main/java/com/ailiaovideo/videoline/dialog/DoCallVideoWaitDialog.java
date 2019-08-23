package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.drawable.BGDrawable;
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
import com.ailiaovideo.videoline.ui.VideoLineActivity;
import com.ailiaovideo.videoline.utils.BGEventManage;
import com.ailiaovideo.videoline.utils.BGViewUtil;
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


/**
 * 等待接听通话
 * Created by weipeng on 2018/2/18.
 */

public class DoCallVideoWaitDialog extends BGDialogBase implements View.OnClickListener {

    private String channelId;
    private String isUseFree;
    private UserModel callUserInfo;
    private UserModel mUserInfo;

    private CircleImageView mIvHead;
    private TextView mTvName;
    private TextView mTvText;
    private MediaPlayer mediaPlayer;
    private OnDialogClick onDialogClick;

    public DoCallVideoWaitDialog(@NonNull Context context, UserModel callUserInfo, String channelId, String isUseFree) {
        super(context);

        this.callUserInfo = callUserInfo;
        this.channelId = channelId;
        this.isUseFree = isUseFree;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_do_call_video_wait);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        BGViewUtil.setBackgroundDrawable(getContentView(), new BGDrawable().color(Color.WHITE).cornerAll(10));

        setHeight(ConvertUtils.dp2px(200));
        padding(10, 0, 10, 0);

        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.repulse_call).setOnClickListener(this);
        findViewById(R.id.accept_call).setOnClickListener(this);
        mIvHead = findViewById(R.id.call_player_img);
        mTvName = findViewById(R.id.call_player_name);
        mTvText = findViewById(R.id.tv_text);

        Utils.loadHttpImg(getContext(), callUserInfo.getAvatar(), mIvHead);
        mTvName.setText(callUserInfo.getUser_nickname());

        if (StringUtils.toInt(isUseFree) == 1) {
            mTvText.setText("一键约爱随机来电，接通只获系统最低奖励");
        }

    }

    private void initData() {
        BGEventManage.register(this);
        mUserInfo = SaveData.getInstance().getUserInfo();


        mediaPlayer = MediaPlayer.create(getContext(), R.raw.call);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });
        mediaPlayer.start();
    }

    public void setOnDialogClick(OnDialogClick onDialogClick) {
        this.onDialogClick = onDialogClick;
    }

    /**
     * 接通
     */
    private void doAcceptCall() {
        if (StringUtils.toInt(channelId) == 1111111) {
            ToastUtils.showLong("余额不足，请充值！");
            dismiss();
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
            ToastUtils.showLong("余额不足，请充值！");
            dismiss();
            return;
        }
        ToastUtils.showLong("拒接");
        doRequestReplyCall("2");
    }

    /**
     * 应答视频通话
     */
    private void doRequestReplyCall(final String type) {

        final QMUITipDialog tipD = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载...")
                .create();
        tipD.show();

        Api.doReplyVideoCall(mUserInfo.getId(), mUserInfo.getToken(), callUserInfo.getId(), channelId, type, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestReplyCallVideo jsonData = JsonRequestReplyCallVideo.getJsonObj(s);
                if (jsonData.getCode() == 1) {

                    IMHelp.sendReplyVideoCallMsg(jsonData.getData().getChannel(), type, jsonData.getData().getTo_user_id(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            tipD.dismiss();
                            LogUtils.i("IM 一对一回复消息推送失败！");
                            ToastUtils.showLong("回复通话失败！");
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            tipD.dismiss();
                            LogUtils.i("IM 一对一回复消息推送成功！");
                            if (StringUtils.toInt(type) == 2) {
                                dismiss();
                                return;
                            }
                            jumpVideoCallActivity();
                        }
                    });

                    CuckooCallVideoEvent cuckooCallVideoEvent = new CuckooCallVideoEvent();
                    EventBus.getDefault().post(cuckooCallVideoEvent);

                } else {
                    tipD.dismiss();
                    ToastUtils.showLong(jsonData.getMsg());
                }

                dismiss();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                tipD.dismiss();
                dismiss();
            }
        });
    }


    //跳转视频通话页面
    private void jumpVideoCallActivity() {

        Api.doCheckIsNeedCharging(SaveData.getInstance().getId(), callUserInfo.getId(),channelId,new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestCheckIsCharging jsonObj = (JsonRequestCheckIsCharging) JsonRequestBase.getJsonObj(s, JsonRequestCheckIsCharging.class);
                if (jsonObj.getCode() == 1) {
                    if (channelId != null && !TextUtils.isEmpty(channelId)) {
                        //组装拨打电话信息跳转通话页面
                        UserChatData userChatData = new UserChatData();
                        userChatData.setChannelName(channelId);
                        userChatData.setUserModel(callUserInfo);
                        Intent intent = new Intent(getContext(), VideoLineActivity.class);
                        intent.putExtra("obj", userChatData);
                        intent.putExtra("video_px", jsonObj.getResolving_power());
                        intent.putExtra(VideoLineActivity.IS_NEED_CHARGE, jsonObj.getIs_need_charging() == 1);
                        intent.putExtra(VideoLineActivity.IS_BE_CALL, true);
                        intent.putExtra(VideoLineActivity.VIDEO_DEDUCTION, jsonObj.getVideo_deduction());
                        getContext().startActivity(intent);
                        dismiss();
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
            if (StringUtils.toInt(customMsgVideoCallReply.getReply_type()) == 1) {
                dismiss();
            }
        } catch (Exception e) {
            LogUtils.i("跳转接通电话页面错误error" + e.getMessage());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        BGEventManage.unregister(this);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public interface OnDialogClick {
        void onLeftClick();

        void onRightClick();
    }
}
