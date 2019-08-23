package com.ailiaovideo.videoline.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weipeng on 2018/2/14.
 */

public class UserModel implements Parcelable {


    private String id;
    private String token;
    private int sex;
    private String user_nickname;
    private String avatar;
    private String address;
    private String sdkappid;
    private int is_reg_perfect;
    private String user_sign;
    private String is_open_do_not_disturb;
    private String level;
    private String yunxin_sign;

    public String getYunxin_sign() {
        return yunxin_sign;
    }

    public void setYunxin_sign(String yunxin_sign) {
        this.yunxin_sign = yunxin_sign;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIs_open_do_not_disturb() {
        return is_open_do_not_disturb;
    }

    public void setIs_open_do_not_disturb(String is_open_do_not_disturb) {
        this.is_open_do_not_disturb = is_open_do_not_disturb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSdkappid() {
        return sdkappid;
    }

    public void setSdkappid(String sdkappid) {
        this.sdkappid = sdkappid;
    }


    public String getUser_sign() {
        return user_sign;
    }

    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
    }

    public int getIs_reg_perfect() {
        return is_reg_perfect;
    }

    public void setIs_reg_perfect(int is_reg_perfect) {
        this.is_reg_perfect = is_reg_perfect;
    }

    public UserModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.token);
        dest.writeInt(this.sex);
        dest.writeString(this.user_nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.address);
        dest.writeString(this.sdkappid);
        dest.writeInt(this.is_reg_perfect);
        dest.writeString(this.user_sign);
        dest.writeString(this.is_open_do_not_disturb);
    }

    protected UserModel(Parcel in) {
        this.id = in.readString();
        this.token = in.readString();
        this.sex = in.readInt();
        this.user_nickname = in.readString();
        this.avatar = in.readString();
        this.address = in.readString();
        this.sdkappid = in.readString();
        this.is_reg_perfect = in.readInt();
        this.user_sign = in.readString();
        this.is_open_do_not_disturb = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
