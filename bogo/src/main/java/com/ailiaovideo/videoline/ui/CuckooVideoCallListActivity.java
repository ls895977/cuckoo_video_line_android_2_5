package com.ailiaovideo.videoline.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.CuckooVideoCallListAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.dialog.DoCallVideoWaitDialog;
import com.ailiaovideo.videoline.event.CuckooCallVideoEvent;
import com.ailiaovideo.videoline.event.EImVideoCallEndMessages;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestGetVideoCallListModel;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.CuckooVideoCallListModel;
import com.ailiaovideo.videoline.modle.JsonDoGetVideoCallInfoModel;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsg;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsgVideoCall;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class CuckooVideoCallListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private List<CuckooVideoCallListModel> listModelList = new ArrayList<>();
    private CuckooVideoCallListAdapter cuckooVideoCallListAdapter;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cuckoo_video_call_list;
    }

    @Override
    protected void initView() {

        rv_content_list.setLayoutManager(new LinearLayoutManager(this));
        cuckooVideoCallListAdapter = new CuckooVideoCallListAdapter(listModelList);
        rv_content_list.setAdapter(cuckooVideoCallListAdapter);
        cuckooVideoCallListAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initSet() {
        getTopBar().setVisibility(View.VISIBLE);
        getTopBar().setTitle("通话列表");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestGetList();
    }

    private void requestGetList() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Api.doRequestGetVideoCallList(SaveData.getInstance().getId(),SaveData.getInstance().getToken(),new StringCallback(){

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestGetVideoCallListModel data = (JsonRequestGetVideoCallListModel) JsonRequestBase.getJsonObj(s,JsonRequestGetVideoCallListModel.class);
                        if(StringUtils.toInt(data.getCode()) == 1){
                            listModelList.clear();
                            listModelList.addAll(data.getList());
                            cuckooVideoCallListAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showLong(data.getMsg());
                        }
                    }
                });
            }
        },500);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        showLoadingDialog("正在获取信息...");
        Api.doGetVideoCallInfo(SaveData.getInstance().getId(),SaveData.getInstance().getToken(),listModelList.get(position).getUser_id(),new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonDoGetVideoCallInfoModel data = (JsonDoGetVideoCallInfoModel) JsonRequestBase.getJsonObj(s,JsonDoGetVideoCallInfoModel.class);
                if(StringUtils.toInt(data.getCode()) == 1){

                    CustomMsgVideoCall videoCall = JSON.parseObject(data.getExt(), CustomMsgVideoCall.class);
                    DoCallVideoWaitDialog dialog = new DoCallVideoWaitDialog(CuckooVideoCallListActivity.this, videoCall.getSender(), videoCall.getChannel(), videoCall.getIs_free());
                    dialog.show();
                    dialog.setOnDialogClick(new DoCallVideoWaitDialog.OnDialogClick() {
                        @Override
                        public void onLeftClick() {
                            //requestGetList();
                        }

                        @Override
                        public void onRightClick() {
                            //finish();
                        }
                    });
                }else{
                    ToastUtils.showLong(data.getMsg());
                    requestGetList();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClickCallVideo(CuckooCallVideoEvent var1) {
        requestGetList();
    }

}
