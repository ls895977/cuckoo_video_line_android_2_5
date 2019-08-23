package com.ailiaovideo.videoline.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by weipeng on 2018/2/10.
 */

public abstract class BaseListFragment<T> extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.sw_refresh)
    protected SwipeRefreshLayout mSwRefresh;

    @BindView(R.id.rv_content_list)
    RecyclerView mRvContentList;

    protected int page = 1;

    protected List<T> dataList = new ArrayList<>();
    protected BaseQuickAdapter baseQuickAdapter;

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base_list, container, false);
    }

    @Override
    protected void initView(View view) {

        baseQuickAdapter = getBaseQuickAdapter();
        mRvContentList.setLayoutManager(getLayoutManage());
        mRvContentList.setAdapter(baseQuickAdapter);
        mSwRefresh.setOnRefreshListener(this);
        baseQuickAdapter.setOnItemClickListener(this);
        baseQuickAdapter.setOnItemChildClickListener(this);
        baseQuickAdapter.setOnLoadMoreListener(this,mRvContentList);
        baseQuickAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    protected void initDate(View view) {

    }

    @Override
    protected void initSet(View view) {

    }

    @Override
    protected void initDisplayData(View view) {

    }

    @Override
    public void fetchData() {
        requestGetData(false);
    }

    protected RecyclerView.LayoutManager getLayoutManage(){

        return new LinearLayoutManager(getContext());
    }

    protected abstract BaseQuickAdapter getBaseQuickAdapter();

    protected void onLoadDataResult(List<T> list){

        mSwRefresh.setRefreshing(false);
        if(page == 1){
            dataList.clear();
        }

        if(list.size() == 0){
            baseQuickAdapter.loadMoreEnd();
        }else{
            baseQuickAdapter.loadMoreComplete();
        }

        dataList.addAll(list);
        if(page == 1){
            baseQuickAdapter.setNewData(dataList);
        }else{
            baseQuickAdapter.notifyDataSetChanged();
        }

    }

    protected void onLoadDataError(){
        mSwRefresh.setRefreshing(false);
        baseQuickAdapter.notifyDataSetChanged();
        baseQuickAdapter.loadMoreEnd();
    }

    @Override
    public void onLoadMoreRequested() {

        if(Utils.isHasNextPage(dataList.size())){
            page ++;
            requestGetData(false);
        }else{
            baseQuickAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestGetData(false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
