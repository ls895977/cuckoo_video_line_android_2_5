package com.ailiaovideo.videoline.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户视频聊数据传输对象
 * Created by jiahengfei on 2018/2/2 0002.
 */

public class UserChatData implements Parcelable {

    private UserModel userModel;//id
    private String channelName;//通道名称
    private String str;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public UserChatData() {
        super();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userModel, flags);
        dest.writeString(this.channelName);
        dest.writeString(this.str);
    }

    protected UserChatData(Parcel in) {
        this.userModel = in.readParcelable(UserModel.class.getClassLoader());
        this.channelName = in.readString();
        this.str = in.readString();
    }

    public static final Creator<UserChatData> CREATOR = new Creator<UserChatData>() {
        @Override
        public UserChatData createFromParcel(Parcel source) {
            return new UserChatData(source);
        }

        @Override
        public UserChatData[] newArray(int size) {
            return new UserChatData[size];
        }
    };
}
