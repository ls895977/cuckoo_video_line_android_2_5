package com.ailiaovideo.videoline.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.video.videoupload.TXUGCPublish;
import com.ailiaovideo.video.videoupload.TXUGCPublishTypeDef;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.json.JsonRequestGetUploadSign;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by weipeng on 2018/3/1.
 */

public class UploadVideoHelp implements TXUGCPublishTypeDef.ITXVideoPublishListener {

    private UploadVideoListen uploadVideoListen;
    private String sign;
    private UserModel userModel;
    private TXUGCPublish mVideoPublish;

    private String videoPath,coverPath;

    public UploadVideoHelp() {

        userModel = SaveData.getInstance().getUserInfo();
    }

    public void uploadVideo(String videoPath,String coverPath){

        this.videoPath = videoPath;
        this.coverPath = coverPath;

        if(sign == null){
            //获取上传sign
            doRequestGetSign();
        }else{

            doUploadFile();
        }
    }

    //获取腾讯云上传sign
    private void doRequestGetSign() {

        Api.doRequestGetUploadSign(userModel.getId(), userModel.getToken(),new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetUploadSign jsonObj = (JsonRequestGetUploadSign) JsonRequestGetUploadSign.getJsonObj(s,JsonRequestGetUploadSign.class);
                if(jsonObj.getCode() == 1){

                    if(jsonObj.getSign() == null || TextUtils.isEmpty(jsonObj.getSign())){
                        ToastUtils.showLong("上传失败,请稍后再试!");
                        return;
                    }

                    sign = jsonObj.getSign();
                    doUploadFile();
                }else{
                    ToastUtils.showLong(jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {

                if(uploadVideoListen != null){
                    uploadVideoListen.onGetSignError();
                }
            }
        });
    }

    //上传视频
    private void doUploadFile() {

        mVideoPublish = new TXUGCPublish(MyApplication.getInstances());
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

    //取消上传
    public void cancelUploadVideo(){
        if(mVideoPublish != null){
            mVideoPublish.canclePublish();
        }
    }

    @Override
    public void onPublishProgress(long uploadBytes, long totalBytes) {
        LogUtils.i("上传大小:" + uploadBytes + "总大小:" + totalBytes);
        if(uploadVideoListen != null){
            uploadVideoListen.onPublishProgress(uploadBytes,totalBytes);
        }
    }

    @Override
    public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {

        if(uploadVideoListen != null){
            uploadVideoListen.onUploadVideoCommon(result);
        }

        LogUtils.i("上传完成:code" + result.retCode + "msg:" + result.descMsg);

    }

    public void setUploadVideoListen(UploadVideoListen uploadVideoListen) {
        this.uploadVideoListen = uploadVideoListen;
    }

    public interface UploadVideoListen{

        void onGetSignError();
        void onPublishProgress(long uploadBytes, long totalBytes);
        void onUploadVideoCommon(TXUGCPublishTypeDef.TXPublishResult result);
    }
}
