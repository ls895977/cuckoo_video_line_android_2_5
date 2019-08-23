package com.ailiaovideo.videoline.json;

import com.ailiaovideo.videoline.modle.AuthInfoBean;
import com.ailiaovideo.videoline.modle.CuckooEvaluateModel;
import com.ailiaovideo.videoline.modle.CuckooUserEvaluateListModel;
import com.ailiaovideo.videoline.modle.EvaluateModel;

import java.util.List;

public class JsonRequestDoGetUserPageInfo extends JsonRequestBase{
    private AuthInfoBean auth_info;
    private List<CuckooUserEvaluateListModel> evaluate_list;

    public AuthInfoBean getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(AuthInfoBean auth_info) {
        this.auth_info = auth_info;
    }

    public List<CuckooUserEvaluateListModel> getEvaluate_list() {
        return evaluate_list;
    }

    public void setEvaluate_list(List<CuckooUserEvaluateListModel> evaluate_list) {
        this.evaluate_list = evaluate_list;
    }
}
