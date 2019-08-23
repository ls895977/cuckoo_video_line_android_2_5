package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCRecord;

public class VideoRecordActivity extends BaseActivity implements TXRecordCommon.ITXVideoRecordListener, View.OnTouchListener {

    private TXUGCRecord mTXCameraRecord;
    private ImageView mIvBack;
    private ImageView mIvSwitchCamera;
    private Button mBtnRecord;
    private QMUIProgressBar mRecordProgress;
    private boolean camera = true;
    //开始录制时间
    private long startRecordVideoTime;
    private int minDuration = 5000;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_video_record;
    }

    @Override
    protected void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mIvSwitchCamera = findViewById(R.id.iv_switch_camera);
        mBtnRecord = findViewById(R.id.btn_record);
        mRecordProgress = findViewById(R.id.pg_bar);
        mIvBack.setOnClickListener(this);
        mIvSwitchCamera.setOnClickListener(this);
        mBtnRecord.setOnTouchListener(this);
        mRecordProgress.setMaxValue(100);
        //LogUtils.i("视频录制按钮焦点状态:" + b);
    }

    @Override
    protected void initSet() {

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
        param.maxDuration = 60000;    //视频录制的最大时长ms
        mTXCameraRecord.startCameraSimplePreview(param, mVideoView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    //重新开始
    private void restartRecord() {

        mRecordProgress.setProgress(0);
        mTXCameraRecord.getPartsManager().deleteAllParts();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                finish();
                break;
            case R.id.iv_switch_camera:
                camera = !camera;
                mTXCameraRecord.switchCamera(camera);
                break;
        }
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
            Toast.makeText(this, getString(R.string.open_camera_error), Toast.LENGTH_SHORT).show();
        } else if (event == TXRecordCommon.EVT_MIC_CANNOT_USE) {
            Toast.makeText(this, getString(R.string.open_audio_error), Toast.LENGTH_SHORT).show();
        } else if (event == TXRecordCommon.EVT_ID_RESUME) {

        }
    }

    @Override
    public void onRecordProgress(long l) {
        float total = (float) l / (float) 60000;

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
                recordOk(txRecordResult);
                break;

            case TXRecordCommon.RECORD_RESULT_OK_REACHED_MAXDURATION:
                recordOk(txRecordResult);
            default:
                //showToastMsg(txRecordResult.descMsg);
                break;
        }
    }

    private void recordOk(TXRecordCommon.TXRecordResult txRecordResult) {
        //发布编辑
        Intent intent = new Intent(this, PushShortVideoActivity.class);
        intent.putExtra(PushShortVideoActivity.VIDEO_PATH, txRecordResult.videoPath);
        intent.putExtra(PushShortVideoActivity.VIDEO_COVER_PATH, txRecordResult.coverPath);
        startActivity(intent);
        finish();
    }

    //点击视频录制按钮
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            mBtnRecord.setText("");
            mBtnRecord.setBackgroundResource(R.drawable.bg_video_record_btn_up);
            restartRecord();
            startRecord();
            startRecordVideoTime = System.currentTimeMillis();

            //LogUtils.i("视频录制按钮焦点状态:按下");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            mTXCameraRecord.stopRecord();
            //时间是否满足
            long endTime = System.currentTimeMillis() - startRecordVideoTime;
            startRecordVideoTime = 0;
            if (endTime < minDuration) {
                showToastMsg(getString(R.string.video_record_min_time));
            }

            mBtnRecord.setText(getString(R.string.click_beat));
            mBtnRecord.setBackgroundResource(R.drawable.bg_video_record_btn);


            //LogUtils.i("视频录制按钮焦点状态:抬起");
        }

        return false;
    }

    private void startRecord() {

        int result = mTXCameraRecord.startRecord();
        if (result != 0) {
            showToastMsg("录制失败，错误码：" + result);
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.stopRecord();
        }
    }
}
