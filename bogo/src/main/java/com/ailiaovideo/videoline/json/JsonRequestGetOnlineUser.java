package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.OnlineUserModel;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/26.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequestGetOnlineUser extends JsonRequestBase {

    private List<OnlineUserModel> list;
    private String online_count;

    public String getOnline_count() {
        return online_count;
    }

    public void setOnline_count(String online_count) {
        this.online_count = online_count;
    }

    public List<OnlineUserModel> getList() {
        return list;
    }

    public void setList(List<OnlineUserModel> list) {
        this.list = list;
    }
}
