package com.ailiaovideo.videoline.modle;

import java.util.List;

public class CuckooUserEvaluateListModel {

    /**
     * user_nickname : 男生
     * avatar : http://p4ulgsz1p.bkt.clouddn.com/1c8dd201807090953305137.jpg
     * label_name : 聊天很愉快
     * label_list : ["聊天很愉快"]
     */

    private String user_nickname;
    private String avatar;
    private String label_name;
    private List<String> label_list;

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public List<String> getLabel_list() {
        return label_list;
    }

    public void setLabel_list(List<String> label_list) {
        this.label_list = label_list;
    }
}
