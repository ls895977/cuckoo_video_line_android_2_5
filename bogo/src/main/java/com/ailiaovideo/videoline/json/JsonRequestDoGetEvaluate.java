package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.EvaluateModel;

import java.util.List;

public class JsonRequestDoGetEvaluate extends JsonRequestBase{
    private List<EvaluateModel> list;

    public List<EvaluateModel> getList() {
        return list;
    }

    public void setList(List<EvaluateModel> list) {
        this.list = list;
    }
}
