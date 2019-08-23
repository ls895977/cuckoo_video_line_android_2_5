package com.ailiaovideo.videoline.json.jsonmodle;

/**
 * 礼物信息
 * Created by jaihengfei on 2018/1/22 0022.
 */

public class GiftData {
    private String id;//礼物记录id
    private String uid;//送礼物的人id
    private String touid;//收礼物人的id
    private String avatar;//送礼物的人的头像
    private String user_nickname;//送礼物人的名称
    private String giftname;//礼物名称
    private String giftcount;//礼物数量
    private String giftcoin;//单个礼物的金币
    private String addtime;//送礼物的时间
    private String img;//礼物图片

    public GiftData(String id, String uid, String touid, String avatar, String user_nickname, String giftname, String giftcount, String giftcoin, String addtime, String img) {
        this.id = id;
        this.uid = uid;
        this.touid = touid;
        this.avatar = avatar;
        this.user_nickname = user_nickname;
        this.giftname = giftname;
        this.giftcount = giftcount;
        this.giftcoin = giftcoin;
        this.addtime = addtime;
        this.img = img;
    }

    public GiftData() {
        super();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public void setGiftcount(String giftcount) {
        this.giftcount = giftcount;
    }

    public void setGiftcoin(String giftcoin) {
        this.giftcoin = giftcoin;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getTouid() {
        return touid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public String getGiftname() {
        return giftname;
    }

    public String getGiftcount() {
        return giftcount;
    }

    public String getGiftcoin() {
        return giftcoin;
    }

    public String getAddtime() {
        return addtime;
    }

    public String getImg() {
        return img;
    }
}
