package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.inter.VideoDoClick;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.TabLiveListModel;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.lzy.okgo.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 自定义videoPage
 * 匹配视频
 */

public class VideoDoFragment extends FrameLayout implements IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnErrorListener, View.OnClickListener {
    private ImageView videoCutOut;//截图举报
    private ImageView pushThisVideo;//刷新当前视频
    private TextView videoLoveBtn;//关注按钮
    private TextView videoNickName;//player昵称
    private RelativeLayout videoMasking;//蒙层
    private ImageView maskingImg;//蒙层图片
    private TextView maskingMsg;//提示消息
    private ImageView maskingCloseWhite;//继续刷新按钮
    FrameLayout videoBar;

    private UserModel mUserModel;

    private TabLiveListModel mDataSource;

    //else
    private VideoDoClick videoDoClick;//刷新操作接口
    private KSYTextureView mVideoView;

    //是否初始化过
    private boolean isInit = false;

    public VideoDoFragment(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoDoFragment(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoDoFragment(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化方法
     * @param context
     */
    private void init(Context context){
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.view_videoplay_video, this);

        videoCutOut = findViewById(R.id.video_cutOut);
        pushThisVideo = findViewById(R.id.push_thisVideo);
        videoLoveBtn = findViewById(R.id.video_loveBtn);
        videoNickName = findViewById(R.id.video_nickName);

        videoMasking = findViewById(R.id.video_masking);
        maskingImg = findViewById(R.id.await_page);
        maskingMsg = findViewById(R.id.video_masking_msg);
        maskingCloseWhite = findViewById(R.id.masking_close_white);
        //videoMasking.setVisibility(View.GONE);
        videoBar = findViewById(R.id.video_bar);
        mVideoView = findViewById(R.id.video_view);

        //刷新按钮的刷新操作
        pushThisVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchMasking();
                if (videoDoClick != null){
                    videoDoClick.refreshVideo(videoMasking);
                }
            }
        });

        videoLoveBtn.setOnClickListener(this);

        mUserModel = SaveData.getInstance().getUserInfo();
        initLive();
    }

    //初始化视频播放
    private void initLive() {

        //静音
        mVideoView.setVolume(0.0f,0.0f);
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                LogUtils.i("准备就绪开始播放");
                if(mVideoView != null) {
                    hideMasking();
                    // 设置视频伸缩模式，此模式为裁剪模式
                    mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_NOSCALE_TO_FIT);
                    // 开始播放视频
                    mVideoView.start();
                }
            }
        });
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnErrorListener(this);
    }

    /**
     * 初始化显示
     * @return 当前画布对象
     */
    public void initDisplay(Context context,TabLiveListModel userData){

        this.mDataSource = userData;
        if(StringUtils.toInt(mDataSource.getIs_follow()) == 1){
            hideLoveBtn();
        }
        videoNickName.setText(userData.getUser_nickname());
        videoBar.addView(new GradeShowLayout(context,"2", StringUtils.toInt(userData.getSex())));

        if(userData.getPull_url() != null){

            if(!isInit){
                //设置播放地址并准备
                try {
                    mVideoView.setDataSource(userData.getPull_url());
                    mVideoView.prepareAsync();
                    LogUtils.i("Init 视频播放地址" + userData.getPull_url());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                LogUtils.i("Refresh 视频播放地址" + userData.getPull_url());
                refreshLive();
            }

        }

        if(!isInit){
            isInit = true;
        }

    }

    /**
    * 更换视频源播放新视频
    * */
    public void refreshLive(){

        if (mVideoView != null && mDataSource.getPull_url() != null){
            mVideoView.runInForeground();
            //当前有视频正在播放，需先停止当前播放
            mVideoView.stop();
            //重置播放
            mVideoView.reset();
            //重新设置播放参数
            //mVideoView.setBufferTimeMax(2.0f);
            //mVideoView.setTimeout(5, 30);
            //mVideoView.setRotateDegree(90);
            //......
            //(其它参数设置)
            //......
            //设置新的播放地址
            try {
                mVideoView.setDataSource(mDataSource.getPull_url());
                mVideoView.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
    * 后台前台切换操作
    * */
    public void runInBackground(boolean isBackground){
        if(isBackground){

            if(mVideoView != null ){
                mVideoView.runInBackground(false);
            }

        }else{
            showSearchMasking();

            if(mVideoView != null ){
                mVideoView.runInForeground();
            }
        }
    }

    /**
    * 开始播放视频
    * */
    public void startLive(){

        if(mVideoView != null){
            mVideoView.start();
        }
    }

    /**
    * 停止播放视频
    * */
    public void stopLive(){

        if(mVideoView != null){
            mVideoView.stop();
        }
    }

    /**
    * 释放
    * */
    public void onDestroy(){
        if(mVideoView != null){
            mVideoView.stop();
            mVideoView.release();
        }
    }

    /**
     * 设置刷新监听(自带效果)
     * @param videoDoClick
     */
    public void setVideoDoClick(VideoDoClick videoDoClick){
        this.videoDoClick = videoDoClick;
    }

    /**
     * 截屏举报点击监听
     * @param onclick
     */
    public void setCutOutOnClick(OnClickListener onclick){
        videoCutOut.setOnClickListener(onclick);
    }

    /**
     * 刷新当前页点击监听
     * @param onclick
     */
    public void setPushVideoOnClick(OnClickListener onclick){
        pushThisVideo.setOnClickListener(onclick);
    }

    /**
     * 关注按钮点击监听
     * @param onclick
     */
    public void setLoveBtnOnClick(OnClickListener onclick){
        videoLoveBtn.setOnClickListener(onclick);
    }

    /**
     * 获取当前videoFullScreen
     * @return Fragmelayout
     */
    public KSYTextureView getVideoFullScreen(){
        return mVideoView;
    }

    /**
     * 显示关注按钮
     */
    public void showLoveBtn(){
        videoLoveBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏关注按钮
     */
    public void hideLoveBtn(){
        videoLoveBtn.setVisibility(View.GONE);
    }

    /**
     * 设置昵称
     * @param nickName
     */
    public void setNickName(String nickName){
        videoNickName.setText(nickName);
    }

    /**
     * 显示搜索中蒙层
     */
    public void showSearchMasking(){

        stopLive();
        videoMasking.setVisibility(View.VISIBLE);
        maskingImg.setImageResource(R.drawable.icon_logo_logo);
        maskingMsg.setText("正在搜索...");
    }

    /**
     * 显示无匹配蒙层
     */
    public void showNoMatchMasking(){
        videoMasking.setVisibility(View.VISIBLE);
        maskingImg.setImageResource(R.drawable.video_chat_empty_small);
        maskingMsg.setText("暂无匹配数据");
    }

    /**
     * 隐藏蒙层
     */
    public void hideMasking(){
        videoMasking.setVisibility(View.GONE);
    }

    //////////////////////////////////////////////播放器监听事件//////////////////////////////////////////////////////////////////

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
//                if(mVideoView != null) {
//                    mVideoView.release();
//                }
            }
        };
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        switch (i) {
            case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                LogUtils.d("开始缓冲数据");
                break;
            case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                LogUtils.d( "数据缓冲完毕");
                break;
            case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                LogUtils.d("开始播放音频");;
                break;
            case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                LogUtils.d( "开始渲染视频");
                break;
            case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:
                // 播放SDK有做快速开播的优化，在流的音视频数据交织并不好时，可能只找到某一个流的信息
                // 当播放器读到另一个流的数据时会发出此消息通知
                // 请务必调用reload接口
                if(mVideoView != null){
                    if(mDataSource != null && mDataSource.getPull_url() != null){

                        mVideoView.reload(mDataSource.getPull_url(), false);
                    }
                }
                break;
            case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                LogUtils.d( "reload成功的消息通知");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        switch (i)
        {
            case KSYMediaPlayer.MEDIA_ERROR_IO:
                //【读超时】和【链接超时】均能导致此错误的出现，用户可以选择重连
                LogUtils.i("播放视频错误");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.video_loveBtn:

                clickFollow();
                break;

            default:
                break;
        }
    }

    //点击关注
    private void clickFollow() {

        hideLoveBtn();
        Api.doLoveTheUser(mDataSource.getId(), mUserModel.getId(), mUserModel.getToken(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s,JsonRequestBase.class);
                if(jsonObj.getCode() == 1){
                    ToastUtils.showLong("关注成功");
                }
            }
        });
    }
}
