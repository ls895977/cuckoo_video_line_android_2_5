package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.ApiConstantDefine;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.dialog.ShowPayVideoDialog;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequest;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetVideo;
import com.ailiaovideo.videoline.json.JsonRequestGetUserBaseInfo;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 短视频播放
 * Created by fly on 2017/12/23 0023.
 */

public class VideoPlayerActivity extends BaseActivity implements View.OnClickListener {
    //功能
    private KSYTextureView mVideoPlay;//video play
    private ImageView toLoadVideo;//加载...
    private TextView shareMeMsg;//分享有礼
    private ImageView lovePlayerImg;//是否喜欢当前视频图像
    //数据
    private CircleImageView thisPlayerImg;//当前player头像--点击进入player主页
    private TextView thisPlayerName;//当前player的昵称
    private TextView thisPlayerNumber;//当前player的粉丝数量
    private TextView sharePlayerNumber; //分享人数
    private TextView lovePlayerNumber; //喜欢、关注人数
    private TextView playerVideoTitle;//当前视频title
    //按钮
    private TextView thisPlayerLoveme;//关注这个player

    //else
    private VideoModel videoData;

    public static final String IS_FOLLOW = "IS_FOLLOW";
    //是否关注
    private int isFollow = 0;

    /////////////////////////////////////////////初始化操作/////////////////////////////////////////
    @Override
    public void initView() {
        mVideoPlay = findViewById(R.id.video_play_video);
        toLoadVideo = findViewById(R.id.toload_video);
        shareMeMsg = findViewById(R.id.share_me_msg);
        lovePlayerImg = findViewById(R.id.love_player_img);

        thisPlayerImg = findViewById(R.id.this_player_img);
        thisPlayerName = findViewById(R.id.this_player_name);
        thisPlayerNumber = findViewById(R.id.this_player_number);
        sharePlayerNumber = findViewById(R.id.share_player_number);
        lovePlayerNumber = findViewById(R.id.love_player_number);
        playerVideoTitle = findViewById(R.id.player_video_title);

        thisPlayerLoveme = findViewById(R.id.this_player_loveme);
    }

    @Override
    public void initData() {
        isFollow = StringUtils.toInt(getIntent().getStringExtra(IS_FOLLOW));
        videoData = getIntent().getParcelableExtra("obj");

        requestUserByVideo();//获取视频的用户信息
        sharePlayerNumber.setText(videoData.getShare());
        lovePlayerNumber.setText(videoData.getFollow_num());
        playerVideoTitle.setText(videoData.getTitle());
        thisPlayerNumber.setText(videoData.getViewed());
        thisPlayerLoveme.setVisibility("0".equals(videoData.getAttention()) ? View.VISIBLE : View.GONE);

        if (isFollow == 1) {
            lovePlayerImg.setImageResource(R.drawable.icon_video_liked);
        } else {
            lovePlayerImg.setImageResource(R.drawable.icon_video_unliked);
        }
    }

    @Override
    protected void initPlayerDisplayData() {
        //thisPlayerName.setText(playerModle.getThisPlayerName());
    }

    @Override
    protected void initSet() {
        setOnclickListener(thisPlayerImg, thisPlayerLoveme);
        setOnclickListener(R.id.video_play_close, R.id.call_player_btn, R.id.love_player_btn, R.id.share_player_btn);
        initLive();
    }

    private void initLive() {

        mVideoPlay.setLooping(true);
        mVideoPlay.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                LogUtils.i("准备就绪开始播放");
                if (mVideoPlay != null) {
                    // 设置视频伸缩模式，此模式为裁剪模式
                    mVideoPlay.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_NOSCALE_TO_FIT);
                    // 开始播放视频
                    mVideoPlay.start();
                }
            }
        });

        //设置播放地址并准备
        try {
            if (mVideoPlay != null) {

                mVideoPlay.setDataSource(videoData.getVideo_url());
                mVideoPlay.prepareAsync();
                LogUtils.i("Init 视频播放地址" + videoData.getVideo_url());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected Context getNowContext() {
        return VideoPlayerActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mVideoPlay != null) {

            mVideoPlay.runInBackground(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mVideoPlay != null) {

            mVideoPlay.runInForeground();
        }
    }

    /////////////////////////////////////////////监听事件处理/////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击进入player主页
            case R.id.this_player_img:
                toThisPlayer();
                break;
            //关注这个player
            case R.id.this_player_loveme:
                loveThisPlayer();
                break;
            //close button
            case R.id.video_play_close:
                finish();
                break;
            //与当前player1v1视频
            case R.id.call_player_btn:
                callPlayer();
                break;
            //喜欢这个视频
            case R.id.love_player_btn:
                loveThisVideo();
                break;
            //分享这个视频
            case R.id.share_player_btn:
                shareThisVideo();
                break;
        }
    }

    /////////////////////////////////////////////业务逻辑处理/////////////////////////////////////////
    //分享这个视频
    private void shareThisVideo() {
        concealView(shareMeMsg);
        showSimpleBottomSheetGrid();
    }

    //进入当前player主页
    private void toThisPlayer() {

        Common.jumpUserPage(VideoPlayerActivity.this, videoData.getId());
    }

    //关注这个player
    private void loveThisPlayer() {
        Api.doLoveTheUser(
                videoData.getId(),
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
                        JsonRequest requestObj = JsonRequest.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            concealView(R.id.this_player_loveme);//隐藏关注按钮
                            showToastMsg(getString(R.string.follow_success));
                        } else {
                            log("关注当前player:" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    //视频当前player
    private void callPlayer() {
        Common.callVideo(this, videoData.getUid(),0);
    }

    //喜欢这个视频
    private void loveThisVideo() {

        lovePlayerImg.setImageResource(R.drawable.icon_video_liked);
        Api.doFollowVideo(uId, uToken, videoData.getId(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.zan_success));
                } else {
                    showToastMsg(jsonObj.getMsg());
                }
            }
        });
    }

    /**
     * 获取当前视频主播信息
     */
    private void requestUserByVideo() {
        Api.getUserBaseData(
                videoData.getUid(),
                uId,
                uToken,
                new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonRequestGetUserBaseInfo requestObj =
                                (JsonRequestGetUserBaseInfo) JsonRequestGetUserBaseInfo.getJsonObj(s, JsonRequestGetUserBaseInfo.class);
                        if (requestObj.getCode() == 1) {
                            UserModel targetUserData = requestObj.getUser_info();
                            if (ApiUtils.isTrueUrl(targetUserData.getAvatar())) {

                                Utils.loadHttpImg(VideoPlayerActivity.this, Utils.getCompleteImgUrl(targetUserData.getAvatar()), thisPlayerImg);
                            }
                            thisPlayerName.setText(targetUserData.getUser_nickname());

                        } else {
                            showToastMsg("获取当前视频主播信息::" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    /**
     * 显示一个分享框
     */
    private void showSimpleBottomSheetGrid() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(VideoPlayerActivity.this);
        builder.addItem(R.drawable.icon_weixin_friend, getString(R.string.share_wechat), TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_weixin_moment, getString(R.string.share_pyq), TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                showToastMsg(getString(R.string.share_wechat));
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                showToastMsg(getString(R.string.share_pyq));
                                break;
                        }
                    }
                }).build().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlay != null) {
            mVideoPlay.stop();
            mVideoPlay.release();
        }
    }

    /**
     * @param videoData 视频信息
     * @dw 跳转播放视频
     */
    public static void startVideoPlayerActivity(final Context context, final VideoModel videoData) {

        final UserModel userModel = SaveData.getInstance().getUserInfo();
        Api.doGetVideo(userModel.getId(), userModel.getToken(), videoData.getId(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JsonRequestDoGetVideo jsonObj = (JsonRequestDoGetVideo) JsonRequestDoGetVideo.getJsonObj(s, JsonRequestDoGetVideo.class);
                if (jsonObj.getCode() == 1) {

                    videoData.setVideo_url(jsonObj.getVideo_url());
                    videoData.setFollow_num(jsonObj.getFollow_num());
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra(VideoPlayerActivity.IS_FOLLOW, jsonObj.getIs_follow());
                    intent.putExtra("obj", videoData);
                    context.startActivity(intent);
                } else if (jsonObj.getCode() == ApiConstantDefine.ApiCode.VIDEO_NOT_PAY) {

                    ShowPayVideoDialog dialog = new ShowPayVideoDialog(context, videoData);
                    dialog.show();
                }
            }
        });
    }
}
