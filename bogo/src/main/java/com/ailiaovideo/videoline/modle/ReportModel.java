package com.ailiaovideo.videoline.modle;

/**
 * Created by 魏鹏 on 2018/3/14.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class ReportModel {

    /**
     * id : 1
     * title : 语言攻击
     * orderno : 1
     * addtime : 1518406245
     */

    private int id;
    private String title;
    private int orderno;
    private int addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}
