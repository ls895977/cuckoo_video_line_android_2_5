package com.ailiaovideo.videoline.modle;

import java.util.ArrayList;
import java.util.List;

/**
 * player模型类
 * Created by fly on 2017/12/23 0023.
 */
public class PlayerModle {
    //当前player的oid
    public Integer oid;
    //当前player的昵称
    public String thisPlayerName;
    //当前player的粉丝数量
    public Integer fansNumber;
    //当前player所在位置
    public Integer location;
    //当前player关注数量
    public Integer loveNumber;
    //历史最高等级
    public int maxGrade;
    //当前等级
    public int nowGrade;
    //好评数量(百分比)
    public double niceNumber;
    //通话时间
    public long callTime;
    //是否在线:1在线0离线
    public int isOnLine;
    //是否已认证:1已认证0未认证
    public int isAuthentication;
    //性别:1男0女
    public int gender;
    //收到的礼物列表
    public List<Object> giftList;
    //上传的小视频列表
    public List<Object> videoList;
    //上传的照片列表
    public List<Object> photoList;

    public PlayerModle() {
        super();
    }

    public PlayerModle(Integer oid, String thisPlayerName, Integer fansNumber, Integer location, Integer loveNumber, int maxGrade, Integer nowGrade, double niceNumber, long callTime, int isOnLine, int isAuthentication, int gender, List<Object> giftList, List<Object> videoList, List<Object> photoList) {
        this.oid = oid;
        this.thisPlayerName = thisPlayerName;
        this.fansNumber = fansNumber;
        this.location = location;
        this.loveNumber = loveNumber;
        this.maxGrade = maxGrade;
        this.nowGrade = nowGrade;
        this.niceNumber = niceNumber;
        this.callTime = callTime;
        this.isOnLine = isOnLine;
        this.isAuthentication = isAuthentication;
        this.gender = gender;
        this.giftList = giftList;
        this.videoList = videoList;
        this.photoList = photoList;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getThisPlayerName() {
        return thisPlayerName;
    }

    public void setThisPlayerName(String thisPlayerName) {
        this.thisPlayerName = thisPlayerName;
    }

    public Integer getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(Integer fansNumber) {
        this.fansNumber = fansNumber;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getLoveNumber() {
        return loveNumber;
    }

    public void setLoveNumber(Integer loveNumber) {
        this.loveNumber = loveNumber;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(int maxGrade) {
        this.maxGrade = maxGrade;
    }

    public Integer getNowGrade() {
        return nowGrade;
    }

    public void setNowGrade(Integer nowGrade) {
        this.nowGrade = nowGrade;
    }

    /**
     * 返回百分比好评占比
     * @return
     */
    public double getNiceNumber() {
        return niceNumber*100;
    }

    public void setNiceNumber(double niceNumber) {
        this.niceNumber = niceNumber;
    }

    /**
     * 返回通话时间(单位:小时)
     * @return
     */
    public long getCallTime() {
        return callTime/60;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    public int getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(int isOnLine) {
        this.isOnLine = isOnLine;
    }

    public int getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(int isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public List<Object> getGiftList() {
        return giftList == null ? new ArrayList<Object>() : giftList;
    }

    public void setGiftList(List<Object> giftList) {
        this.giftList = giftList;
    }

    public List<Object> getVideoList() {
        return videoList == null ? new ArrayList<Object>() : videoList;
    }

    public void setVideoList(List<Object> videoList) {
        this.videoList = videoList;
    }

    public List<Object> getPhotoList() {
        return photoList == null ? new ArrayList<Object>() : photoList;
    }

    public void setPhotoList(List<Object> photoList) {
        this.photoList = photoList;
    }
}
