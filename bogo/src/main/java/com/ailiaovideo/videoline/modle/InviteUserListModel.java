package com.ailiaovideo.videoline.modle;

/**
 * Created by 魏鹏 on 2018/3/14.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class InviteUserListModel  {

    /**
     * avatar : http://p4ulgsz1p.bkt.clouddn.com/63a79201803010044571685.jpg
     * id : 100115
     * user_nickname : 莫非
     * income_total : 0
     */

    private String avatar;
    private String id;
    private String user_nickname;
    private String income_total;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getIncome_total() {
        return income_total;
    }

    public void setIncome_total(String income_total) {
        this.income_total = income_total;
    }
}
