package com.ailiaovideo.videoline.json.jsonmodle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 小视频视频基本信息
 * Created by jiahengfei on 2018/1/25 0025.
 */

public class VideoModel implements Parcelable {

    private String id;//视频id
    private String uid;//主播id
    private String title;//视频标题
    private String video_url;//视频地址
    private String img;//视频图片
    private String viewed;//观看次数
    private String coin;//收费金额
    private String share;//分享次数
    private String addtime;//上传时间
    private String status;//1免费2收费
    private String attention;//用户是否关注本主播视频1已关注 0未关注
    private String count;//统计本视频关注总数
    private String follow_num;//点赞数量
    private String type;
    private String is_follow;

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(String follow_num) {
        this.follow_num = follow_num;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {

        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }


    public String getImg() {
        return img;
    }

    public String getViewed() {
        return viewed;
    }

    public String getCoin() {
        return coin;
    }

    public String getShare() {
        return share;
    }

    public String getAddtime() {
        return addtime;
    }

    public String getStatus() {
        return status;
    }

    public String getAttention() {
        return attention;
    }

    public String getCount() {
        return count;
    }

    public VideoModel(String id, String uid, String title, String video_url, String img, String viewed, String coin, String share, String addtime, String status, String attention, String count) {

        this.id = id;
        this.uid = uid;
        this.title = title;
        this.video_url = video_url;
        this.img = img;
        this.viewed = viewed;
        this.coin = coin;
        this.share = share;
        this.addtime = addtime;
        this.status = status;
        this.attention = attention;
        this.count = count;
    }

    public VideoModel() {
        super();
    }

    @Override
    public String toString() {
        return "VideoData{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", address='" + video_url + '\'' +
                ", img='" + img + '\'' +
                ", viewed='" + viewed + '\'' +
                ", coin='" + coin + '\'' +
                ", share='" + share + '\'' +
                ", addtime='" + addtime + '\'' +
                ", status='" + status + '\'' +
                ", attention='" + attention + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.title);
        dest.writeString(this.video_url);
        dest.writeString(this.img);
        dest.writeString(this.viewed);
        dest.writeString(this.coin);
        dest.writeString(this.share);
        dest.writeString(this.addtime);
        dest.writeString(this.status);
        dest.writeString(this.attention);
        dest.writeString(this.count);
        dest.writeString(this.follow_num);
    }

    protected VideoModel(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.title = in.readString();
        this.video_url = in.readString();
        this.img = in.readString();
        this.viewed = in.readString();
        this.coin = in.readString();
        this.share = in.readString();
        this.addtime = in.readString();
        this.status = in.readString();
        this.attention = in.readString();
        this.count = in.readString();
        this.follow_num = in.readString();
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel source) {
            return new VideoModel(source);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };
}
