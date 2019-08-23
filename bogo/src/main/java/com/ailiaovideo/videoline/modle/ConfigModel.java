package com.ailiaovideo.videoline.modle;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.json.jsonmodle.Html5PageUrlData;
import com.ailiaovideo.videoline.manage.JsonDataManage;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.utils.StringUtils;

import java.util.ArrayList;

/**
 * 配置信息对象
 * Created by jiahengfei on 2018/1/22 0022.
 */

public class ConfigModel {
    /**
     * 用户请求在线##时间间隔
     */
    private String heartbeat;
    /**
     * 视频聊通道名称
     */
    private String channel;
    /**
     * 在线成员广播大群id
     */
    private String group_id;

    /*
     * 声网 appid
     * */
    private String app_qgorq_key;

    /*
     * 声网 app_certificate
     * */
    private String app_certificate;
    /*
     * 腾讯云APPID
     * */
    private String sdkappid;

    /*
     * 腾讯云ACCOUNT_TYPE
     * */
    private String account_type;

    /**
     * 分钟扣费金额
     */
    private String video_deduction;

    /**
     * 直播心跳间隔
     */
    private String tab_live_heart_time;

    private String private_photos;

    //系统公告
    private String system_message;

    private String splash_url;

    private String splash_img_url;

    private String splash_content;

    //是否开启自定义金额模块
    private int open_custom_video_charge_coin;

    //脏字库
    private String dirty_word;

    //客服QQ
    private String custom_service_qq;

    //安卓版本号
    private String android_version;

    //是否强制升级
    private int is_force_upgrade;

    //升级内容
    private String android_app_update_des;

    //下载地址
    private String android_download_url;

    //货币名称
    private String currency_name;

    private String is_open_chat_pay;
    private String private_chat_money;
    private String video_call_msg_alert;

    private String open_invite;
    private String open_video_chat;

    //微信APP_ID
    private String wx_appid;

    //第三方登录开关
    private int open_login_qq;
    private int open_login_wx;
    private int open_login_facebook;

    private String pay_pal_client_id;
    private int open_pay_pal;
    private int open_sandbox;

    private String share_title;
    private String share_content;

    private String upload_short_video_time_limit;
    private String upload_certification;

    private String rechage_message_contact;

    private int screenshots_time;

    public int getScreenshots_time() {
        return screenshots_time;
    }

    public void setScreenshots_time(int screenshots_time) {
        this.screenshots_time = screenshots_time;
    }

    public String getRechage_message_contact() {
        return rechage_message_contact;
    }

    public void setRechage_message_contact(String rechage_message_contact) {
        this.rechage_message_contact = rechage_message_contact;
    }

    public String getUpload_certification() {
        return upload_certification;
    }

    public void setUpload_certification(String upload_certification) {
        this.upload_certification = upload_certification;
    }

    public String getUpload_short_video_time_limit() {
        return upload_short_video_time_limit;
    }

    public void setUpload_short_video_time_limit(String upload_short_video_time_limit) {
        this.upload_short_video_time_limit = upload_short_video_time_limit;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public int getOpen_sandbox() {
        return open_sandbox;
    }

    public void setOpen_sandbox(int open_sandbox) {
        this.open_sandbox = open_sandbox;
    }

    public String getPay_pal_client_id() {
        return pay_pal_client_id;
    }

    public void setPay_pal_client_id(String pay_pal_client_id) {
        this.pay_pal_client_id = pay_pal_client_id;
    }

    public int getOpen_pay_pal() {
        return open_pay_pal;
    }

    public void setOpen_pay_pal(int open_pay_pal) {
        this.open_pay_pal = open_pay_pal;
    }

    public int getOpen_login_qq() {
        return open_login_qq;
    }

    public void setOpen_login_qq(int open_login_qq) {
        this.open_login_qq = open_login_qq;
    }

    public int getOpen_login_wx() {
        return open_login_wx;
    }

    public void setOpen_login_wx(int open_login_wx) {
        this.open_login_wx = open_login_wx;
    }

    public int getOpen_login_facebook() {
        return open_login_facebook;
    }

    public void setOpen_login_facebook(int open_login_facebook) {
        this.open_login_facebook = open_login_facebook;
    }

    public String getWx_appid() {
        return wx_appid;
    }

    public void setWx_appid(String wx_appid) {
        this.wx_appid = wx_appid;
    }

    public String getOpen_video_chat() {
        return open_video_chat;
    }

    public void setOpen_video_chat(String open_video_chat) {
        this.open_video_chat = open_video_chat;
    }

    public String getOpen_invite() {
        return open_invite;
    }

    public void setOpen_invite(String open_invite) {
        this.open_invite = open_invite;
    }

    private ArrayList<StarLevelModel> star_level_list;

    public ArrayList<StarLevelModel> getStar_level_list() {
        return star_level_list;
    }

    public void setStar_level_list(ArrayList<StarLevelModel> star_level_list) {
        this.star_level_list = star_level_list;
    }

    public String getVideo_call_msg_alert() {
        return video_call_msg_alert;
    }

    public void setVideo_call_msg_alert(String video_call_msg_alert) {
        this.video_call_msg_alert = video_call_msg_alert;
    }

    public String getIs_open_chat_pay() {
        return is_open_chat_pay;
    }

    public void setIs_open_chat_pay(String is_open_chat_pay) {
        this.is_open_chat_pay = is_open_chat_pay;
    }

    public String getPrivate_chat_money() {
        return private_chat_money;
    }

    public void setPrivate_chat_money(String private_chat_money) {
        this.private_chat_money = private_chat_money;
    }

    public static final String SAVE_CONFIG_INFO_KEY = "SAVE_CONFIG_INFO_KEY";

    /*
     * 系统h5页面
     * */
    private Html5PageUrlData app_h5;

    public Html5PageUrlData getApp_h5() {
        return app_h5;
    }

    /*


     * 存储config信息到数据库中
     * */
    public static void saveInitData(ConfigModel configObj) {

        RequestConfig.getConfigObj().setRequestIntervalIsOnLine(StringUtils.toInt(configObj.getHeartBeat()));
        RequestConfig.getConfigObj().setAisleName(configObj.getChannel());
        RequestConfig.getConfigObj().setGroupId(configObj.getGroup_id());
        RequestConfig.getConfigObj().setApp_qgorq_key(configObj.getApp_qgorq_key());
        RequestConfig.getConfigObj().setAppCertificate(configObj.getApp_certificate());
        RequestConfig.getConfigObj().setSdkappid(configObj.getSdkappid());
        RequestConfig.getConfigObj().setAccount_type(configObj.getAccount_type());
        RequestConfig.getConfigObj().setTabLiveHeartTime(configObj.getTab_live_heart_time());
        RequestConfig.getConfigObj().setPrivatePhotosCoin(configObj.getPrivate_photos());
        RequestConfig.getConfigObj().setMainSystemMessage(configObj.getSystem_message());
        RequestConfig.getConfigObj().setSplashUrl(configObj.getSplash_url());
        RequestConfig.getConfigObj().setSplashImage(configObj.getSplash_img_url());
        RequestConfig.getConfigObj().setCurrency(configObj.getCurrency_name());


        JsonData jsonData = new JsonData();
        jsonData.setKey(SAVE_CONFIG_INFO_KEY);
        jsonData.setVal(JSON.toJSONString(configObj));
        JsonDataManage.getInstance().saveData(jsonData);
    }


    /*
     * 从数据库中去除config信息
     * */
    public static ConfigModel getInitData() {

        JsonData data = JsonDataManage.getInstance().getData(SAVE_CONFIG_INFO_KEY);
        if (data != null) {

            return JSON.parseObject(data.getVal(), ConfigModel.class);
        }
        return new ConfigModel();
    }


    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getAndroid_download_url() {
        return android_download_url;
    }

    public void setAndroid_download_url(String android_download_url) {
        this.android_download_url = android_download_url;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public int getIs_force_upgrade() {
        return is_force_upgrade;
    }

    public void setIs_force_upgrade(int is_force_upgrade) {
        this.is_force_upgrade = is_force_upgrade;
    }

    public String getAndroid_app_update_des() {
        return android_app_update_des;
    }

    public void setAndroid_app_update_des(String android_app_update_des) {
        this.android_app_update_des = android_app_update_des;
    }

    public String getDirty_word() {
        return dirty_word;
    }


    public String getCustom_service_qq() {
        return custom_service_qq;
    }

    public void setCustom_service_qq(String custom_service_qq) {
        this.custom_service_qq = custom_service_qq;
    }

    public void setDirty_word(String dirty_word) {
        this.dirty_word = dirty_word;
    }

    public int getOpen_custom_video_charge_coin() {
        return open_custom_video_charge_coin;
    }

    public void setOpen_custom_video_charge_coin(int open_custom_video_charge_coin) {
        this.open_custom_video_charge_coin = open_custom_video_charge_coin;
    }

    public String getSplash_content() {
        return splash_content;
    }

    public void setSplash_content(String splash_content) {
        this.splash_content = splash_content;
    }

    public String getSplash_url() {
        return splash_url;
    }

    public void setSplash_url(String splash_url) {
        this.splash_url = splash_url;
    }

    public String getSplash_img_url() {
        return splash_img_url;
    }

    public void setSplash_img_url(String splash_img_url) {
        this.splash_img_url = splash_img_url;
    }

    public String getSystem_message() {
        return system_message;
    }

    public void setSystem_message(String system_message) {
        this.system_message = system_message;
    }


    public String getPrivate_photos() {
        return private_photos;
    }

    public void setPrivate_photos(String private_photos) {
        this.private_photos = private_photos;
    }

    public String getTab_live_heart_time() {
        return tab_live_heart_time;
    }

    public void setTab_live_heart_time(String tab_live_heart_time) {
        this.tab_live_heart_time = tab_live_heart_time;
    }

    public String getVideo_deduction() {
        return video_deduction;
    }

    public void setVideo_deduction(String video_deduction) {
        this.video_deduction = video_deduction;
    }

    public void setApp_h5(Html5PageUrlData app_h5) {
        this.app_h5 = app_h5;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
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

    public ConfigModel() {
        super();
    }

    @Override
    public String toString() {
        return "ConfigObj{" +
                "val='" + heartbeat + '\'' +
                ", channel='" + channel + '\'' +
                ", group_id='" + group_id + '\'' +
                ", app_qgorq_key='" + app_qgorq_key + '\'' +
                ", app_certificate='" + app_certificate + '\'' +
                ", sdkappid='" + sdkappid + '\'' +
                ", account_type='" + account_type + '\'' +
                '}';
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

    public String getHeartBeat() {
        return heartbeat;
    }

    public void setHeartBeat(String val) {
        this.heartbeat = val;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public ConfigModel(String val, String channel, String group_id) {

        this.heartbeat = val;
        this.channel = channel;
        this.group_id = group_id;
    }


}
