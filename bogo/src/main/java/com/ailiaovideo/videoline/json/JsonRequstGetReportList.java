package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.ReportModel;

import java.util.List;

/**
 * Created by 魏鹏 on 2018/3/14.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class JsonRequstGetReportList extends JsonRequestBase {


    private List<ReportModel> list;

    public List<ReportModel> getList() {
        return list;
    }

    public void setList(List<ReportModel> list) {
        this.list = list;
    }
}
