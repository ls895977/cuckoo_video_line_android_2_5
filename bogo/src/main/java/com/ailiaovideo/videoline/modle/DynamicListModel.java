package com.ailiaovideo.videoline.modle;

import android.os.Parcel;
import android.os.Parcelable;

import com.ailiaovideo.videoline.utils.StringUtils;

import java.util.List;

public class DynamicListModel implements Parcelable {


    /**
     * id : 51
     * is_audio : 0
     * audio_file :
     * publish_time : 26分钟前
     * msg_content :
     * uid : 100169
     * comment_count : 0
     * userInfo : {"id":100169,"avatar":"http://p4ulgsz1p.bkt.clouddn.com/08ed2201807281647316370.jpg","user_nickname":"船舶","sex":1,"level":1,"coin":1500,"user_status":2}
     * originalPicUrls : ["http://p4ulgsz1p.bkt.clouddn.com/9fcf6201808061806211168.png","http://p4ulgsz1p.bkt.clouddn.com/294e1201808061806221892.png"]
     * thumbnailPicUrls : ["http://p4ulgsz1p.bkt.clouddn.com/9fcf6201808061806211168.png","http://p4ulgsz1p.bkt.clouddn.com/294e1201808061806221892.png"]
     * is_like : 0
     * like_count : 0
     */

    private String id;
    private String is_audio;
    private String audio_file;
    private String publish_time;
    private String msg_content;
    private String uid;
    private String comment_count;
    private UserModel userInfo;
    private String is_like;
    private String like_count;
    private List<String> originalPicUrls;
    private List<String> thumbnailPicUrls;

    public void plusLikeCount(int count){
        like_count = String.valueOf(StringUtils.toInt(like_count) + count);
    }

    public void decLikeCount(int count){
        like_count = String.valueOf(StringUtils.toInt(like_count) - count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_audio() {
        return is_audio;
    }

    public void setIs_audio(String is_audio) {
        this.is_audio = is_audio;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public static Creator<DynamicListModel> getCREATOR() {
        return CREATOR;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }


    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public List<String> getOriginalPicUrls() {
        return originalPicUrls;
    }

    public void setOriginalPicUrls(List<String> originalPicUrls) {
        this.originalPicUrls = originalPicUrls;
    }

    public List<String> getThumbnailPicUrls() {
        return thumbnailPicUrls;
    }

    public void setThumbnailPicUrls(List<String> thumbnailPicUrls) {
        this.thumbnailPicUrls = thumbnailPicUrls;
    }

    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.is_audio);
        dest.writeString(this.audio_file);
        dest.writeString(this.publish_time);
        dest.writeString(this.msg_content);
        dest.writeString(this.uid);
        dest.writeString(this.comment_count);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeString(this.is_like);
        dest.writeString(this.like_count);
        dest.writeStringList(this.originalPicUrls);
        dest.writeStringList(this.thumbnailPicUrls);
    }

    public DynamicListModel() {
    }

    protected DynamicListModel(Parcel in) {
        this.id = in.readString();
        this.is_audio = in.readString();
        this.audio_file = in.readString();
        this.publish_time = in.readString();
        this.msg_content = in.readString();
        this.uid = in.readString();
        this.comment_count = in.readString();
        this.userInfo = in.readParcelable(UserModel.class.getClassLoader());
        this.is_like = in.readString();
        this.like_count = in.readString();
        this.originalPicUrls = in.createStringArrayList();
        this.thumbnailPicUrls = in.createStringArrayList();
    }

    public static final Creator<DynamicListModel> CREATOR = new Creator<DynamicListModel>() {
        @Override
        public DynamicListModel createFromParcel(Parcel source) {
            return new DynamicListModel(source);
        }

        @Override
        public DynamicListModel[] newArray(int size) {
            return new DynamicListModel[size];
        }
    };
}
