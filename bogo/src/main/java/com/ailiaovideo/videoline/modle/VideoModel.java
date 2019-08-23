package com.ailiaovideo.videoline.modle;

import android.net.Uri;

/**
 * video模型类
 * Created by fly on 2017/12/23 0023.
 * @author 山东布谷鸟网络科技有限公司著
 */
public class VideoModel {
    //视频对象播放地址
    public Uri videoUri;
    //视频对象标题
    public String videoTitle;
    //视频分享数量
    public Integer sharePlayerNumber;
    //视频点赞数量
    public Integer lovePlayerNumber;
    //关联player的oid
    public Integer fromPlayerOid;

    public VideoModel() {
        super();
    }

    public VideoModel(Uri videoUri, String videoTitle, Integer sharePlayerNumber, Integer lovePlayerNumber, Integer fromPlayerOid) {
        this.videoUri = videoUri;
        this.videoTitle = videoTitle;
        this.sharePlayerNumber = sharePlayerNumber;
        this.lovePlayerNumber = lovePlayerNumber;
        this.fromPlayerOid = fromPlayerOid;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public Integer getSharePlayerNumber() {
        return sharePlayerNumber;
    }

    public void setSharePlayerNumber(Integer sharePlayerNumber) {
        this.sharePlayerNumber = sharePlayerNumber;
    }

    public Integer getLovePlayerNumber() {
        return lovePlayerNumber;
    }

    public void setLovePlayerNumber(Integer lovePlayerNumber) {
        this.lovePlayerNumber = lovePlayerNumber;
    }

    public Integer getFromPlayerOid() {
        return fromPlayerOid;
    }

    public void setFromPlayerOid(Integer fromPlayerOid) {
        this.fromPlayerOid = fromPlayerOid;
    }
}
