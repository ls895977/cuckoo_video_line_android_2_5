package com.ailiaovideo.videoline.modle;

public class DynamicCommonListModel {


    /**
     * id : 11
     * addtime : 11分钟前
     * body : 我们
     * uid : 100169
     * zone_id : 30
     * userInfo : {"id":100169,"avatar":"http://p4ulgsz1p.bkt.clouddn.com/08ed2201807281647316370.jpg","user_nickname":"船舶","sex":1,"level":1,"coin":1500,"user_status":2}
     */

    private String id;
    private String addtime;
    private String body;
    private String uid;
    private String zone_id;
    private UserModel userInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }

}
