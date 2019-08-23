package com.ailiaovideo.videoline.json.jsonmodle;

/**
 * 用户基本信息
 * Created by jiahengfei on 2018/1/24 0024.
 */

public class UserBaseData {
    private String id;
    private String token;
    private String UserSig;

    public UserBaseData() {
        super();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserSig(String userSig) {
        UserSig = userSig;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUserSig() {
        return UserSig;
    }

    public UserBaseData(String id, String token, String userSig) {
        this.id = id;
        this.token = token;
        UserSig = userSig;
    }
}
