package com.ailiaovideo.videoline.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.recycler.RecycleViewShortVideoAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.event.CuckooPushVideoCommonEvent;
import com.ailiaovideo.videoline.event.EImOnPrivateMessage;
import com.ailiaovideo.videoline.helper.ImageUtil;
import com.ailiaovideo.videoline.inter.MenuDialogClick;
import com.ailiaovideo.videoline.inter.MsgDialogClick;
import com.ailiaovideo.videoline.json.JsonGetIsAuth;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestGetShortVideoList;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.utils.BGVideoFile;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

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
 * @dw 短视频
 */
public class ShortVideoActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private QMUITopBar qmuiTopBar;
    private RecyclerView mRvContentList;
    private RecycleViewShortVideoAdapter mShortVideoAdapter;

    private Button rightBtn;
    private List<VideoModel> mVideoList = new ArrayList<>();
    private int page = 1;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_short_video;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        qmuiTopBar = findViewById(R.id.qmui_top_bar);
        mRvContentList = findViewById(R.id.rv_content_list);
        mRvContentList.setLayoutManager(new GridLayoutManager(this, 2));

        mShortVideoAdapter = new RecycleViewShortVideoAdapter(this, mVideoList);
        mRvContentList.setAdapter(mShortVideoAdapter);
        mShortVideoAdapter.setOnLoadMoreListener(this, mRvContentList);
        mShortVideoAdapter.setOnItemClickListener(this);
        mShortVideoAdapter.disableLoadMoreIfNotFullPage(mRvContentList);

        initTopBar();

    }

    private void initTopBar() {

        qmuiTopBar.addLeftImageButton(R.drawable.icon_back_black, R.id.all_backbtn).setOnClickListener(this);
        qmuiTopBar.setTitle(getString(R.string.small_video));
        rightBtn = qmuiTopBar.addRightTextButton(getString(R.string.push_video), R.id.right_btn);
        rightBtn.setTextColor(getResources().getColor(R.color.color_4d));
        rightBtn.setTextSize(13);
        rightBtn.setOnClickListener(this);
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {

        //请求视频列表信息
        requestGetVideoList();

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_backbtn:
                finish();
                break;
            case R.id.right_btn:
                clickPushVideo();
                break;

            default:
                break;
        }
    }

    private void clickPushVideo() {
        DialogHelp.getSelectDialog(this, new String[]{getString(R.string.shot_video), getString(R.string.album_chose)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickPushVideo(i);
            }
        }).show();
    }


    //选择本地视频文件
    private void clickSelectVideo() {

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofVideo())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }

    //删除短视频
    private void deleteVideo(final int pos, String videoId) {

        showLoadingDialog(getString(R.string.loading_now_del));
        Api.doDeleteVideoFile(uId, uToken, videoId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    mVideoList.remove(pos);
                    mShortVideoAdapter.notifyDataSetChanged();
                    showToastMsg(getString(R.string.del_success));
                } else {
                    showToastMsg(jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                hideLoadingDialog();
            }
        });
    }

    /**
     * @dw 获取当前用户的视频列表
     */
    private void requestGetVideoList() {

        Api.doGetShortVideoList(uId, uToken, page, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetShortVideoList jsonObj = (JsonRequestGetShortVideoList) JsonRequestBase.getJsonObj(s, JsonRequestGetShortVideoList.class);
                if (jsonObj.getCode() == 1) {

                    if (page == 1) {
                        mVideoList.clear();
                    }
                    mVideoList.addAll(jsonObj.getList());

                    if (jsonObj.getList().size() == 0) {
                        mShortVideoAdapter.loadMoreEnd();
                    } else {
                        mShortVideoAdapter.loadMoreComplete();
                    }
                } else {
                    showToastMsg(jsonObj.getMsg());
                    mShortVideoAdapter.loadMoreEnd();
                }

                mShortVideoAdapter.notifyDataSetChanged();
            }
        });
    }


    //点击发布视频
    private void clickPushVideo(final int type) {

        if (StringUtils.toInt(ConfigModel.getInitData().getUpload_certification()) == 0) {
            if (type == 0) {
                Intent intent = new Intent(ShortVideoActivity.this, VideoRecordActivity.class);
                startActivity(intent);
            } else {
                clickSelectVideo();
            }
            return;
        }

        Api.doRequestGetIsAuth(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JsonGetIsAuth data = (JsonGetIsAuth) JsonRequestBase.getJsonObj(s, JsonGetIsAuth.class);
                if (data.getCode() == 1) {
                    if (data.getIs_auth() == 1) {
                        if (type == 0) {
                            Intent intent = new Intent(ShortVideoActivity.this, VideoRecordActivity.class);
                            startActivity(intent);
                        } else {
                            clickSelectVideo();
                        }
                    } else {
                        showToastMsg("未认证不能发布视频！");
                    }
                } else {
                    showToastMsg("未认证不能发布视频！");
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushVideoCommon(CuckooPushVideoCommonEvent var1) {
        page = 1;
        requestGetVideoList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1 && requestCode == PictureConfig.CHOOSE_REQUEST) {
                //录制监听
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                LogUtils.i("选择视频");
                if (selectList.size() > 0) {
                    LocalMedia videoFile = selectList.get(0);
                    Bitmap thumb = BGVideoFile.getVideoThumbnail(videoFile.getPath());
                    if (thumb != null) {

                        File thumbFile = ImageUtil.getSaveFile(thumb, String.valueOf(System.currentTimeMillis()));
                        //发布编辑
                        Intent intent = new Intent(this, PushShortVideoActivity.class);
                        intent.putExtra(PushShortVideoActivity.VIDEO_PATH, videoFile.getPath());
                        intent.putExtra(PushShortVideoActivity.VIDEO_COVER_PATH, thumbFile.getCanonicalPath());
                        startActivity(intent);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        showMenuDialog(new String[]{getString(R.string.show), getString(R.string.del)}, new MenuDialogClick() {
            @Override
            public void OnMenuItemClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        //查看视频
                        VideoPlayerActivity.startVideoPlayerActivity(ShortVideoActivity.this, mVideoList.get(position));
                        break;

                    case 1:

                        showMsgDialog(getString(R.string.tips), getString(R.string.is_del_video), new MsgDialogClick() {
                            @Override
                            public void doYes(QMUIDialog dialog, int index) {

                                deleteVideo(position, mVideoList.get(position).getId());
                            }

                            @Override
                            public void doNo(QMUIDialog dialog, int index) {

                                dialog.dismiss();
                            }
                        });

                        break;
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {

        //上拉刷新
        page++;
        requestGetVideoList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        page = 1;
        requestGetVideoList();
    }


    public static void startShortVideoActivity(Context context) {

        Intent intent = new Intent(context, ShortVideoActivity.class);
        context.startActivity(intent);
    }


}
