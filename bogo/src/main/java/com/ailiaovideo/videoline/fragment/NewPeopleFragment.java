package com.ailiaovideo.videoline.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecyclerNewPeopleAdapter;
import com.ailiaovideo.videoline.adapter.recycler.RecyclerRecommendAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.base.BaseListFragment;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequest;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestsPeople;
import com.ailiaovideo.videoline.json.jsonmodle.NewPeople;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 新人
 * Created by wp on 2017/12/28 0028.
 */
public class NewPeopleFragment extends BaseListFragment<TargetUserData> {

    @Override
    protected void initDate(View view) {

    }

    @Override
    public void fetchData() {

        //加载数据源
        requestGetData(false);
    }

    @Override
    protected void initSet(View view) {

    }

    @Override
    protected void initDisplayData(View view) {

    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManage() {
        return new GridLayoutManager(getContext(),2);
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new RecyclerRecommendAdapter(getContext(),dataList);
    }

    @Override
    protected void requestGetData(boolean isCache) {
        Api.getNewPeoplePageList(
                uId,
                uToken,
                page,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JsonRequestsPeople requestObj = (JsonRequestsPeople) JsonRequestBase.getJsonObj(s,JsonRequestsPeople.class);
                        if (requestObj.getCode() == 1){
                            onLoadDataResult(requestObj.getData());
                        }else{
                            onLoadDataError();
                            showToastMsg(getContext(),requestObj.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        onLoadDataError();
                    }
                }
        );
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Common.jumpUserPage(getContext(),dataList.get(position).getId());
    }

}
