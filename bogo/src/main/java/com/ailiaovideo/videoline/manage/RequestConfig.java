package com.ailiaovideo.videoline.manage;

import com.ailiaovideo.videoline.modle.ConfigModel;

/**
 * 请求配置
 */

public class RequestConfig {
    private static RequestConfig configObj;
    /**
     * 用户配置信息
     */
    private long requestIntervalIsOnLine;//用户请求在线时间间隔
    private String currency = "萌币";//货币
    private String app_qgorq_key;//声网的appid
    private String app_certificate;//声网的appCertificate
    private String aisleName;//通道名称(视频聊)
    private String groupId;//在线成员广播大群id
    private String sdkappid;//腾讯云APPID
    private String account_type;//腾讯云ACCOUNT_TYPE
    private String privatePhotosCoin;
    private String systemMessage;//系统公告消息
    private String splashUrl;
    private String splashImage;

    /**
     * 直播心跳间隔
     */
    private String tab_live_heart_time;


    /**
     * 获取实例
     *
     * @return
     */
    public static RequestConfig getConfigObj() {
        if (configObj == null) {
            configObj = new RequestConfig();
            return configObj;
        } else {
            return configObj;
        }
    }


    public String getSystemMessage() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getSystem_message() != null) {
            return configObj.getApp_h5().getSystem_message();
        }
        return "";
    }

    /**
     * 新人引导页面的h5
     */
    public String getNewBitGuideUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getNewbie_guide() != null) {
            return configObj.getApp_h5().getNewbie_guide();
        }
        return "";
    }

    /**
     * 分享注册页面
     */
    public String getInviteShareRegUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getInvite_reg_url() != null) {
            return configObj.getApp_h5().getInvite_reg_url();
        }
        return "";
    }


    /**
     * 转盘活动
     */
    public String getLuckyCorinUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getTurntable_url() != null) {
            return configObj.getApp_h5().getTurntable_url();
        }
        return "";
    }

    /**
     * 关于我们
     */
    public String getAboutUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getAbout_me() != null) {
            return configObj.getApp_h5().getAbout_me();
        }
        return "";
    }

    /**
     * 我的等级h5
     */
    public String getMyLevelUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getMy_Level() != null) {
            return configObj.getApp_h5().getMy_Level();
        }
        return "";
    }

    /**
     * 邀请好友H5页面
     */
    public String getInviteFirendsUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getInvite_friends() != null) {
            return configObj.getApp_h5().getInvite_friends();
        }
        return "";
    }

    /**
     * 徒弟贡献榜H5页面
     */
    public String getDiscipleContributionUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getDisciple_contribution() != null) {
            return configObj.getApp_h5().getDisciple_contribution();
        }
        return "";
    }

    /**
     * 我的详细资料
     */
    public String getMyDetailUrl() {
        ConfigModel configObj = ConfigModel.getInitData();
        if (configObj.getApp_h5() != null && configObj.getApp_h5().getMy_detail() != null) {
            return configObj.getApp_h5().getMy_detail();
        }
        return "";
    }

    @Override
    public String toString() {
        return "RequestConfig{" +
                "requestIntervalIsOnLine=" + requestIntervalIsOnLine +
                ", currency='" + currency + '\'' +
                ", ogorqAppId='" + app_qgorq_key + '\'' +
                ", AppCertificate='" + app_certificate + '\'' +
                ", aisleName='" + aisleName + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }


    public String getSplashUrl() {
        return splashUrl;
    }

    public void setSplashUrl(String splashUrl) {
        this.splashUrl = splashUrl;
    }

    public String getSplashImage() {
        return splashImage;
    }

    public void setSplashImage(String splashImage) {
        this.splashImage = splashImage;
    }

    public String getMainSystemMessage() {

        return systemMessage;
    }

    public void setMainSystemMessage(String systemMessage) {

        this.systemMessage = systemMessage;
    }

    public String getPrivatePhotosCoin() {
        return privatePhotosCoin;
    }

    public void setPrivatePhotosCoin(String privatePhotosCoin) {
        this.privatePhotosCoin = privatePhotosCoin;
    }

    public String getTabLiveHeartTime() {
        return tab_live_heart_time;
    }

    public void setTabLiveHeartTime(String tab_live_heart_time) {
        this.tab_live_heart_time = tab_live_heart_time;
    }

    public String getApp_qgorq_key() {
        return app_qgorq_key;
    }

    public void setApp_qgorq_key(String app_qgorq_key) {
        this.app_qgorq_key = app_qgorq_key;
    }

    public String getApp_certificate() {
        return app_certificate;
    }

    public void setApp_certificate(String app_certificate) {
        this.app_certificate = app_certificate;
    }

    public String getSdkappid() {
        return sdkappid;
    }

    public void setSdkappid(String sdkappid) {
        this.sdkappid = sdkappid;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public static void setConfigObj(RequestConfig configObj) {
        RequestConfig.configObj = configObj;
    }

    public long getRequestIntervalIsOnLine() {
        return requestIntervalIsOnLine;
    }

    public void setRequestIntervalIsOnLine(long requestIntervalIsOnLine) {
        this.requestIntervalIsOnLine = requestIntervalIsOnLine;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAppCertificate() {
        return app_certificate;
    }

    public void setAppCertificate(String appCertificate) {
        app_certificate = appCertificate;
    }

    public String getAisleName() {
        return aisleName;
    }

    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public RequestConfig() {
        super();
    }

    public RequestConfig(long requestIntervalIsOnLine, String currency, String ogorqAppId, String appCertificate, String aisleName, String groupId) {
        this.requestIntervalIsOnLine = requestIntervalIsOnLine;
        this.currency = currency;
        this.app_qgorq_key = ogorqAppId;
        this.app_certificate = appCertificate;
        this.aisleName = aisleName;
        this.groupId = groupId;
    }

    public int getHas_dirty_words() {
        return 1;
    }
}
