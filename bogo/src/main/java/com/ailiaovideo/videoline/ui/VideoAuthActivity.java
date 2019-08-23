package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.video.videoupload.TXUGCPublishTypeDef;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.utils.UploadVideoHelp;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCRecord;

import okhttp3.Call;
import okhttp3.Response;

public class VideoAuthActivity extends BaseActivity implements TXRecordCommon.ITXVideoRecordListener, View.OnTouchListener, UploadVideoHelp.UploadVideoListen {

    private Button mBtnRecord;
    private QMUIProgressBar mRecordProgress;
    private LinearLayout mLayoutNext, mLayoutRecord;
    private ImageView mIvRestart, mIvNext;

    private TXUGCRecord mTXCameraRecord;
    private int minDuration = 3000;
    private int maxDuration = 10000;    //视频录制的最大时长ms
    //开始录制时间
    private long startRecordVideoTime;

    private TXRecordCommon.TXRecordResult txRecordResult;
    private UploadVideoHelp uploadVideoHelp;


    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_video_auth;
    }

    @Override
    protected void initView() {
        mBtnRecord = findViewById(R.id.btn_record);
        mRecordProgress = findViewById(R.id.pg_bar);
        mLayoutNext = findViewById(R.id.ll_record_next);
        mLayoutRecord = findViewById(R.id.ll_record_content);
        mIvRestart = findViewById(R.id.iv_restart);
        mIvNext = findViewById(R.id.iv_next);

        mBtnRecord.setOnTouchListener(this);
        mIvRestart.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
    }

    @Override
    protected void initSet() {
        setVideoParam();

    }

    @Override
    protected void initData() {

        uploadVideoHelp = new UploadVideoHelp();
        uploadVideoHelp.setUploadVideoListen(this);
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_restart:

                restartRecord();
                break;
            case R.id.iv_next:

                uploadVideo();
                break;
            default:
                break;
        }
    }


    private void setVideoParam() {

        mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        mTXCameraRecord.setVideoRecordListener(this);                    //设置录制回调
        TXCloudVideoView mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);    //准备一个预览摄像头画面的
        mVideoView.enableHardwareDecode(true);
        TXRecordCommon.TXUGCSimpleConfig param = new TXRecordCommon.TXUGCSimpleConfig();
        //param.videoQuality = TXRecordCommon.VIDEO_QUALITY_LOW;        // 360p
        param.videoQuality = TXRecordCommon.VIDEO_QUALITY_MEDIUM;        // 540p
        // param.videoQuality = TXRecordCommon.VIDEO_QUALITY_HIGH;        // 720p
        param.isFront = true;           //是否前置摄像头，使用
        param.minDuration = minDuration;    //视频录制的最小时长ms
        param.maxDuration = maxDuration;    //视频录制的最大时长ms
        mTXCameraRecord.startCameraSimplePreview(param, mVideoView);
    }


    //上传视频
    private void uploadVideo() {

        if (txRecordResult != null) {
            //上传视频
            showLoadingDialog(getString(R.string.loading_now_upload_video));
            uploadVideoHelp.uploadVideo(txRecordResult.videoPath, txRecordResult.coverPath);
        }
    }

    //提交认证
    private void requestSubmitAuth(String videoUrl, String coverUrl, String videoId) {

        showLoadingDialog(getString(R.string.loading_upload_data));
        Api.doSubmitAuthVideo(uId, uToken, videoUrl, coverUrl, videoId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() != 1) {
                    showToastMsg(jsonObj.getMsg());
                }
                showToastMsg(getString(R.string.submit_success));
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                hideLoadingDialog();
                showToastMsg(getString(R.string.submit_fail));
            }
        });
    }

    //重新开始
    private void restartRecord() {

        mRecordProgress.setProgress(0);
        mLayoutNext.setVisibility(View.GONE);
        mLayoutRecord.setVisibility(View.VISIBLE);

        mTXCameraRecord.getPartsManager().deleteAllParts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXCameraRecord.getPartsManager().deleteAllParts();
        mTXCameraRecord.stopCameraPreview();
        mTXCameraRecord.release();

    }


    @Override
    public void onRecordEvent(int event, Bundle bundle) {
        LogUtils.d("onRecordEvent event id = " + event);
        if (event == TXRecordCommon.EVT_ID_PAUSE) {

        } else if (event == TXRecordCommon.EVT_CAMERA_CANNOT_USE) {
            Toast.makeText(this, R.string.open_camera_error, Toast.LENGTH_SHORT).show();
        } else if (event == TXRecordCommon.EVT_MIC_CANNOT_USE) {
            Toast.makeText(this, R.string.open_audio_error, Toast.LENGTH_SHORT).show();
        } else if (event == TXRecordCommon.EVT_ID_RESUME) {

        }
    }

    @Override
    public void onRecordProgress(long l) {
        float total = (float) l / (float) maxDuration;

        LogUtils.i("进度:" + total * 100);
        mRecordProgress.setProgress((int) (total * 100));
    }

    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult txRecordResult) {

        LogUtils.i("视频录制Complete:retCode" + txRecordResult.retCode + "- msg:" + txRecordResult.descMsg
                + "视频路径:" + txRecordResult.videoPath + " 视频预览图片:" + txRecordResult.coverPath);
        switch (txRecordResult.retCode) {

            case TXRecordCommon.RECORD_RESULT_OK:
            case TXRecordCommon.RECORD_RESULT_OK_LESS_THAN_MINDURATION:

                recordOk();
                break;
            case TXRecordCommon.RECORD_RESULT_OK_REACHED_MAXDURATION:
                recordOk();
                break;
            default:
                showToastMsg(txRecordResult.descMsg);
                break;
        }
        this.txRecordResult = txRecordResult;
    }

    private void recordOk() {
        //发布编辑
        mLayoutRecord.setVisibility(View.GONE);
        mLayoutNext.setVisibility(View.VISIBLE);
    }

    //点击视频录制按钮
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            mBtnRecord.setText("");
            mBtnRecord.setBackgroundResource(R.drawable.bg_auth_video_record_btn);
            restartRecord();
            mTXCameraRecord.startRecord();
            startRecordVideoTime = System.currentTimeMillis();

            //LogUtils.i("视频录制按钮焦点状态:按下");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            mTXCameraRecord.stopRecord();
            //时间是否满足
            long endTime = System.currentTimeMillis() - startRecordVideoTime;
            startRecordVideoTime = 0;
            if (endTime < minDuration) {
                showToastMsg(getString(R.string.video_min_time));
            }
            mBtnRecord.setText(R.string.click_beat);
            mBtnRecord.setBackgroundResource(R.drawable.bg_auth_video_record_btn_up);

            //LogUtils.i("视频录制按钮焦点状态:抬起");
        }

        return false;
    }

    @Override
    public void onGetSignError() {
        hideLoadingDialog();
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {

    }

    @Override
    public void onUploadVideoCommon(TXUGCPublishTypeDef.TXPublishResult result) {

        hideLoadingDialog();
        switch (result.retCode) {
            case TXUGCPublishTypeDef.PUBLISH_RESULT_OK:

                requestSubmitAuth(result.videoURL, result.coverURL, result.videoId);
                break;

            default:
                //showToastMsg(result.descMsg);
                break;
        }
    }
}
