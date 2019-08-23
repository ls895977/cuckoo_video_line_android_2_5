package com.ailiaovideo.videoline.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.DynamicAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.base.BaseListFragment;
import com.ailiaovideo.videoline.event.RefreshMessageEvent;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetDynamicList;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.DynamicListModel;
import com.ailiaovideo.videoline.ui.DynamicDetailActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.qcloud.presentation.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 动态 推荐
 */
public class DynamicRecommeFragment extends BaseListFragment<DynamicListModel> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @Override
    protected void initSet(View view) {

    }

    @Override
    protected void initDisplayData(View view) {

    }


    @Override
    protected boolean isRegEvent() {
        return true;
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new DynamicAdapter(getContext(), dataList);
    }

    @Override
    protected void requestGetData(boolean isCache) {

        Api.doRequestGetDynamicList(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), page, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestDoGetDynamicList data = (JsonRequestDoGetDynamicList) JsonRequestBase.getJsonObj(s, JsonRequestDoGetDynamicList.class);
                if (StringUtils.toInt(data.getCode()) == 1) {
                    onLoadDataResult(data.getList());
                } else {
                    mSwRefresh.setRefreshing(false);
                    ToastUtils.showLong(data.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                mSwRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), DynamicDetailActivity.class);
        intent.putExtra(DynamicDetailActivity.DYNAMIC_DATA, dataList.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        if (view.getId() == R.id.item_iv_like_count) {
            Api.doRequestDynamicLike(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), dataList.get(position).getId(), new StringCallback() {

                @Override
                public void onSuccess(String s, Call call, Response response) {

                    JsonRequestBase data = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                    if (StringUtils.toInt(data.getCode()) == 1) {
                        if (StringUtils.toInt(dataList.get(position).getIs_like()) == 1) {
                            dataList.get(position).setIs_like("0");
                            dataList.get(position).decLikeCount(1);
                        } else {
                            dataList.get(position).setIs_like("1");
                            dataList.get(position).plusLikeCount(1);
                        }
                        baseQuickAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else if (view.getId() == R.id.item_del) {

            DialogHelp.getConfirmDialog(getContext(), "确定要删除动态？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clickDelDynamic(position);
                }
            }).show();
        } else if (view.getId() == R.id.item_tv_chat) {
            Common.startPrivatePage(getContext(), dataList.get(position).getUserInfo().getId());
        } else if (view.getId() == R.id.item_iv_avatar) {
            Common.jumpUserPage(getContext(), dataList.get(position).getUserInfo().getId());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshMessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("refresh_dynamic_list")) {
            page = 1;
            requestGetData(false);
        }
    }

    private void clickDelDynamic(final int position) {

        showLoadingDialog("正在操作...");
        Api.doRequestDelDynamic(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), dataList.get(position).getId(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase data = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (StringUtils.toInt(data.getCode()) == 1) {

                    dataList.remove(position);
                    baseQuickAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }
}