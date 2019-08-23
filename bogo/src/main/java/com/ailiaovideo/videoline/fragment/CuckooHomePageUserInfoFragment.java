package com.ailiaovideo.videoline.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.CuckooEvaluateAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetUserPageInfo;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.AuthInfoBean;
import com.ailiaovideo.videoline.modle.CuckooUserEvaluateListModel;
import com.ailiaovideo.videoline.modle.EvaluateModel;
import com.ailiaovideo.videoline.ui.PrivatePhotoActivity;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.lzy.okgo.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 用户主页资料
 */
public class CuckooHomePageUserInfoFragment extends BaseFragment {

    @BindView(R.id.rv_content_list)
    RecyclerView rv_content_list;

    private TagFlowLayout id_flow_layout;

    private TextView tv_height;
    private TextView tv_weight;
    private TextView tv_constellation;
    private TextView tv_city;
    private TextView tv_sign;

    private List<CuckooUserEvaluateListModel> listCuckooEvaluate = new ArrayList<>();

    private List<String> selfEvaluateList = new ArrayList<>();
    private TagAdapter tagAdapter;

    public static final String TO_USER_ID = "TO_USER_ID";

    private AuthInfoBean authInfoBean;
    private String toUserId;
    private int page = 1;
    private CuckooEvaluateAdapter cuckooEvaluateAdapter;
    private RelativeLayout myPrivateImgRl;

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_cuckoo_home_page_user_info, null);
    }

    public static CuckooHomePageUserInfoFragment getInstance(String toUserId) {

        CuckooHomePageUserInfoFragment cuckooHomePageUserInfoFragment = new CuckooHomePageUserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TO_USER_ID, toUserId);
        cuckooHomePageUserInfoFragment.setArguments(bundle);
        return cuckooHomePageUserInfoFragment;
    }


    @Override
    protected void initView(View view) {

        cuckooEvaluateAdapter = new CuckooEvaluateAdapter(getContext(), listCuckooEvaluate);
        rv_content_list.setLayoutManager(new LinearLayoutManager(getContext()));

        View headView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_page_user_info_top, null);
        tv_height = headView.findViewById(R.id.tv_height);
        tv_weight = headView.findViewById(R.id.tv_weight);
        tv_constellation = headView.findViewById(R.id.tv_constellation);
        tv_city = headView.findViewById(R.id.tv_city);
        tv_sign = headView.findViewById(R.id.tv_sign);
        myPrivateImgRl = headView.findViewById(R.id.rl_private_img);
        id_flow_layout = headView.findViewById(R.id.id_flow_layout);
        cuckooEvaluateAdapter.addHeaderView(headView);
        rv_content_list.setAdapter(cuckooEvaluateAdapter);
        cuckooEvaluateAdapter.setOnLoadMoreListener(this, rv_content_list);
        cuckooEvaluateAdapter.disableLoadMoreIfNotFullPage(rv_content_list);
        myPrivateImgRl.setOnClickListener(this);

        tagAdapter = new TagAdapter<String>(selfEvaluateList) {
            @Override
            public View getView(FlowLayout parent, int position, String item) {
                TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_evaluate_label,
                        id_flow_layout, false);
                tv.setText(item);
                tv.setBackgroundResource(R.drawable.bg_evaluate_select);
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setTextSize(ConvertUtils.dp2px(7));
                return tv;
            }
        };
        id_flow_layout.setAdapter(tagAdapter);
    }

    @Override
    protected void initDate(View view) {

        toUserId = getArguments().getString(TO_USER_ID);
        requestGetInfo();
    }

    @Override
    protected void initSet(View view) {

    }

    @Override
    protected void initDisplayData(View view) {

    }

    private void requestGetInfo() {

        Api.doRequestGetUserPageInfo(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), page, toUserId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JsonRequestDoGetUserPageInfo data = (JsonRequestDoGetUserPageInfo) JsonRequestBase.getJsonObj(s, JsonRequestDoGetUserPageInfo.class);
                if (StringUtils.toInt(data.getCode()) == 1) {

                    if (data.getAuth_info() != null && page == 1 && StringUtils.toInt(data.getAuth_info().getStatus()) == 1) {
                        tv_city.setText(data.getAuth_info().getCity());
                        tv_height.setText(data.getAuth_info().getHeight());
                        tv_weight.setText(data.getAuth_info().getWeight());
                        tv_constellation.setText(data.getAuth_info().getConstellation());
                        tv_sign.setText(data.getAuth_info().getSign());
                        selfEvaluateList.clear();
                        selfEvaluateList.addAll(data.getAuth_info().getEvaluate_list());
                        tagAdapter.notifyDataChanged();
                    }

                    if (page == 1) {
                        listCuckooEvaluate.clear();
                    }

                    listCuckooEvaluate.addAll(data.getEvaluate_list());

                    if (data.getEvaluate_list().size() == 0) {
                        cuckooEvaluateAdapter.loadMoreEnd();
                    } else {
                        cuckooEvaluateAdapter.loadMoreComplete();
                    }

                    cuckooEvaluateAdapter.notifyDataSetChanged();

                } else {
                    showToastMsg(getContext(), data.getMsg());
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        requestGetInfo();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_private_img:
                PrivatePhotoActivity.startPrivatePhotoActivity(getContext(), uId, "", 0);
                break;
        }
    }
}
