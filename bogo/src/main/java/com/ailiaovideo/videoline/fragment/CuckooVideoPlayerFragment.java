package com.ailiaovideo.videoline.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.ApiConstantDefine;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.VideoPlayerAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.dialog.CuckooShareDialog;
import com.ailiaovideo.videoline.dialog.GiftBottomDialog;
import com.ailiaovideo.videoline.dialog.ShowPayVideoDialog;
import com.ailiaovideo.videoline.event.CuckooBuyVideoCommonEvent;
import com.ailiaovideo.videoline.event.CuckooOnTouchShortVideoChangeEvent;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetVideo;
import com.ailiaovideo.videoline.json.JsonRequestDoPrivateSendGif;
import com.ailiaovideo.videoline.json.JsonRequestGetUserBaseInfo;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.manage.AppConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.GiftAnimationModel;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsgPrivateGift;
import com.ailiaovideo.videoline.ui.CuckooVideoTouchPlayerActivity;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.VideoPlayerActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.GiftAnimationContentView;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 小视频播放
 */
public class CuckooVideoPlayerFragment extends BaseFragment implements GiftBottomDialog.DoSendGiftListen, ITXLivePlayListener {

    //功能
    private TXCloudVideoView mTXCloudVideoView;
    private ImageView toLoadVideo;//加载...
    private ImageView lovePlayerImg;//是否喜欢当前视频图像
    //数据
    private CircleImageView thisPlayerImg;//当前player头像--点击进入player主页
    private TextView thisPlayerName;//当前player的昵称
    private TextView sharePlayerNumber; //分享人数
    private TextView lovePlayerNumber; //喜欢、关注人数
    private TextView playerVideoTitle;//当前视频title
    //按钮
    private ImageView thisPlayerLoveme;//关注这个player
    private GiftBottomDialog giftBottomDialog;

    @BindView(R.id.tv_look_count)
    TextView tv_look_count;

    @BindView(R.id.ll_gift_content)
    GiftAnimationContentView ll_gift_content;
    //else
    private VideoModel videoData;
    //是否关注
    private int isFollow = 0;

    private boolean mVideoIsPlay;
    private TXLivePlayer mTXLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig = null;


    public static final String IS_FOLLOW = "IS_FOLLOW";
    public static final String VIDEO_DATA = "VIDEO_DATA";
    private View view;
    private boolean isFollowVideo;

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_cuckoo_video_player, container, false);
    }

    @Override
    protected void initView(View view) {
        this.view = view;
        mTXCloudVideoView = view.findViewById(R.id.video_play_video);
        toLoadVideo = view.findViewById(R.id.toload_video);
        lovePlayerImg = view.findViewById(R.id.love_player_img);
        thisPlayerImg = view.findViewById(R.id.this_player_img);
        thisPlayerName = view.findViewById(R.id.this_player_name);
        sharePlayerNumber = view.findViewById(R.id.share_player_number);
        lovePlayerNumber = view.findViewById(R.id.love_player_number);
        playerVideoTitle = view.findViewById(R.id.player_video_title);
        thisPlayerLoveme = view.findViewById(R.id.this_player_loveme);

        view.findViewById(R.id.call_player_btn).setOnClickListener(this);
        thisPlayerImg.setOnClickListener(this);

        ll_gift_content.startHandel();
    }

    @Override
    protected void initDate(View view) {

        isFollow = StringUtils.toInt(getArguments().getString(IS_FOLLOW));
        videoData = getArguments().getParcelable(VIDEO_DATA);

        sharePlayerNumber.setText(videoData.getShare());
        playerVideoTitle.setText(videoData.getTitle());
        tv_look_count.setText(videoData.getViewed());
        thisPlayerLoveme.setVisibility("0".equals(videoData.getAttention()) ? View.VISIBLE : View.GONE);

        toLoadVideo.setVisibility(View.VISIBLE);
        requestUserByVideo();//获取视频的用户信息
    }

    @Override
    protected void initSet(View view) {

        initLive();
    }

    @Override
    protected void initDisplayData(View view) {

    }

    private void initLive() {

        mTXLivePlayer = new TXLivePlayer(getContext());
        mTXPlayConfig = new TXLivePlayConfig();

        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.enableHardwareDecode(false);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_ROTATION_PORTRAIT);//RENDER_MODE_ADJUST_RESOLUTION
        mTXLivePlayer.setConfig(mTXPlayConfig);
    }

    @OnClick({R.id.video_play_close, R.id.ll_share, R.id.iv_gift, R.id.love_player_img})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.love_player_img:
                clickLoveVideo();
                break;
            //点击进入player主页
            case R.id.this_player_img:
                toThisPlayer();
                break;
            case R.id.video_play_close:
                getActivity().finish();
                break;
            case R.id.ll_share:
                showSimpleBottomSheetGrid();
                break;
            case R.id.iv_gift:
                clickOpenGiftDialog();
                break;

            //与当前player1v1视频
            case R.id.call_player_btn:
                callPlayer();
                break;
            default:
                break;
        }
    }

    private void toThisPlayer() {

        Common.jumpUserPage(getContext(), videoData.getUid());
    }


    //视频当前player
    private void callPlayer() {
        Common.callVideo(getActivity(), videoData.getUid(),0);
    }

    //点赞视频
    private void clickLoveVideo() {

        lovePlayerImg.setImageResource(R.drawable.video_like_on);
        Api.doFollowVideo(uId, uToken, videoData.getId(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {

                    if (isFollowVideo) {
                        lovePlayerImg.setImageResource(R.drawable.video_like);
                    } else {
                        lovePlayerImg.setImageResource(R.drawable.video_like_on);
                    }

                    isFollowVideo = !isFollowVideo;

                    showToastMsg(getContext(), getString(R.string.zan_success));
                } else {
                    showToastMsg(getContext(), jsonObj.getMsg());
                }
            }
        });
    }

    /**
     * 获取当前视频主播信息
     */
    private void requestUserByVideo() {

        if (CuckooVideoTouchPlayerActivity.select_video_id != StringUtils.toInt(videoData.getId())) {
            return;
        }
        Api.doGetVideo(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), videoData.getId(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JsonRequestDoGetVideo jsonObj = (JsonRequestDoGetVideo) JsonRequestDoGetVideo.getJsonObj(s, JsonRequestDoGetVideo.class);
                if (jsonObj.getCode() == 1) {
                    toLoadVideo.setVisibility(View.GONE);
                    videoData.setVideo_url(jsonObj.getVideo_url());
                    videoData.setFollow_num(jsonObj.getFollow_num());

                    if (ApiUtils.isTrueUrl(jsonObj.getAvatar())) {
                        Utils.loadHttpImg(getContext(), Utils.getCompleteImgUrl(jsonObj.getAvatar()), thisPlayerImg);
                    }

                    thisPlayerName.setText(videoData.getTitle());
                    isFollowVideo = StringUtils.toInt(jsonObj.getIs_follow()) == 1;
                    if (isFollowVideo) {
                        lovePlayerImg.setImageResource(R.drawable.video_like_on);
                    } else {
                        lovePlayerImg.setImageResource(R.drawable.video_like);
                    }
                    lovePlayerNumber.setText(jsonObj.getFollow_num());
                    startPlay();
                } else if (jsonObj.getCode() == ApiConstantDefine.ApiCode.VIDEO_NOT_PAY) {

                    ShowPayVideoDialog dialog = new ShowPayVideoDialog(getContext(), videoData);
                    dialog.setDialogClickCallback(new ShowPayVideoDialog.DialogClickCallback() {
                        @Override
                        public void onClickLeft() {
                            getActivity().finish();
                        }

                        @Override
                        public void onClickRight() {

                        }
                    });
                    dialog.show();
                } else {
                    showToastMsg(getContext(), jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                toLoadVideo.setVisibility(View.GONE);
            }
        });
    }

    private boolean startPlay() {

        int result = mTXLivePlayer.startPlay(videoData.getVideo_url(), TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result != 0) {
            //mStartPreview.setBackgroundResource(R.drawable.icon_record_start);
            return false;
        }

        mVideoIsPlay = true;
        return true;
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mVideoIsPlay = false;
        }
    }

    //礼物弹窗
    private void clickOpenGiftDialog() {
        if (giftBottomDialog == null) {

            giftBottomDialog = new GiftBottomDialog(getContext(), videoData.getUid());
            giftBottomDialog.setType(1);
            giftBottomDialog.setChanelId("");
            giftBottomDialog.setDoSendGiftListen(this);
        }

        giftBottomDialog.show();
    }

    /**
     * 显示一个分享框
     */
    private void showSimpleBottomSheetGrid() {
        CuckooShareDialog cuckooShareDialog = new CuckooShareDialog(getContext());
        cuckooShareDialog.setImg(videoData.getImg());
        cuckooShareDialog.setShareUrl(AppConfig.MAIN_URL + "/mapi/public/index.php/api/share_api/index/id/" + videoData.getId() + "/invite_code/" + SaveData.getInstance().getId());
        cuckooShareDialog.show();
    }


    //视频页面滑动切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTouchPageChange(CuckooOnTouchShortVideoChangeEvent event) {

        if ((isVisible() && CuckooVideoTouchPlayerActivity.select_video_id == StringUtils.toInt(videoData.getId()))) {
            if (mTXCloudVideoView != null) {
                requestUserByVideo();
            }
        } else {
            stopPlay(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTouchPageChange(CuckooBuyVideoCommonEvent event) {

        if (event.getVideoId().equals(videoData.getId())) {
            requestUserByVideo();
        }
    }

    @Override
    protected boolean isRegEvent() {
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        mTXLivePlayer.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mTXLivePlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTXLivePlayer != null) {
            mTXLivePlayer.stopPlay(true);
        }
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onDestroy();
        }
    }

    @Override
    public void onSuccess(JsonRequestDoPrivateSendGif sendGif) {

        final CustomMsgPrivateGift gift = new CustomMsgPrivateGift();
        gift.fillData(sendGif.getSend());
        if (ll_gift_content != null) {
            pushGiftMsg(gift);
        }
    }


    //添加礼物消息
    private void pushGiftMsg(CustomMsgPrivateGift giftCustom) {

        GiftAnimationModel giftAnimationModel = new GiftAnimationModel();

        giftAnimationModel.setUserAvatar(giftCustom.getSender().getAvatar());
        giftAnimationModel.setUserNickname(giftCustom.getSender().getUser_nickname());
        giftAnimationModel.setMsg(giftCustom.getFrom_msg());
        giftAnimationModel.setGiftIcon(giftCustom.getProp_icon());
        if (ll_gift_content != null) {
            ll_gift_content.addGift(giftAnimationModel);
        }
    }

    @Override
    public void onPlayEvent(int i, Bundle bundle) {

    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }
}
