package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.InviteUserListModel;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/13.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestGetInvitePage extends JsonRequestBase {

    private String invite_user_count;
    private String income_total;
    private String day_income_total;
    private String invite_code;

    public String getInvite_user_count() {
        return invite_user_count;
    }

    public void setInvite_user_count(String invite_user_count) {
        this.invite_user_count = invite_user_count;
    }

    public String getIncome_total() {
        return income_total;
    }

    public void setIncome_total(String income_total) {
        this.income_total = income_total;
    }

    public String getDay_income_total() {
        return day_income_total;
    }

    public void setDay_income_total(String day_income_total) {
        this.day_income_total = day_income_total;
    }

    private List<InviteUserListModel> invite_user_list;

    public List<InviteUserListModel> getInvite_user_list() {
        return invite_user_list;
    }

    public void setInvite_user_list(List<InviteUserListModel> invite_user_list) {
        this.invite_user_list = invite_user_list;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }
}
