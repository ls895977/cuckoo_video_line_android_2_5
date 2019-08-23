package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.video.videoupload.TXUGCPublish;
import com.ailiaovideo.video.videoupload.TXUGCPublishTypeDef;
import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.event.CuckooPushVideoCommonEvent;
import com.ailiaovideo.videoline.json.JsonDoRequestGetOssInfo;
import com.ailiaovideo.videoline.json.JsonRequestAddShortVideo;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestCheckVideoCoinRange;
import com.ailiaovideo.videoline.json.JsonRequestGetUploadSign;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.lzy.okgo.callback.StringCallback;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class PushShortVideoActivity extends BaseActivity implements TXUGCPublishTypeDef.ITXVideoPublishListener {

    private KSYTextureView mVideoView;
    public static final String VIDEO_PATH = "VIDEO_PATH";
    public static final String VIDEO_COVER_PATH = "VIDEO_COVER_PATH";

    private Button mBtnChangePay;
    private EditText mEtInputMoney;
    private Button mBtnPushVideo;
    private EditText mEtTitle;

    private String videoPath;
    private String coverPath;
    private boolean isPay = false;

    private String uploadFileVideoCoverThumbUrl;
    private String uploadFileVideoUrl;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_push_short_video;
    }

    @Override
    protected void initView() {

        mBtnChangePay = findViewById(R.id.btn_pay);
        mEtInputMoney = findViewById(R.id.et_money);
        mEtTitle = findViewById(R.id.et_title);
        mBtnPushVideo = findViewById(R.id.btn_push);
        mBtnChangePay.setOnClickListener(this);
        mBtnPushVideo.setOnClickListener(this);
        mVideoView = findViewById(R.id.video_view);

        findViewById(R.id.iv_back).setOnClickListener(this);

    }

    private void initLive() {

        mVideoView.setLooping(true);
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                LogUtils.i("准备就绪开始播放");
                if (mVideoView != null) {
                    // 设置视频伸缩模式，此模式为裁剪模式
                    mVideoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_NOSCALE_TO_FIT);
                    // 开始播放视频
                    mVideoView.start();
                }
            }
        });

        //设置播放地址并准备
        try {
            if (videoPath != null) {

                mVideoView.setDataSource(videoPath);
                mVideoView.prepareAsync();
                LogUtils.i("init 视频播放地址" + videoPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initSet() {
        videoPath = getIntent().getStringExtra(VIDEO_PATH);
        coverPath = getIntent().getStringExtra(VIDEO_COVER_PATH);

        initLive();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_pay:
                isPay = !isPay;

                if (isPay) {
                    mEtInputMoney.setVisibility(View.VISIBLE);
                    mBtnChangePay.setTextColor(getResources().getColor(R.color.admin_color));
                    mBtnChangePay.setBackgroundResource(R.drawable.btn_push_short_video_pay_select);
                } else {
                    mEtInputMoney.setVisibility(View.GONE);
                    mBtnChangePay.setTextColor(getResources().getColor(R.color.white));
                    mBtnChangePay.setBackgroundResource(R.drawable.btn_push_short_video_pay);
                }
                break;
            case R.id.btn_push:

                clickPushVideo();
                break;
            case R.id.iv_back:

                finish();
                break;
        }
    }

    private void clickPushVideo() {

        showLoadingDialog(getString(R.string.test_text));
        String money = mEtInputMoney.getText().toString();
        Api.doCheckVideoCoinRange(money, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestCheckVideoCoinRange data = (JsonRequestCheckVideoCoinRange) JsonRequestBase.getJsonObj(s, JsonRequestCheckVideoCoinRange.class);
                if (data.getCode() == 1) {
                    pushVideo();
                } else {
                    ToastUtils.showShort(data.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }

    private void pushVideo() {

        String money = mEtInputMoney.getText().toString();
        String title = mEtTitle.getText().toString();

        if (TextUtils.isEmpty(title)) {
            showToastMsg(getString(R.string.please_full_video_title));
            return;
        }
        if (isPay && StringUtils.toInt(money) == 0) {
            showToastMsg(getString(R.string.please_full_video_money));
            return;
        }

        try {
            MediaPlayer mediaPalyer = new MediaPlayer();
            mediaPalyer.setDataSource(videoPath);
            mediaPalyer.prepare();
            long time = mediaPalyer.getDuration();
            if (time / 1000 > StringUtils.toInt(ConfigModel.getInitData().getUpload_short_video_time_limit())) {
                ToastUtils.showLong("视频长度超过" + ConfigModel.getInitData().getUpload_short_video_time_limit() + "秒");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        showLoadingDialog(getString(R.string.loading_now_upload_video));
        //获取上传sign
//        Api.doRequestGetUploadSign(uId, uToken, new StringCallback() {
//
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//
//                hideLoadingDialog();
//                JsonRequestGetUploadSign jsonObj = (JsonRequestGetUploadSign) JsonRequestGetUploadSign.getJsonObj(s, JsonRequestGetUploadSign.class);
//                if (jsonObj.getCode() == 1) {
//
//                    if (jsonObj.getSign() == null || TextUtils.isEmpty(jsonObj.getSign())) {
//                        showToastMsg(getString(R.string.upload_fail));
//                        return;
//                    }
//                    doUploadFile(jsonObj.getSign());
//                } else {
//                    showToastMsg(jsonObj.getMsg());
//
//                }
//            }
//
//            @Override
//            public void onError(Call call, Response response, Exception e) {
//                super.onError(call, response, e);
//                hideLoadingDialog();
//            }
//        });

        Api.doRequestGetOSSInfo(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonDoRequestGetOssInfo data = (JsonDoRequestGetOssInfo) JsonRequestBase.getJsonObj(s, JsonDoRequestGetOssInfo.class);
                if (StringUtils.toInt(data.getCode()) == 1) {

                    uploadVideoThumb(data);
                } else {
                    ToastUtils.showLong(data.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });


    }

    //上传视频封面
    private void uploadVideoThumb(final JsonDoRequestGetOssInfo data) {

        File file = new File(coverPath);
        //设置上传后文件的key
        final String upkey = LiveConstant.VIDEO_COVER_IMG_DIR + System.currentTimeMillis() + "_" + file.getName();

        String token = data.getToken();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, upkey, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        uploadFileVideoCoverThumbUrl = data.getDomain() + "/" + upkey;

                        uploadVideo(data);
                    }

                }, null);
    }

    //上传视频
    private void uploadVideo(final JsonDoRequestGetOssInfo data) {

        File file = new File(videoPath);
        //设置上传后文件的key
        final String upkey = LiveConstant.VIDEO_DIR + System.currentTimeMillis() + "_" + file.getName();

        String token = data.getToken();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, upkey, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        uploadFileVideoUrl = data.getDomain() + "/" + upkey;
                        requestAddShortVideo();
                    }


                }, null);
    }

    /**
     * 添加视频记录
     */
    private void requestAddShortVideo() {

        String money = mEtInputMoney.getText().toString();
        String title = mEtTitle.getText().toString();

        HashMap<String, String> local = MyApplication.getInstances().getLocation();

        String lat = "", lng = "";
        if (local.get("lat") != null) {
            lat = local.get("lat");
        }
        if (local.get("lng") != null) {
            lng = local.get("lng");
        }

        showLoadingDialog(getString(R.string.loading_now_upload_video));
        Api.doUploadShortVideo(uId, uToken, isPay ? 2 : 1, money, title, "1111111", uploadFileVideoUrl, uploadFileVideoCoverThumbUrl, lat, lng, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestAddShortVideo jsonObj = (JsonRequestAddShortVideo) JsonRequestAddShortVideo.getJsonObj(s, JsonRequestAddShortVideo.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.upload_success));
                    finish();
                } else {
                    showToastMsg(jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }

    /**
     * 上传视频到腾讯云
     */
    private void doUploadFile(String sign) {

        showLoadingDialog(getString(R.string.loading_now_upload_video));

        TXUGCPublish mVideoPublish = new TXUGCPublish(PushShortVideoActivity.this.getApplicationContext());
        // 文件发布默认是采用断点续传
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        param.signature = sign;                       // 需要填写第四步中计算的上传签名
        // 录制生成的视频文件路径, ITXVideoRecordListener 的 onRecordComplete 回调中可以获取
        param.videoPath = videoPath;
        // 录制生成的视频首帧预览图，ITXVideoRecordListener 的 onRecordComplete 回调中可以获取
        param.coverPath = coverPath;
        mVideoPublish.publishVideo(param);
        mVideoPublish.setListener(this);
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {

        LogUtils.i("上传大小:" + uploadBytes + "总大小:" + totalBytes);

    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {

        switch (result.retCode) {
            case TXUGCPublishTypeDef.PUBLISH_RESULT_OK:

                //requestAddShortVideo(result.videoURL, result.coverURL, result.videoId);
                break;

            default:
                showToastMsg(result.descMsg);
                break;
        }

        CuckooPushVideoCommonEvent event = new CuckooPushVideoCommonEvent();
        EventBus.getDefault().post(event);

        LogUtils.i("上传完成:code" + result.retCode + "msg:" + result.descMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }
}
