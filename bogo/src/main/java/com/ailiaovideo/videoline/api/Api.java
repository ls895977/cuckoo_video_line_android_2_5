package com.ailiaovideo.videoline.api;

import android.util.Log;

import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.RecommendBean;
import com.ailiaovideo.videoline.manage.AppConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * api
 */

public class Api {

    public static void doRequestGetGiftCabinetList(String id, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/request_get_gift_cabinet")
                .params("to_user_id", id)
                .tag("doRequestGetGiftCabinetList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }


    public static void getUnReadSystemMsg(String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/system_message_api/unread_messages")
                .params("uid", uid)
                .params("token", token)
                .tag("getUnReadSystemMsg")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    /**
     * 拨打视频电话
     *
     * @param uid   uid
     * @param token token
     * @param id    对方id
     */
    public static void getPushCallVideo(String uid, String token, String id, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/Push/index")
                .params("uid", uid)
                .params("token", token)
                .params("id", id)
                .tag("getPushCallVideo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "拨打电话####" + "getPushCallVideo");
    }

    /**
     * 获取关注列表
     *
     * @param uid   uid
     * @param token token
     */
    public static void getAboutDataList(String uid, String token, int page, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/user_api/get_follow_list")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .tag("getAboutDataList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取关注列表####" + "getAboutDataList");
    }

    /**
     * 获取粉丝列表
     *
     * @param uid   uid
     * @param token token
     */
    public static void getFansDataList(String uid, String token, int page, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/get_fans_list")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .tag("getFansDataList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取粉丝列表####" + "getFansDataList");
    }

    /**
     * 批量获取用户魅力排行榜
     *
     * @param uid   uid
     * @param token token
     * @param type  日榜day 周榜week 不填是总榜
     * @param page  分页不传或0为第一页每页
     */
    public static void getCharmListData(String uid, String token, String type, int page, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/rank_api/charm_rank_list")
                .params("uid", uid)
                .params("token", token)
                .params("type", type)
                .params("page", page)
                .tag("getCharmListData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "批量获取用户魅力排行榜####" + "getCharmListData");
    }

    /**
     * 批量获取用户财气排行榜
     *
     * @param uid   uid
     * @param token token
     * @param type  日榜day 周榜week 不填是总榜
     * @param page  分页不传或0为第一页每页
     */
    public static void getMoneyListData(String uid, String token, String type, int page, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/rank_api/wealth_rank_list")
                .params("uid", uid)
                .params("token", token)
                .params("type", type)
                .params("page", page)
                .tag("getMoneyListData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "批量获取用户财气排行榜####" + "getMoneyListData");
    }

    /**
     * 批量获取新人页列表信息
     *
     * @param uid   uid
     * @param token token
     */
    public static void getNewPeoplePageList(String uid, String token, int page, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/page_data_api/get_news_user_list")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .tag("getNewPeoplePageList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "批量获取新人页列表信息####" + "getNewPeoplePageList");
    }

    /**
     * 获取轮播图
     *
     * @param uid       uid
     * @param token     token
     * @param shuffling 类型,1推荐页轮播*2消息页轮播
     */
    public static void getRollImage(String uid, String token, String shuffling, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/recommended_api/shuffling")
                .params("uid", uid)
                .params("token", token)
                .params("shuffling", shuffling)
                .tag("getUserPhotoList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取轮播图####" + shuffling + "getUserPhotoList");
    }

    /**
     * 获取小视频列表
     *
     * @param uid   用户id
     * @param token token
     * @param type  类型##
     * @param lat   附近的视频 要传的用户维度参数
     * @param lng   附近的视频 要传的用户经度参数
     * @see ApiUtils.VideoType 推荐reference最新latest关注attention附近near
     */
    public static void getVideoPageList(String uid, String token, String type, int page, String lat, String lng, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/small_video_api/index")
                .params("uid", uid)
                .params("token", token)
                .params("type", type)
                .params("lat", lat)
                .params("lng", lng)
                .params("page", page)
                .tag("getVideoPageList")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void getVideoList(String uid, String token, String type, int page, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/small_video_api/index")
                .params("uid", uid)
                .params("token", token)
                .params("type", type)
                .params("page", page)
                .tag("getVideoList")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取推荐用户列表
     *
     * @param uid   用户uid
     * @param token token
     * @param page  分页数
     */
    public static void getRecommendUserList(String uid, String token, int page, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/recommend_user")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .tag("getRecommendUserList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取推荐用户列表####" + "getRecommendUserList");
    }

    /**
     * 执行监听是否在线(进行心跳操作)
     *
     * @param uid   用户id
     * @param token token
     */
    public static void doMonitorIsOnLine(String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/app_api/interval")
                .params("uid", uid)
                .params("token", token)
                .tag("doMonitorIsOnLine")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
        Log.d("api", "执行监听是否在线(进行心跳操作)####" + "doMonitorIsOnLine");
    }

    /**
     * 获取配置信息
     *
     * @param uid   用户id
     * @param token token
     */
    public static void getConfigData(String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/app_api/config")
                .params("uid", uid)
                .params("token", token)
                .tag("getConfigData")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
        Log.d("api", "获取配置信息####" + "getConfigData");
    }

    /**
     * 获取用户信息
     *
     * @param id    目标用户id
     * @param uid   用户id
     * @param token token
     */
    public static void getUserData(String id, String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/personal_api/get_user_page_info")
                .params("id", id)
                .params("uid", uid)
                .params("token", token)
                .tag("getUserData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取用户信息####" + "getUserData");
    }

    /**
     * 获取用户信息
     *
     * @param id    目标用户id
     * @param uid   用户id
     * @param token token
     */
    public static void getUserBaseData(String id, String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/personal_api/get_user_base_info")
                .params("uid", uid)
                .params("token", token)
                .params("to_user_id", id)
                .tag("getUserBaseData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取用户信息####" + "getUserData");
    }

    /**
     * 获取自身信息
     *
     * @param uid   用户id
     * @param token token
     */
    public static void getUserDataByMe(String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/get_user_center_info")
                .params("uid", uid)
                .params("token", token)
                .tag("getUserDataByMe")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取自身信息####" + "getUserDataByMe");
    }

    /**
     * 关注用户
     *
     * @param id    目标人id
     * @param token token
     * @param uid   用户id+
     */
    public static void doLoveTheUser(String id, String uid, String token, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/personal_api/click_attention")
                .params("id", id)
                .params("uid", uid)
                .params("token", token)
                .tag("doLoveTheUser")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "关注用户####" + "doLoveTheUser");
    }

    /**
     * 编辑用户资料保存
     *
     * @param uid           id
     * @param token         token
     * @param user_nickname 用户昵称
     * @param avatar        用户头像
     * @param sex           性别
     * @param img           轮播图文件集合
     */
    public static void saveUserDataAtCompile(String uid, String token, String user_nickname, File avatar, int sex, ArrayList<File> img, String sign, StringCallback callback) {
        PostRequest postRequest = OkGo.post(AppConfig.API_DOMAIN + "/user_api/update_user_info");
        postRequest.params("uid", uid);
        postRequest.params("token", token);
        postRequest.params("sex", sex);
        postRequest.params("user_nickname", user_nickname);
        postRequest.params("sign", sign);
        if (avatar != null) {
            postRequest.params("avatar", avatar);
        }
        if (img.size() != 0) {
            for (int i = 0; i < img.size(); i++) {
                postRequest.params("img" + i, img.get(i));
                Log.d("api", "img--->" + i + "####" + img.get(i).toString());
            }
        }
        postRequest.tag("saveUserDataAtCompile");
        postRequest.cacheMode(CacheMode.NO_CACHE);
        postRequest.execute(callback);
        Log.d("api", "imgs---->" + img + "####" + img.toString());
        Log.d("api", "编辑用户资料保存####" + "saveUserDataAtCompile");
    }

    /**
     * 在编辑时获取用户数据
     *
     * @param uid   用户id
     * @param token 用户token
     */
    public static void getUserDataAtCompile(String uid, String token, StringCallback callback) {
        OkGo.post(AppConfig.API_DOMAIN + "/user_api/edit_user_info")
                .params("uid", uid)
                .params("token", token)
                .tag("getUserDataAtCompile")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
        Log.d("api", "在编辑时获取用户数据####" + "getUserDataAtCompile");
    }

    /**
     * 在编辑时删除用户图片
     *
     * @param uid   用户id
     * @param token 用户token
     */
    public static void delectUserImage(String uid, String token, String delId, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/del_image")
                .params("uid", uid)
                .params("token", token)
                .params("id", delId)
                .tag("delectUserImage")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
        Log.d("api", "在编辑时删除用户图片####" + "delectUserImage");
    }

    /**
     * 发送验证码
     *
     * @param mobile 手机号码
     */
    public static void sendCodeByRegister(String mobile, String img_code,StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/login_api/code")
                .params("mobile", mobile)
                .params("img_code", img_code)
                .tag("sendCodeByRegister")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    /**
     * 用户登录操作
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    public static void userLogin(String mobile, String code, String inviteCode, String agent, String uuid, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/login_api/do_login")
                .params("mobile", mobile)
                .params("code", code)
                .params("invite_code", inviteCode)
                .params("agent", agent)
                .params("uuid", uuid)
                .tag("userLogin")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    /**
     * 用户登录操作(自动登录)
     *
     * @param id    手机号
     * @param token 验证码
     */
    public static void userLogin(StringCallback callback, String id, String token) {
        OkGo.post(AppConfig.API_DOMAIN + "/login_api/do_login")
                .params("id", id)
                .params("token", token)
                .tag("userLogin")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }

    //拨打视频电话
    public static void doCallToUser(String userId, String userToken, String targetUserId, int type, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/video_call_1215")
                .params("uid", userId)
                .params("token", userToken)
                .params("id", targetUserId)
                .params("call_type", type)
                .tag("doCallToUser")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //回复电话
    public static void doReplyVideoCall(String userId, String userToken, String toUserId, String channel, String type, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/reply_video_call_0907")
                .params("uid", userId)
                .params("token", userToken)
                .params("to_user_id", toUserId)
                .params("type", type)
                .params("channel", channel)
                .tag("doReplyVideoCall")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //挂断电话
    public static void doHangUpVideoCall(String userId, String userToken, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/hang_up_video_call_0907")
                .params("uid", userId)
                .params("token", userToken)
                .tag("doHangUpVideoCall")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //结束通话
    public static void doEndVideoCall(String userId, String userToken, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/end_video_call")
                .params("uid", userId)
                .params("token", userToken)
                .params("to_user_id", toUserId)
                .tag("doEndVideoCall")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //计时扣费
    public static void doVideoCallTimeCharging(String userId, String userToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/video_call_time_charging")
                .params("uid", userId)
                .params("token", userToken)
                .tag("doVideoCallTimeCharging")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取娱乐场正在直播的视频列表
    public static void doGetVideoList(String uId, String userToken, int count, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/live_api/rand_video_live_list")
                .params("uid", uId)
                .params("token", userToken)
                .params("count", count)
                .tag("doGetVideoList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //直播心跳
    public static void doUpdateTabLiveHeart(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/live_api/update_live")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doUpdateTabLiveHeart")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取用户会话信息
    public static void doRequestConversationUserInfo(StringBuilder ids, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/get_conversation_user_info")
                .params("ids", ids.toString())
                .tag("doRequestConversationUserInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取自己的私照
    public static void doRequestGetPrivatePhoto(String uId, String id, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/private_photo_api/pictures_list")
                .params("uid", uId)
                .params("id", id)
                .params("page", page)
                .tag("doRequestGetPrivatePhoto")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //上传私照
    public static void doUploadPrivatePhoto(String uId, String uToken, List<File> files, StringCallback callback) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("uid", uId);
        httpParams.put("token", uToken);
        for (File file : files) {
            httpParams.put(file.getName(), file);
        }
        OkGo.post(AppConfig.API_DOMAIN + "/private_photo_api/private_photos_upload")
                .params(httpParams)
                .tag("doUploadPrivatePhoto")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetUploadSign(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/get_upload_sign")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doRequestGetUploadSign")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doUploadShortVideo(String uId, String uToken, int status, String money, String title, String videoId, String videoPath, String coverPath, String lat, String lng, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/add_video")
                .params("uid", uId)
                .params("token", uToken)
                .params("status", status)
                .params("money", money)
                .params("title", title)
                .params("video_url", videoPath)
                .params("video_id", videoId)
                .params("cover_url", coverPath)
                .params("lat", lat)
                .params("lng", lng)
                .tag("doUploadShortVideo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    /**
     * @param uId 被点赞用户id
     * @dw 点赞用户
     */
    public static void doFabulousUser(String uId, String uToken, String channelId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/video_fabulous")
                .params("uid", uId)
                .params("token", uToken)
                .params("channel_id", channelId)
                .tag("doFabulousUser")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doGetVideo(String uId, String uToken, String videoId, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/get_video")
                .params("uid", uId)
                .params("token", uToken)
                .params("video_id", videoId)
                .tag("doGetVideo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestBuyVideo(String uId, String uToken, String videoId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/buy_video")
                .params("uid", uId)
                .params("token", uToken)
                .params("video_id", videoId)
                .tag("doRequestBuyVideo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestSelectPic(String uId, String uToken, String pid, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/private_photo_api/select_photo")
                .params("uid", uId)
                .params("token", uToken)
                .params("pid", pid)
                .tag("doRequestSelectPic")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);

    }

    //购买私照
    public static void doRequestBuyPhoto(String uId, String uToken, String pid, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/private_photo_api/pay_personal")
                .params("uid", uId)
                .params("token", uToken)
                .params("pid", pid)
                .tag("doRequestBuyPhoto")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取视频结束信息
    public static void doReuqestGetVideoEndInfo(String uId, String uToken, String channelId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/get_video_call_end_info")
                .params("uid", uId)
                .params("token", uToken)
                .params("channel_id", channelId)
                .tag("doReuqestGetVideoEndInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //提交认证视频
    public static void doSubmitAuthVideo(String uId, String uToken, String videoUrl, String coverUrl, String videoId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/video_auth")
                .params("uid", uId)
                .params("token", uToken)
                .params("video_url", videoUrl)
                .params("cover_url", coverUrl)
                .params("video_id", videoId)
                .tag("doSubmitAuthVideo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取短视频列表
    public static void doGetShortVideoList(String uId, String uToken, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/get_video_list")
                .params("uid", uId)
                .params("token", uToken)
                .params("page", page)
                .tag("doGetShortVideoList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //删除视频文件
    public static void doDeleteVideoFile(String uId, String uToken, String videoId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/del_video")
                .params("uid", uId)
                .params("token", uToken)
                .params("video_id", videoId)
                .tag("doDeleteVideoFile")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //充值规则
    public static void doRequestGetChargeRule(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/recharge_api/get_recharge_page_data")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doRequestGetChargeRule")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //钱包信息
    public static void doRequestGetWealthPageInfo(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_wealth_page_info")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doRequestGetWealthPageInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //私信信息
    public static void doPrivateChat(String uId, String uToken, String toUserId, StringCallback callback) {


        OkGo.get(AppConfig.API_DOMAIN + "/personal_api/private_chat")
                .params("uid", uId)
                .params("token", uToken)
                .params("to_user_id", toUserId)
                .tag("doPrivateChat")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //关注用户
    public static void doFollowVideo(String uId, String uToken, String videoId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/follow_video")
                .params("uid", uId)
                .params("token", uToken)
                .params("video_id", videoId)
                .tag("doFollowVideo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //拉黑用户
    public static void doRequestBlackUser(String uId, String uToken, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/personal_api/black_user")
                .params("uid", uId)
                .params("token", uToken)
                .params("to_user_id", toUserId)
                .tag("doRequestBlackUser")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取礼物列表
    public static void doRequestGetGift(StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_gift_list")
                .tag("doRequestGetGift")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取礼物列表
    public static void doRequestGetMyGift(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_bag_list")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doRequestGetGift")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //发送礼物
    public static void doSendGift(String uid, String token, String count, String toUserId, String giftId, String channelId, String url, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + url)
                .params("uid", uid)
                .params("token", token)
                .params("to_user_id", toUserId)
                .params("gid", giftId)
                .params("count", count)
                .params("channel", channelId)
                .tag("doSendGift")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取邀请页面信息
    public static void doRequestGetInviteData(String uId, String token, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/invite_api/get_my_invite_page")
                .params("uid", uId)
                .params("token", token)
                .params("page", page)
                .tag("doRequestGetInviteData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //检查是否提交邀请码
    public static void doCheckIsFullInviteCode(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/invite_api/is_full_invite_code")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doCheckIsFullInviteCode")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //提交邀请码
    public static void doRequestSubmitInviteCode(String uId, String uToken, String inviteCode, int type, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/invite_api/full_invite_code")
                .params("uid", uId)
                .params("token", uToken)
                .params("invite_code", inviteCode)
                .params("type", type)
                .tag("doRequestSubmitInviteCode")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //提现
    public static void doRequestCash(String uId, String uToken, String name, String number, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/cash_income")
                .params("uid", uId)
                .params("token", uToken)
                .params("name", name)
                .params("number", number)
                .tag("doRequestCash")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //获取我的收益信息
    public static void doGetCashPageInfo(String uId, String uToken, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_user_income_page_info")
                .params("uid", uId)
                .params("token", uToken)
                .params("page", page)
                .tag("doGetCashPageInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //举报
    public static void doReportUser(String uId, String uToken, String toUserId, int type, String content, List<File> fileList, StringCallback callback) {

        PostRequest postRequest = OkGo.post(AppConfig.API_DOMAIN + "/user_api/do_report_user");
        postRequest.params("uid", uId);
        postRequest.params("token", uToken);
        postRequest.params("to_user_id", toUserId);
        postRequest.params("type", type);
        postRequest.params("content", content);

        if (fileList.size() != 0) {
            for (int i = 0; i < fileList.size(); i++) {
                postRequest.params("img" + i, fileList.get(i));
                Log.d("api", "img--->" + i + "####" + fileList.get(i).toString());
            }
        }
        postRequest.tag("doReportUser");
        postRequest.cacheMode(CacheMode.NO_CACHE);
        postRequest.execute(callback);
    }

    //获取举报类型列表
    public static void doGetReportList(StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_report_type")
                .tag("doGetReportList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //删除私照
    public static void doDelPrivatePhoto(String uId, String uToken, String pid, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/private_photo_api/del_photo")
                .params("uid", uId)
                .params("token", uToken)
                .params("pid", pid)
                .tag("doDelPrivatePhoto")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetCity(StringCallback callback) {

        OkGo.get("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json")
                .tag("doRequestGetCity")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }


    //加盟合作
    public static void doJoinIn(String uId, String uToken, String type, String content, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/join_in")
                .params("uid", uId)
                .params("token", uToken)
                .params("type", type)
                .params("contact", content)
                .tag("doJoinIn")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetUserContributionRank(String uId, String uToken, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/rank_api/user_contribution_rank")
                .params("uid", uId)
                .params("token", uToken)
                .params("to_user_id", toUserId)
                .tag("doRequestGetUserContributionRank")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doGetBlackList(String uId, String uToken, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/black_list")
                .params("uid", uId)
                .params("token", uToken)
                .params("page", page)
                .tag("doGetBlackList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestPerfectInfo(String uid, String token, String userNickname, int sex, File avatar, StringCallback callback) {

        OkGo.post(AppConfig.API_DOMAIN + "/login_api/perfect_reg_info")
                .params("uid", uid)
                .params("token", token)
                .params("user_nickname", userNickname)
                .params("sex", sex)
                .tag("doRequestPerfectInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doGetOnlineUser(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_online_user")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doGetOnlineUser")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doCheckIsNeedCharging(String uId, String toUserId,String channel_id, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/is_need_charging_qiniu")
                .params("uid", uId)
                .params("to_user_id", toUserId)
                .params("channel_id",channel_id)
                .tag("doCheckIsNeedCharging")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestCharge(String uId, String uToken, String rid, String pid, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/pay_api/pay")
                .params("uid", uId)
                .params("token", uToken)
                .params("pid", pid)
                .params("rid", rid)
                .tag("doRequestCharge")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doChangeVideoChargeCoin(String uId, String uToken, String coin, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/change_video_line_money")
                .params("uid", uId)
                .params("token", uToken)
                .params("coin", coin)
                .tag("doChangeVideoChargeCoin")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestSetDoNotDisturb(int type, String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/request_set_do_not_disturb")
                .params("uid", uId)
                .params("token", uToken)
                .params("type", type)
                .tag("doRequestSetDoNotDisturb")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRefreshCity(String uId, String city, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/refresh_city")
                .params("uid", uId)
                .params("city", city)
                .tag("doRefreshCity")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doPlatAuthLogin(String platId, String inviteCode, String agent, String uuid, JsonCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/login_api/auth_login")
                .params("plat_id", platId)
                .params("invite_code", inviteCode)
                .params("agent", agent)
                .params("uuid", uuid)
                .tag("doPlatAuthLogin")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetSystemMessageList(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/system_message_api/get_system_message")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doRequestGetSystemMessageList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doGetInviteCode(String uId, String uToken, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/invite_api/get_invite_code")
                .params("uid", uId)
                .params("token", uToken)
                .tag("doGetInviteCode")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestChatPay(String uId, String uToken, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/deal_api/request_private_chat_pay")
                .params("uid", uId)
                .params("token", uToken)
                .params("to_user_id", toUserId)
                .tag("doRequestChatPay")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doCheckVideoCoinRange(String money, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/video_coin_check")
                .params("money", money)
                .tag("doCheckVideoCoinRange")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetVideoChatPageData(StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/request_get_video_chat_page_data")
                .tag("doRequestGetVideoChatPageData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void getOnlineUserList(String uId, String uToken, int page, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/request_get_index_online")
                .tag("getOnlineUserList")
                .params("uid", uId)
                .params("token", uToken)
                .params("page", page)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //一键约爱
    public static void doRequestOneKeyCall(String uid, String token, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/one_key_video_call_1901017")
                .tag("doRequestOneKeyCall")
                .params("uid", uid)
                .params("token", token)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetStarEmceeList(String levelId, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/request_get_star_emcee_list")
                .tag("doRequestGetStarEmceeList")
                .params("level_id", levelId)
                .params("page", page)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetAttentionList(String uid, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/request_get_follow_emcee_list")
                .tag("doRequestGetAttentionList")
                .params("uid", uid)
                .params("page", page)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetEvaluate(StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/request_get_evaluate_list")
                .tag("doRequestGetEvaluate")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestEvaluateEmcee(String uid, String token, String toUserId, String channelId, String labelListStr, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/request_submit_evaluate")
                .tag("doRequestEvaluateEmcee")
                .params("uid", uid)
                .params("token", token)
                .params("to_user_id", toUserId)
                .params("label_str", labelListStr)
                .params("channel_id", channelId)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestSearch(String uid, String keyWord, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/request_search")
                .tag("doRequestSearch")
                .params("uid", uid)
                .params("key_word", keyWord)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetEvaluate(String uid, int evaluatePage, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/personal_api/get_evaluate_list")
                .tag("doRequestGetEvaluate")
                .params("to_user_id", uid)
                .params("page", evaluatePage)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestSubmitAuthInfo(HttpParams params, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/request_submit_auth_info")
                .tag("doRequestSubmitAuthInfo")
                .params(params)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doGetOtherUserShortVideoList(String uid, String token, String toUserId, int page, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/short_video_api/get_other_user_video_list")
                .tag("doGetOtherUserShortVideoList")
                .params("uid", uid)
                .params("token", token)
                .params("to_user_id", toUserId)
                .params("page", page)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void getUserHomePageInfo(String toUserId, String uid, String token, JsonCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/get_user_page_info")
                .tag("getUserHomePageInfo")
                .params("uid", uid)
                .params("token", token)
                .params("to_user_id", toUserId)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetUserPageInfo(String uid, String token, int page, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/user_api/get_user_page_user_info")
                .tag("doRequestGetUserPageInfo")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .params("to_user_id", toUserId)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetOSSInfo(String uid, String token, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/app_api/get_qiniu_upload_token")
                .tag("doRequestGetOSSInfo")
                .params("uid", uid)
                .params("token", token)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetRewardCoinRule(StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_coin_reward_rule")
                .tag("doRequestGetRewardCoinRule")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetPayPalOrderStatus(String id, String r_id, String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/pay_api/check_pay_pal_order_status")
                .params("order_id", id)
                .params("r_id", r_id)
                .params("uid", uid)
                .params("token", token)
                .tag("doRequestGetPayPalOrderStatus")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRewardCoin(String uid, String token, String r_id, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/deal_api/coin_reward")
                .params("r_id", r_id)
                .params("uid", uid)
                .params("token", token)
                .tag("doRewardCoin")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetVideoCallList(String userId, String token, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/get_video_call_list")
                .params("uid", userId)
                .params("token", token)
                .tag("doRequestGetVideoCallList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doGetVideoCallInfo(String userId, String token, String id, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/get_video_call_info")
                .params("uid", userId)
                .params("token", token)
                .params("id", id)
                .tag("doGetVideoCallInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }


    //实时获取主播收益信息
    public static void doRequestGetVideoCallTimeInfo(String uid, String channelName, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/get_video_call_time_info")
                .params("uid", uid)
                .params("channel_id", channelName)
                .tag("doRequestGetVideoCallTimeInfo")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }


    /**
     * 获取动态列表 推荐
     */

    public static void doRequestGetDynamicList(String uid, String token, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/bzone_api/get_list")
                .tag("doRequestGetDynamicList")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .params("type", 2)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    /**
     * 获取动态列表 关注
     */

    public static void doRequestGetLoveDynamicList(String uid, String token, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/bzone_api/get_attention_list")
                .tag("doRequestGetDynamicList")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .params("type", 2)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    //点赞
    public static void doRequestDynamicLike(String uid, String token, String zoneId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/bzone_api/request_like")
                .tag("doRequestDynamicLike")
                .params("uid", uid)
                .params("token", token)
                .params("zone_id", zoneId)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetDynamicCommonList(String zoneId, String uid, String token, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/bzone_api/get_reply_list")
                .tag("doRequestGetDynamicCommonList")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .params("zone_id", zoneId)
                .params("type", 2)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestPublishCommon(String uid, String token, String msgContent, String zoneId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/bzone_api/add_dynamic_reply")
                .tag("doRequestPublishCommon")
                .params("uid", uid)
                .params("token", token)
                .params("body", msgContent)
                .params("zone_id", zoneId)
                .params("type", 2)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestPushDynamic(String uid, String token, String content, int isAudio, List<LocalMedia> selectList, String voiceFilePath, StringCallback callback) {

        HttpParams params = new HttpParams();
        params.put("uid", uid);
        params.put("token", token);
        params.put("msg_content", content);
        params.put("is_audio", isAudio);
        if (isAudio == 1) {
            params.put("file1_audio", new File(voiceFilePath));
        }

        for (int i = 0; i < selectList.size(); i++) {
            params.put("file" + (i + 1), new File(selectList.get(i).getPath()));
        }

        OkGo.post(AppConfig.API_DOMAIN + "/bzone_api/add_dynamic")
                .tag("doRequestPushDynamic")
                .params(params)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestDelDynamic(String uid, String token, String zoneId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/bzone_api/del_dynamic")
                .tag("doRequestDynamicLike")
                .params("uid", uid)
                .params("token", token)
                .params("zone_id", zoneId)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }


    /**
     * 获取推荐列表
     *
     * @param uid   用户id
     * @param token token
     */
    public static void getRecommedListData(String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/recommended_api/recommend_list")
                .params("uid", uid)
                .params("token", token)
                .tag("getUserData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
        Log.d("api", "获取推荐列表####" + "getRecommedListData");
    }

    /**
     * 一键关注推荐用户
     */

    public static void getCommendRecommedListData(String dataList, String uid, String token, StringCallback callback) {


        OkGo.post(AppConfig.API_DOMAIN + "/recommended_api/follows")
                .params("uid", uid)
                .params("liveuid", dataList)
                .params("token", token)
                .tag("getCommendRecommedListData")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetSubscribeList(String uid, String token, int action, int page, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/page_data_api/get_my_subscribe_user_list")
                .params("uid", uid)
                .params("token", token)
                .params("page", page)
                .params("action", action)
                .tag("doRequestGetSubscribeList")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestBackVideoCall(String uid, String token, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/back_video_call")
                .params("uid", uid)
                .params("token", token)
                .params("id", toUserId)
                .tag("doRequestBackVideoCall")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    /**
     * 获取用户性质
     *
     * @param to_user_id
     * @param callback
     */
    public static void doGetAuthStatus(String to_user_id, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/get_user_auth_status")
                .params("to_user_id", to_user_id)
                .tag("doGetAuthStatus")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doSubscribe(String uid, String token, String toUserId, StringCallback callback) {

        OkGo.get(AppConfig.API_DOMAIN + "/video_call_api/subscribe_user")
                .params("uid", uid)
                .params("token", token)
                .params("to_user_id", toUserId)
                .tag("doSubscribe")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void doRequestGetIsAuth(String uid, String token, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/is_auth")
                .params("uid", uid)
                .params("token", token)
                .tag("doRequestGetIsAuth")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void editUserNickname(String uid, String token, String nickName, StringCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/user_api/edit_user_nickname")
                .params("uid", uid)
                .params("token", token)
                .params("user_nickname", nickName)
                .tag("editUserNickname")
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }

    public static void userPassLogin(String user, String pass, String inviteCode, String agent, String uuid, JsonCallback callback) {
        OkGo.get(AppConfig.API_DOMAIN + "/login_api/do_pass_login")
                .params("mobile", user)
                .params("pass", pass)
                .params("invite_code", inviteCode)
                .params("agent", agent)
                .params("uuid", uuid)
                .tag("userLogin")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(callback);
    }
}
