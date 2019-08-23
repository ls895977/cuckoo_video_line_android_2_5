package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.chat.ui.ChatActivity;
import com.ailiaovideo.videoline.event.RefreshMessageEvent;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.ApiConstantDefine;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecyclePrivatePhotoAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.dialog.ShowPayPhotoDialog;
import com.ailiaovideo.videoline.inter.MenuDialogClick;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoUploadPrivatePhoto;
import com.ailiaovideo.videoline.json.JsonRequestGetPrivateImg;
import com.ailiaovideo.videoline.json.JsonRequestSelectPic;
import com.ailiaovideo.videoline.modle.PrivatePhotoModel;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * @dw 私照
 */
public class PrivatePhotoActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private QMUITopBar qmuiTopBar;
    private Button rightBtn;
    private RecyclerView mViewContentList;
    private int page = 0;
    private List<LocalMedia> localMediaList;
    private List<PrivatePhotoModel> privateImageModelList = new ArrayList<>();
    private RecyclePrivatePhotoAdapter privatePhotoAdapter;

    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String ACTION_TYPE = "ACTION_TYPE";

    private String userId;
    private String userName;
    private int actionType = 0;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_private_photo;
    }

    @Override
    protected void initView() {

        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        qmuiTopBar = findViewById(R.id.qmui_top_bar);
        mViewContentList = findViewById(R.id.rv_content_list);

        qmuiTopBar.addLeftImageButton(R.drawable.icon_back_black, R.id.all_backbtn).setOnClickListener(this);
        rightBtn = qmuiTopBar.addRightTextButton(getString(R.string.add), R.id.right_btn);
        qmuiTopBar.setTitle(getString(R.string.private_img));
        rightBtn.setTextColor(getResources().getColor(R.color.color_4d));
        rightBtn.setTextSize(14);
        rightBtn.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        actionType = getIntent().getIntExtra(ACTION_TYPE, 0);
        userId = getIntent().getStringExtra(USER_ID);
        userName = getIntent().getStringExtra(USER_NAME);

        requestGetData();
    }


    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    protected void initSet() {

        mViewContentList.setLayoutManager(new GridLayoutManager(this, 3));
        privatePhotoAdapter = new RecyclePrivatePhotoAdapter(this, privateImageModelList);
        mViewContentList.setAdapter(privatePhotoAdapter);
        privatePhotoAdapter.setOnLoadMoreListener(this, mViewContentList);
        privatePhotoAdapter.disableLoadMoreIfNotFullPage();

        privatePhotoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

                //actionType1代表选择私照发送
                if (actionType == 1) {
                    if (StringUtils.toInt(privateImageModelList.get(position).getStatus()) != 1) {
                        ToastUtils.showShort(R.string.please_select_adopt_private_img);
                        return;
                    }
                    DialogHelp.getConfirmDialog(PrivatePhotoActivity.this, getString(R.string.is_send), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.putExtra("img_id", privateImageModelList.get(position).getId());
                            intent.putExtra("img_url", privateImageModelList.get(position).getImg2());
                            setResult(ChatActivity.RESULT_OK, intent);
                            finish();
                        }
                    }).show();
                    return;
                }

                if (StringUtils.toInt(uId) != StringUtils.toInt(privateImageModelList.get(position).getUid())) {

                    Common.requestSelectPic(PrivatePhotoActivity.this, privateImageModelList.get(position).getId());
                } else {
                    showMenuDialog(new String[]{getString(R.string.show), getString(R.string.del)}, new MenuDialogClick() {
                        @Override
                        public void OnMenuItemClick(DialogInterface dialog, int which) {

                            if (which == 0) {
                                Common.requestSelectPic(PrivatePhotoActivity.this, privateImageModelList.get(position).getId());
                            } else {

                                requestDeletePhoto(privateImageModelList.get(position).getId());
                            }
                        }
                    });
                }
            }
        });

        //是否是自己
        if (StringUtils.toInt(userId) != StringUtils.toInt(uId)) {
            qmuiTopBar.removeAllRightViews();
        }
    }

    //删除照片
    private void requestDeletePhoto(String id) {

        showLoadingDialog(getString(R.string.loading_now_del));
        Api.doDelPrivatePhoto(uId, uToken, id, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.del_success));
                    requestGetData();
                } else {
                    showToastMsg(jsonObj.getMsg());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:

                selectPhoto();
                break;
            case R.id.all_backbtn:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 选择相册图片
     */
    private void selectPhoto() {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }


    private void requestGetData() {

        Api.doRequestGetPrivatePhoto(uId, userId, page, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetPrivateImg jsonObj =
                        (JsonRequestGetPrivateImg) JsonRequestGetPrivateImg.getJsonObj(s, JsonRequestGetPrivateImg.class);
                if (jsonObj.getCode() == 1) {

                    if (page > 0) {
                        privatePhotoAdapter.loadMoreComplete();
                    } else {
                        privateImageModelList.clear();
                    }
                    privateImageModelList.addAll(jsonObj.getList());
                    privatePhotoAdapter.notifyDataSetChanged();

                    if (jsonObj.getList().size() == 0) {

                        privatePhotoAdapter.loadMoreEnd();
                    }
                }
            }
        });
    }

    private void requestUploadPhoto() {

        tipD = new QMUITipDialog.Builder(getNowContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.loading_upload_info))
                .create();
        tipD.show();

        List<File> fileList = new ArrayList<>();
        for (LocalMedia localMedia : localMediaList) {
            fileList.add(new File(localMedia.getPath()));

        }
        Api.doUploadPrivatePhoto(uId, uToken, fileList, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                tipD.dismiss();
                JsonRequestDoUploadPrivatePhoto jsonObj =
                        (JsonRequestDoUploadPrivatePhoto) JsonRequestDoUploadPrivatePhoto.getJsonObj(s, JsonRequestDoUploadPrivatePhoto.class);
                if (jsonObj.getCode() == 1) {

                    requestGetData();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                tipD.dismiss();
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
                    localMediaList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    requestUploadPhoto();
                    break;
            }
        }
    }

    //跳转私照
    public static void startPrivatePhotoActivity(Context context, String userId, String userName, int type) {
        Intent intent = new Intent(context, PrivatePhotoActivity.class);
        intent.putExtra(PrivatePhotoActivity.USER_ID, userId);
        intent.putExtra(PrivatePhotoActivity.USER_NAME, userName);
        intent.putExtra(PrivatePhotoActivity.ACTION_TYPE, type);
        context.startActivity(intent);
    }


    @Override
    public void onLoadMoreRequested() {

        page++;
        requestGetData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
