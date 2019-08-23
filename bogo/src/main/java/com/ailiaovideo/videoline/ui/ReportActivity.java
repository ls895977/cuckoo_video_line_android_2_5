package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.FullyGridLayoutManager;
import com.ailiaovideo.videoline.adapter.GridImageAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.MenuDialogClick;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequstGetReportList;
import com.ailiaovideo.videoline.modle.ReportModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 举报页面
 * Created by fly on 2018/1/3 0003.
 */

public class ReportActivity extends BaseActivity {
    private MaterialEditText editText;//举报信息输入框
    private TextView selectText;//选择文本
    private Button rightBtn;//提交按钮

    private RecyclerView mRvSelectImage;
    private List<LocalMedia> selectList = new ArrayList<>();

    private GridImageAdapter adapter;

    public static final String REPORT_USER_ID = "REPORT_USER_ID";

    private List<ReportModel> mReportList = new ArrayList<>();
    private String toUserId;
    private String[] items;
    private int selectType = -1;

    @Override
    protected Context getNowContext() {
        return ReportActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_player_inform;
    }

    @Override
    protected void initView() {

        editText = findViewById(R.id.edit_text);
        selectText = findViewById(R.id.select_text);
        mRvSelectImage = findViewById(R.id.rv_select_pic);

    }

    @Override
    protected boolean hasTopBar() {
        return true;
    }

    @Override
    protected void initSet() {
        setOnclickListener(R.id.inform_cause);
        getTopBar().setTitle("举报");
        rightBtn = getTopBar().addRightTextButton("提交", R.id.right_btn);
        rightBtn.setTextColor(getResources().getColor(R.color.black));
        rightBtn.setOnClickListener(this);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReportActivity.this, 3, GridLayoutManager.VERTICAL, false);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(3);
        mRvSelectImage.setAdapter(adapter);
        mRvSelectImage.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        toUserId = getIntent().getStringExtra(REPORT_USER_ID);
        requestGetReportList();
    }

    @Override
    protected void initPlayerDisplayData() {
    }

    //////////////////////////////////////////监听事件操作////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inform_cause:
                doSelectCause();
                break;
            case R.id.left_btn:
                finish();
                break;
            case R.id.right_btn:
                doSubmit();
                break;
            default:
                break;
        }
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(ReportActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(3)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }

    };


    //获取举报类型
    private void requestGetReportList() {

        Api.doGetReportList(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequstGetReportList jsonObj = (JsonRequstGetReportList) JsonRequstGetReportList.getJsonObj(s, JsonRequstGetReportList.class);
                if (jsonObj.getCode() == 1) {
                    mReportList.addAll(jsonObj.getList());
                }
            }
        });
    }


    /**
     * 执行提交操作
     */
    private void doSubmit() {

        String content = editText.getText().toString();

        List<File> fileList = new ArrayList<>();
        for (LocalMedia localMedia : selectList) {
            fileList.add(new File(localMedia.getPath()));

        }

        if (fileList.size() == 0) {
            showToastMsg(getString(R.string.please_upload_report_img));
            return;
        }

        if (selectType == -1) {
            showToastMsg(getString(R.string.please_chose_report_type));
            return;
        }

        if (TextUtils.isEmpty(content)) {
            showToastMsg(getString(R.string.des_content_not_empty));
            return;
        }
        showLoadingDialog(getString(R.string.loading_now_submit_data));

        int reportId = mReportList.get(selectType).getId();
        Api.doReportUser(uId, uToken, toUserId, reportId, content, fileList, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.report_success));

                } else {
                    showToastMsg(jsonObj.getMsg());
                }

                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                hideLoadingDialog();
                showToastMsg(getString(R.string.report_fail));
                finish();
            }
        });
    }

    /**
     * 执行选择举报原因操作
     */
    private void doSelectCause() {

        List<String> reportName = new ArrayList<>();
        for (ReportModel reportModel : mReportList) {

            reportName.add(reportModel.getTitle());
        }

        items = new String[reportName.size()];
        items = reportName.toArray(items);
        showMenuDialog(items, new MenuDialogClick() {
            @Override
            public void OnMenuItemClick(DialogInterface dialog, int which) {
                selectText.setText(items[which]);
                hideView(R.id.inform_right_img);
                selectType = which;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);

                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

}
