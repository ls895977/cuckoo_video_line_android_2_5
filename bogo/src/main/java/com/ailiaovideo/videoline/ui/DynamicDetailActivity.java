package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.DynamicCommonAdapter;
import com.ailiaovideo.videoline.adapter.DynamicImgAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.audiorecord.entity.AudioEntity;
import com.ailiaovideo.videoline.audiorecord.view.CommonSoundItemView;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.event.RefreshMessageEvent;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetDynamicCommonList;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.DynamicCommonListModel;
import com.ailiaovideo.videoline.modle.DynamicListModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class DynamicDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    public static final String DYNAMIC_DATA = "DYNAMIC_DATA";

    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_content;

    private CircleImageView iv_avatar;
    private RecyclerView rv_photo_list;
    private CommonSoundItemView pp_sound_item_view;

    @BindView(R.id.rv_content_list)
    RecyclerView rv_common_list;

    @BindView(R.id.et_input)
    EditText et_input;

    private DynamicCommonAdapter dynamicCommonAdapter;
    private DynamicListModel dynamicListModel;
    private int page = 1;
    private List<DynamicCommonListModel> list = new ArrayList<>();
    private View headView;
    private QMUITopBar topBar;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dynamic_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        EventBus.getDefault().register(this);

        dynamicListModel = getIntent().getParcelableExtra(DYNAMIC_DATA);

        headView = LayoutInflater.from(this).inflate(R.layout.dynamic_datail_layout, null);
        topBar = findViewById(R.id.qmui_top_bar);
        topBar.setTitle("动态详情");

        tv_name = headView.findViewById(R.id.item_tv_name);
        tv_content = headView.findViewById(R.id.item_tv_content);
        tv_time = headView.findViewById(R.id.item_tv_time);
        iv_avatar = headView.findViewById(R.id.item_iv_avatar);
        rv_photo_list = headView.findViewById(R.id.rv_photo_list);
        pp_sound_item_view = headView.findViewById(R.id.pp_sound_item_view);

        TextView tv_common_count = headView.findViewById(R.id.item_tv_common_count);
        TextView item_tv_like_count = headView.findViewById(R.id.item_tv_like_count);
        tv_common_count.setText(dynamicListModel.getComment_count());
        item_tv_like_count.setText(dynamicListModel.getLike_count());
        changeLikeStatus();
        item_tv_like_count.setOnClickListener(this);

        tv_name.setText(dynamicListModel.getUserInfo().getUser_nickname());
        tv_content.setText(dynamicListModel.getMsg_content());

        tv_time.setText(dynamicListModel.getPublish_time());

        Utils.loadHttpIconImg(this, dynamicListModel.getUserInfo().getAvatar(), iv_avatar, 0);

        AudioEntity audioEntity = new AudioEntity();
        audioEntity.setUrl(dynamicListModel.getAudio_file());
        pp_sound_item_view.setSoundData(audioEntity);

        rv_photo_list.setLayoutManager(new GridLayoutManager(this, 3));
        rv_photo_list.setAdapter(new DynamicImgAdapter(dynamicListModel.getThumbnailPicUrls()));
        if (StringUtils.toInt(dynamicListModel.getIs_audio()) == 1) {
            pp_sound_item_view.setVisibility(View.VISIBLE);
        } else {
            pp_sound_item_view.setVisibility(View.GONE);
        }

        rv_common_list.setLayoutManager(new LinearLayoutManager(this));
        dynamicCommonAdapter = new DynamicCommonAdapter(list);
        dynamicCommonAdapter.addHeaderView(headView);
        dynamicCommonAdapter.setOnLoadMoreListener(this, rv_common_list);
        dynamicCommonAdapter.disableLoadMoreIfNotFullPage();
        dynamicCommonAdapter.notifyDataSetChanged();


        requestGetData();


    }


    private void requestGetData() {

        Api.doRequestGetDynamicCommonList(dynamicListModel.getId(), SaveData.getInstance().getId(), SaveData.getInstance().getToken(), page, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestDoGetDynamicCommonList data = (JsonRequestDoGetDynamicCommonList) JsonRequestBase.getJsonObj(s, JsonRequestDoGetDynamicCommonList.class);
                if (StringUtils.toInt(data.getCode()) == 1) {

                    if (page == 1) {
                        list.clear();
                    }

                    if (data.getList().size() <= 20) {
                        dynamicCommonAdapter.loadMoreEnd();
                        dynamicCommonAdapter.setEnableLoadMore(false);
                    } else {
                        dynamicCommonAdapter.loadMoreComplete();
                    }

                    if (dynamicCommonAdapter == null) {
                        dynamicCommonAdapter = new DynamicCommonAdapter(list);
                    } else {
                        list.addAll(data.getList());
                    }

                    rv_common_list.setAdapter(dynamicCommonAdapter);

//                    dynamicCommonAdapter.notifyDataSetChanged();
                } else {

                    ToastUtils.showLong(data.getMsg());
                }
            }
        });
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @OnClick({R.id.btn_publish_common})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_publish_common:

                clickPublishCommon();
                break;

            case R.id.item_tv_like_count:
                clickLike();
                break;
            default:
                break;
        }
    }

    private void changeLikeStatus() {
        TextView item_tv_like_count = headView.findViewById(R.id.item_tv_like_count);
        ImageView like_iv = headView.findViewById(R.id.item_iv_like_count);

        if (StringUtils.toInt(dynamicListModel.getIs_like()) == 1) {
            like_iv.setBackgroundResource(R.mipmap.ic_dynamic_thumbs_up_s);
        } else {
            like_iv.setBackgroundResource(R.mipmap.ic_dynamic_thumbs_up_n);
        }

        item_tv_like_count.setText(dynamicListModel.getLike_count());
    }

    private void clickLike() {
        Api.doRequestDynamicLike(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), dynamicListModel.getId(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase data = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (StringUtils.toInt(data.getCode()) == 1) {
                    if (StringUtils.toInt(dynamicListModel.getIs_like()) == 1) {
                        dynamicListModel.setIs_like("0");
                        dynamicListModel.decLikeCount(1);
                    } else {
                        dynamicListModel.setIs_like("1");
                        dynamicListModel.plusLikeCount(1);
                    }

                    changeLikeStatus();
                }
            }
        });
    }

    private void clickPublishCommon() {

        String msgContent = et_input.getText().toString();
        if (TextUtils.isEmpty(msgContent)) {
            ToastUtils.showLong("请输入内容！");
            return;
        }

        showLoadingDialog("正在提交...");
        Api.doRequestPublishCommon(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), msgContent, dynamicListModel.getId(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase data = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (StringUtils.toInt(data.getCode()) == 1) {
                    ToastUtils.showLong("发布成功！");
                    et_input.setText("");
                    page = 1;
                    requestGetData();
                } else {
                    ToastUtils.showLong(data.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        requestGetData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new RefreshMessageEvent("refresh_dynamic_list"));
    }
}
