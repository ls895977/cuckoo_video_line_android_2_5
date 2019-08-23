package com.ailiaovideo.videoline.json.jsonmodle;

import com.ailiaovideo.videoline.modle.AuthInfoBean;
import com.ailiaovideo.videoline.utils.Utils;

import java.util.List;

/**
 * 目标用户详细信息
 * Created by jiahengfei on 2018/1/22 0022.
 */

public class TargetUserData {
    private String uid;//用户id
    private String token;//账户token

    private String id;//目标用户id
    private String user_nickname;//目标用户的昵称
    private String user_status;//用户目标状态:0禁用-1正常-2未验证
    private String sex;//性别
    private String avatar;//目标用户头像地址
    private String address;//目标用户所在地地址
    private String level;//目标用户当前等级(字符串
    private String max_level;//目标用户最大等级(字符串)
    private String attention;//是否关注目标用户
    private String attention_fans;//获取主播关注的人数
    private String attention_all;//获取粉丝人数
    private String call;//通话总时长(xx时xx分xx秒)
    private String evaluation;//好评百分比(30%)
    private String split;//分成比例
    private String coin;//总金币
    private String is_online;
    private String charging_coin;

    private int gift_count;
    private int video_count;
    private int pictures_count;
    private int give_like;
    private int is_black;
    private int is_auth;
    private AuthInfoBean auth_info;
    private String video_deduction;
    private List<GiftBean> gift;
    private List<VideoModel> video;
    private List<PicturesBean> pictures;
    private List<ImgBean> img;
    private String sign;
    private String voice_deduction;
    private String last_online_time;

    public String getLast_online_time() {
        return last_online_time;
    }

    public void setLast_online_time(String last_online_time) {
        this.last_online_time = last_online_time;
    }

    public String getVoice_deduction() {
        return voice_deduction;
    }

    public void setVoice_deduction(String voice_deduction) {
        this.voice_deduction = voice_deduction;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCharging_coin() {
        return charging_coin;
    }

    public void setCharging_coin(String charging_coin) {
        this.charging_coin = charging_coin;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public AuthInfoBean getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(AuthInfoBean auth_info) {
        this.auth_info = auth_info;
    }

    public String getVideo_deduction() {
        return video_deduction;
    }

    public void setVideo_deduction(String video_deduction) {
        this.video_deduction = video_deduction;
    }

    public int getIs_black() {
        return is_black;
    }

    public void setIs_black(int is_black) {
        this.is_black = is_black;
    }


    public List<ImgBean> getImg() {
        return img;
    }

    public void setImg(List<ImgBean> img) {
        this.img = img;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getCoin() {

        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public TargetUserData() {
        super();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMax_level(String max_level) {
        this.max_level = max_level;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public void setAttention_fans(String attention_fans) {
        this.attention_fans = attention_fans;
    }

    public void setAttention_all(String attention_all) {
        this.attention_all = attention_all;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getUid() {

        return uid;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    /**
     * 获取年龄
     * @return 字符串
     */
    public int getSex() {
       if (sex == null){
           return 0;
       }else{
           return Integer.valueOf(sex);
       }
    }

    public String getAvatar() {
        return Utils.getCompleteImgUrl(avatar);
    }

    public String getAddress() {
        return address;
    }

    public String getLevel() {
        return level;
    }

    public String getMax_level() {
        return max_level;
    }

    public String getAttention() {
        return attention;
    }

    public String getAttention_fans() {
        return attention_fans;
    }

    public String getAttention_all() {
        return attention_all;
    }

    public String getCall() {
        return call;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public int getGift_count() {
        return gift_count;
    }

    public void setGift_count(int gift_count) {
        this.gift_count = gift_count;
    }

    public int getVideo_count() {
        return video_count;
    }

    public void setVideo_count(int video_count) {
        this.video_count = video_count;
    }

    public int getPictures_count() {
        return pictures_count;
    }

    public void setPictures_count(int pictures_count) {
        this.pictures_count = pictures_count;
    }

    public int getGive_like() {
        return give_like;
    }

    public void setGive_like(int give_like) {
        this.give_like = give_like;
    }

    public List<GiftBean> getGift() {
        return gift;
    }

    public void setGift(List<GiftBean> gift) {
        this.gift = gift;
    }

    public List<VideoModel> getVideo() {
        return video;
    }

    public void setVideo(List<VideoModel> video) {
        this.video = video;
    }

    public List<PicturesBean> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesBean> pictures) {
        this.pictures = pictures;
    }


    public static class GiftBean {
        /**
         * avatar : http://p2ftrp6p5.bkt.clouddn.com/04ab920180208202056403.jpg
         * img : /upload/admin/20180122/264f6057d28560fd277f8d5818a603b3.jpg
         */

        private String name;
        private String img;
        private String count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }


    public static class PicturesBean {
        /**
         * img : http://p2ftrp6p5.bkt.clouddn.com/000d2201802020956275289.png
         * id : 1
         * watch : 0
         */

        private String img;
        private int id;
        private String watch;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWatch() {
            return watch;
        }

        public void setWatch(String watch) {
            this.watch = watch;
        }
    }

    public static class ImgBean {
        /**
         * id : 14
         * img : /api/public/upload/image/20180126\4184b59292df96b29c0316541d03e447.jpg
         */

        private int id;
        private String img;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }



    @Override
    public String toString() {
        return "TargetUserData{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_status='" + user_status + '\'' +
                ", sex='" + sex + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                ", level='" + level + '\'' +
                ", max_level='" + max_level + '\'' +
                ", attention='" + attention + '\'' +
                ", attention_fans='" + attention_fans + '\'' +
                ", attention_all='" + attention_all + '\'' +
                ", call='" + call + '\'' +
                ", evaluation='" + evaluation + '\'' +
                '}';
    }
}