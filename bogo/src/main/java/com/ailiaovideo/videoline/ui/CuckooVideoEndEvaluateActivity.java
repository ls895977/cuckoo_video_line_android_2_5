package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetEvaluate;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.EvaluateModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CuckooVideoEndEvaluateActivity extends BaseActivity {

    @BindView(R.id.id_flowlayout)
    TagFlowLayout id_flow_layout;

    private List<EvaluateModel> list = new ArrayList<>();
    private TagAdapter tagAdapter;

    public static final String TO_USER_ID = "TO_USER_ID";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    private String toUserId;
    private String channelID;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_video_end_evaluate;
    }

    @Override
    protected void initView() {

        tagAdapter = new TagAdapter<EvaluateModel>(list) {
            @Override
            public View getView(FlowLayout parent, int position, EvaluateModel item) {
                TextView tv = (TextView) LayoutInflater.from(getNowContext()).inflate(R.layout.view_evaluate_label,
                        id_flow_layout, false);
                tv.setText(item.getLabel_name());
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {

                view.setBackgroundResource(R.drawable.bg_evaluate_select);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void unSelected(int position, View view) {
                view.setBackgroundResource(R.drawable.bg_evaluate_un_select);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.white));
            }
        };
        id_flow_layout.setAdapter(tagAdapter);


    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {

        toUserId = getIntent().getStringExtra(TO_USER_ID);
        channelID = getIntent().getStringExtra(CHANNEL_ID);
        requestGetData();
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.tv_submit})
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_submit:

                clickSubmit();
                break;
            default:
                break;
        }
    }

    private void clickSubmit() {

        Integer pos[] = new Integer[id_flow_layout.getSelectedList().size()];
        id_flow_layout.getSelectedList().toArray(pos);

        if(pos.length == 0){
            finish();
            return;
        }

        if(pos.length > LiveConstant.DATA_DEFINE.MAX_SELECT_LABEL_NUM){
            ToastUtils.showLong(getString(R.string.max_select) + LiveConstant.DATA_DEFINE.MAX_SELECT_LABEL_NUM + getString(R.string.a_label));
            return;
        }

        StringBuilder labelListStr = new StringBuilder();

        for (int i = 0 ;i < pos.length; i++){
            labelListStr.append(list.get(pos[i]).getLabel_name() + "-");
        }

        Api.doRequestEvaluateEmcee(SaveData.getInstance().getId(),SaveData.getInstance().getToken(),toUserId,channelID,labelListStr.toString(),new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase res = JsonRequestBase.getJsonObj(s,JsonRequestBase.class);
                if(res != null){
                    ToastUtils.showLong(R.string.reply_success);
                }else{
                    ToastUtils.showLong(R.string.action_error);
                }
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                finish();
            }
        });
    }

    private void requestGetData() {

        Api.doRequestGetEvaluate(new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JsonRequestDoGetEvaluate data = (JsonRequestDoGetEvaluate) JsonRequestBase.getJsonObj(s,JsonRequestDoGetEvaluate.class);
                if(StringUtils.toInt(data.getCode()) == 1){

                    list.clear();
                    list.addAll(data.getList());

                    tagAdapter.notifyDataChanged();
                }else{
                  showToastMsg(data.getMsg());
                }
            }
        });
    }

}
