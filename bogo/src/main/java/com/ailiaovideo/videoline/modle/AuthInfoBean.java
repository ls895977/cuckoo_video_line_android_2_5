package com.ailiaovideo.videoline.modle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AuthInfoBean implements Parcelable {


    private String height;
    private String weight;
    private String constellation;
    private String city;
    private String image_label;
    private String introduce;
    private String sign;
    private String status;
    private List<String> evaluate_list;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage_label() {
        return image_label;
    }

    public void setImage_label(String image_label) {
        this.image_label = image_label;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getEvaluate_list() {
        return evaluate_list;
    }

    public void setEvaluate_list(List<String> evaluate_list) {
        this.evaluate_list = evaluate_list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.height);
        dest.writeString(this.weight);
        dest.writeString(this.constellation);
        dest.writeString(this.city);
        dest.writeString(this.image_label);
        dest.writeString(this.introduce);
        dest.writeString(this.sign);
        dest.writeString(this.status);
        dest.writeList(this.evaluate_list);
    }

    public AuthInfoBean() {
    }

    protected AuthInfoBean(Parcel in) {
        this.height = in.readString();
        this.weight = in.readString();
        this.constellation = in.readString();
        this.city = in.readString();
        this.image_label = in.readString();
        this.introduce = in.readString();
        this.sign = in.readString();
        this.status = in.readString();
        this.evaluate_list = new ArrayList<String>();
        in.readList(this.evaluate_list, CuckooUserEvaluateListModel.class.getClassLoader());
    }

    public static final Creator<AuthInfoBean> CREATOR = new Creator<AuthInfoBean>() {
        @Override
        public AuthInfoBean createFromParcel(Parcel source) {
            return new AuthInfoBean(source);
        }

        @Override
        public AuthInfoBean[] newArray(int size) {
            return new AuthInfoBean[size];
        }
    };
}
