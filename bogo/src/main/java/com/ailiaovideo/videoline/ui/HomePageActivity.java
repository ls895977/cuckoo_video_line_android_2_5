package com.ailiaovideo.videoline.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.json.JsonRequestTarget;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.CuckooEvaluateAdapter;
import com.ailiaovideo.videoline.adapter.recycler.RecycleUserHomeGiftAdapter;
import com.ailiaovideo.videoline.adapter.recycler.RecycleUserHomePhotoAdapter;
import com.ailiaovideo.videoline.adapter.recycler.RecycleUserHomeVideoAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.inter.AdapterOnItemClick;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonDoRequestGetEvaluateList;
import com.ailiaovideo.videoline.json.JsonRequest;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestGetInviteCode;
import com.ailiaovideo.videoline.json.jsonmodle.TargetUserData;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.CuckooUserEvaluateListModel;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.utils.GlideImageLoader;
import com.ailiaovideo.videoline.utils.QRCodeUtil;
import com.ailiaovideo.videoline.utils.SimpleUtils;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.utils.im.IMHelp;
import com.ailiaovideo.videoline.widget.BGLevelTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.youth.banner.Banner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import okhttp3.Response;

/**
 * player主页
 */

public class HomePageActivity extends BaseActivity implements View.OnClickListener, AdapterOnItemClick {

    @BindView(R.id.rv_evaluate)
    RecyclerView rv_evaluate;

    //功能
    private TextView shareUserMsg;//分享有礼
    private LinearLayout divideNumber;//分成比例
    private ImageView userIsonLine;//是否在线图标
    private SwipeRefreshLayout homePageRefreshLayout;//下拉刷新组件
    private Banner homePageWallpaper;//轮播组件
    private TextView videoRecommend;


    //数据
    private TextView maxLevelText;//当前最高等级
    private TextView userNickname; //当前player名称
    private TextView userTimeText; //通话x小时
    private TextView userGoodText;//好评x%
    private TextView userLocationText;//当前player位置
    private TextView userIsonLineText;//当前player是否在线
    private TextView loveNumber;//关注数量
    private TextView fansNumber;//粉丝数量
    private TextView listBarGiftText;//礼物数量
    private TextView listBarVideoText;//小视频数量
    private TextView listBarPhotoText;//私照数量
    private TextView tvVideoMoney;
    private TextView tv_id;

    private BGLevelTextView tv_level;

    private ImageView userIsattestation;//是否认证

    private RecyclerView listBarGiftList;//礼物列表
    private RecyclerView listBarVideoList;//小视频列表
    private RecyclerView listBarPhotoList;//私照列表

    //视频
    private List<VideoModel> videos = new ArrayList<>();

    //轮播图
    private ArrayList<String> rollPath = new ArrayList<>();

    //收到的礼物列表
    private ArrayList<TargetUserData.GiftBean> giftList = new ArrayList<>();
    //发布的视频list
    private ArrayList<VideoModel> videoList = new ArrayList<>();
    //发布的私照list
    private ArrayList<TargetUserData.PicturesBean> photoList = new ArrayList<>();

    private List<CuckooUserEvaluateListModel> listCuckooEvaluate = new ArrayList<>();

    //按钮
    private TextView userLoveme;//关注这个player

    //else
    private TargetUserData targetUserData;//当前目标用户信息

    private String targetUserId;//目标用户id

    private Dialog dialog;

    //收到的礼物列表适配器
    private RecycleUserHomeGiftAdapter recycleUserHomeGiftAdapter;
    //发布的视频适配器
    private RecycleUserHomeVideoAdapter recycleUserHomeVideoAdapter;
    //发布的私照适配器
    private RecycleUserHomePhotoAdapter recycleUserHomePhotoAdapter;

    private CuckooEvaluateAdapter cuckooEvaluateAdapter;
    //分享view
    private View shareView;
    private View headView;

    private int evaluatePage = 1;

    @Override
    protected void initPlayerDisplayData() {
    }

    @Override
    protected Context getNowContext() {
        return HomePageActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_player_page;
    }

    @Override
    protected void initView() {

        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏

        headView = LayoutInflater.from(this).inflate(R.layout.view_home_page_head, null);

        homePageRefreshLayout = findViewById(R.id.home_page_refresh_layout);


        shareUserMsg = headView.findViewById(R.id.share_user_msg);
        userIsonLine = headView.findViewById(R.id.userinfo_bar_isonLine);
        userIsattestation = headView.findViewById(R.id.userinfo_bar_isattestation);
        userNickname = headView.findViewById(R.id.userinfo_bar_userid);
        userTimeText = headView.findViewById(R.id.userinfo_bar_time_text);
        userGoodText = headView.findViewById(R.id.userinfo_bar_good_text);
        userLocationText = headView.findViewById(R.id.userinfo_bar_location_text);
        userIsonLineText = headView.findViewById(R.id.userinfo_bar_isonLine_text);
        loveNumber = headView.findViewById(R.id.love_number);
        fansNumber = headView.findViewById(R.id.fans_number);
        divideNumber = headView.findViewById(R.id.count_divide_layout);
        listBarGiftList = headView.findViewById(R.id.list_bar_gift_list);
        listBarVideoList = headView.findViewById(R.id.list_bar_video_list);
        listBarPhotoList = headView.findViewById(R.id.list_bar_photo_list);
        listBarGiftText = headView.findViewById(R.id.list_bar_gift_text);
        listBarVideoText = headView.findViewById(R.id.list_bar_video_text);
        listBarPhotoText = headView.findViewById(R.id.list_bar_photo_text);
        userLoveme = headView.findViewById(R.id.userinfo_bar_loveme);
        homePageWallpaper = headView.findViewById(R.id.home_page_wallpaper);
        tv_level = headView.findViewById(R.id.tv_level);
        maxLevelText = headView.findViewById(R.id.userinfo_bar_max_number);
        tvVideoMoney = headView.findViewById(R.id.tv_video_money);
        tv_id = headView.findViewById(R.id.tv_id);

        setOnclickListener(headView, new int[]{R.id.userinfo_bar_loveme, R.id.float_back, R.id.float_share, R.id.float_meun, R.id.contribution_btn});

        //隐藏一些信息
        concealView(divideNumber);

        rv_evaluate.setLayoutManager(new LinearLayoutManager(this));
        cuckooEvaluateAdapter = new CuckooEvaluateAdapter(this, listCuckooEvaluate);
        cuckooEvaluateAdapter.addHeaderView(headView);
        rv_evaluate.setAdapter(cuckooEvaluateAdapter);


    }

    @Override
    protected void initSet() {

        //下拉刷新
        homePageRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homePageRefreshLayout.setRefreshing(false);
                        showToastMsg(getString(R.string.refresh_success));
                    }
                }, 1000);
            }
        });

        //轮播参数设置
        //设置图片加载器
        homePageWallpaper.setImageLoader(new GlideImageLoader());
        //设置图片集合
        homePageWallpaper.setImages(rollPath);
        //banner设置方法全部调用完毕时最后调用
        homePageWallpaper.start();


        //设置收到的礼物列表
        LinearLayoutManager listGiftLayoutManage = new LinearLayoutManager(this);
        listGiftLayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        listBarGiftList.setLayoutManager(listGiftLayoutManage);

        recycleUserHomeGiftAdapter = new RecycleUserHomeGiftAdapter(this, giftList);
        listBarGiftList.setAdapter(recycleUserHomeGiftAdapter);

        //设置发布的小视频列表
        LinearLayoutManager listVideoLayoutManage = new LinearLayoutManager(this);
        listVideoLayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        listBarVideoList.setLayoutManager(listVideoLayoutManage);

        recycleUserHomeVideoAdapter = new RecycleUserHomeVideoAdapter(this, videoList);
        listBarVideoList.setAdapter(recycleUserHomeVideoAdapter);
        recycleUserHomeVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //播放视频
                VideoPlayerActivity.startVideoPlayerActivity(HomePageActivity.this, videoList.get(position));
            }
        });

        //设置发布的私照列表
        LinearLayoutManager listPhotoLayoutManage = new LinearLayoutManager(this);
        listPhotoLayoutManage.setOrientation(LinearLayoutManager.HORIZONTAL);
        listBarPhotoList.setLayoutManager(listPhotoLayoutManage);

        recycleUserHomePhotoAdapter = new RecycleUserHomePhotoAdapter(this, photoList);
        listBarPhotoList.setAdapter(recycleUserHomePhotoAdapter);
        recycleUserHomePhotoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PrivatePhotoActivity.startPrivatePhotoActivity(HomePageActivity.this, targetUserId, "", 0);
            }
        });
    }

    @Override
    protected void doLogout() {
    }

    @Override
    protected void initData() {
        targetUserId = getIntent().getStringExtra("str");
        tv_id.setText("ID: " + targetUserId);
        requestTargetUserData();//获取目标用户信息
        requestGetEvaluate();//获取推荐视频
    }

    private void requestGetEvaluate() {

        Api.doRequestGetEvaluate(targetUserId, evaluatePage, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonDoRequestGetEvaluateList data = (JsonDoRequestGetEvaluateList) JsonRequestBase.getJsonObj(s, JsonDoRequestGetEvaluateList.class);
                if (StringUtils.toInt(data.getCode()) != 0) {
                    listCuckooEvaluate.clear();
                    listCuckooEvaluate.addAll(data.getEvaluate_list());
                    cuckooEvaluateAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({R.id.home_page_chat_layout, R.id.home_page_video_layout, R.id.home_page_gift_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关注这个player
            case R.id.userinfo_bar_loveme:
                loveThisPlayer();
                break;
            //back button
            case R.id.float_back:
                finish();
                break;
            //分享这个视频
            case R.id.float_share:
                shareThisVideo();
                break;
            //菜单按钮
            case R.id.float_meun:
                showFloatMeun();
                break;
            //贡献榜按钮
            case R.id.contribution_btn:
                showContribution();
                break;
            //私聊按钮
            case R.id.home_page_chat_layout:
                showChatPage(false);
                break;
            //视频聊按钮
            case R.id.home_page_video_layout:
                callThisPlayer();
                break;
            //送礼物
            case R.id.home_page_gift_layout:
                showChatPage(true);
                break;
            //加入黑名单操作
            case R.id.join_black_list:
                clickBlack();
                dialog.dismiss();
                break;
            //举报该用户操作
            case R.id.report_this_user:
                clickReport();
                dialog.dismiss();
                break;
            //取消操作
            case R.id.dialog_dis:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    //举报
    private void clickReport() {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra(ReportActivity.REPORT_USER_ID, targetUserId);
        startActivity(intent);
    }

    //拉黑
    private void clickBlack() {

        showLoadingDialog(getResources().getString(R.string.loading_action));
        Api.doRequestBlackUser(uId, uToken, targetUserId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getResources().getString(R.string.action_success));
                    if (targetUserData.getIs_black() == 1) {
                        targetUserData.setIs_black(0);
                        IMHelp.deleteBlackUser(targetUserId, new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                LogUtils.i("解除拉黑用户失败:" + s);
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                LogUtils.i("解除拉黑用户成功");
                            }
                        });
                    } else {
                        targetUserData.setIs_black(1);
                        IMHelp.addBlackUser(targetUserId, new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                LogUtils.i("拉黑用户失败:" + s);
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                LogUtils.i("拉黑用户成功");
                            }
                        });
                    }
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

    @Override
    public void onItemClick(View view, AdapterOnItemClick.ViewName viewName, final int position) {
        //在此处理点击事件即可，viewName可以区分是item还是内部控件

        VideoPlayerActivity.startVideoPlayerActivity(this, videos.get(position));
    }

    /**
     * 获取目标用户基础信息
     */
    private void requestTargetUserData() {
        Api.getUserData(
                targetUserId,
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JsonRequestTarget jsonTargetUser = JsonRequestTarget.getJsonObj(s);
                        if (jsonTargetUser.getCode() == 1) {
                            targetUserData = jsonTargetUser.getData();
                            //log("targetUserData::"+targetUserData);
                            initDisplayData();
                            //requestUserPateRoll();
                        } else {
                            //请求失败
                            showToastMsg(jsonTargetUser.getMsg());
                        }
                    }
                }
        );
    }

    /**
     * 获取推荐视频
     */
//    private void requestRecommendVideo() {
//        Api.getVideoPageList(
//                uId,
//                uToken,
//                ApiUtils.VideoType.reference,
//                MyApplication.getInstances().getLocation().get("lat"),
//                MyApplication.getInstances().getLocation().get("lng"),
//                new JsonCallback() {
//                    @Override
//                    public Context getContextToJson() {
//                        return getNowContext();
//                    }
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        JsonRequestsVideo requestObj = JsonRequestsVideo.getJsonObj(s);
//                        if (requestObj.getCode() == 1){
//                            videos = requestObj.getData();
//                            //log(videos.toString());//打印获取到的视频
//                        }else
//                            showToastMsg(requestObj.getMsg());
//                    }
//
//                    @Override
//                    public String returnMsg() {
//                        return "获取推荐视频";
//                    }
//                }
//        );
//    }

    /**
     * 初始化显示页面数据
     */
    private void initDisplayData() {
        //初始化显示页面数据
        maxLevelText.setText(targetUserData.getMax_level());
        tv_level.setLevelInfo(targetUserData.getSex(), targetUserData.getLevel());
        //设置是否显示关注
        userLoveme.setVisibility(StringUtils.toInt(targetUserData.getAttention()) == 1 ? View.GONE : View.VISIBLE);

        userNickname.setText(targetUserData.getUser_nickname());
        userTimeText.setText(getString(R.string.home_page_call) + targetUserData.getCall());
        userGoodText.setText(getString(R.string.home_page_good) + targetUserData.getEvaluation());
        userLocationText.setText(targetUserData.getAddress());
        tvVideoMoney.setText(targetUserData.getVideo_deduction() + RequestConfig.getConfigObj().getCurrency() + "/分钟");
        userIsattestation.setImageResource(SelectResHelper.getAttestationRes(StringUtils.toInt(targetUserData.getUser_status())));
        loveNumber.setText(targetUserData.getAttention_all());
        fansNumber.setText(targetUserData.getAttention_fans());

        //是否在线
        userIsonLineText.setText(StringUtils.toInt(targetUserData.getIs_online()) == 1 ? "在线" : "离线");
        userIsonLine.setBackgroundResource(SelectResHelper.getOnLineRes(StringUtils.toInt(targetUserData.getIs_online())));

        //礼物--视频--私照--统计个数
        listBarVideoText.setText(getString(R.string.home_page_small_video) + "(" + objToString(targetUserData.getVideo_count()) + ")");
        listBarPhotoText.setText(getString(R.string.home_page_private_photo) + "(" + objToString(targetUserData.getPictures_count()) + ")");

        if (targetUserId.equals(SaveData.getInstance().getId())) {
            //查询自己
            userLoveme.setVisibility(View.GONE);
            listBarGiftText.setText(getString(R.string.home_page_send_gift) + "(" + objToString(targetUserData.getGift_count()) + ")");
        } else {
            listBarGiftText.setText(getString(R.string.home_page_received) + "(" + objToString(targetUserData.getGift_count()) + ")");
        }

        if (targetUserData.getImg() != null) {
            for (TargetUserData.ImgBean img : targetUserData.getImg()) {
                rollPath.add(Utils.getCompleteImgUrl(img.getImg()));
            }
            if (targetUserData.getImg().size() == 0) {
                rollPath.add(Utils.getCompleteImgUrl(targetUserData.getAvatar()));
            }
        }

        //填充收到的礼物列表
        if (targetUserData.getGift() != null) {
            giftList.addAll(targetUserData.getGift());
            recycleUserHomeGiftAdapter.notifyDataSetChanged();
        }

        //填充发布的视频列表
        if (targetUserData.getVideo() != null) {
            videoList.addAll(targetUserData.getVideo());
            recycleUserHomePhotoAdapter.notifyDataSetChanged();
        }
        //填充发布的私照列表
        if (targetUserData.getPictures() != null) {
            photoList.addAll(targetUserData.getPictures());
            recycleUserHomeVideoAdapter.notifyDataSetChanged();
        }
        refreshRollView();

    }

    /**
     * 刷新轮播图
     */
    private void refreshRollView() {
        //log("刷新轮播");
        homePageWallpaper.setImages(rollPath);
        homePageWallpaper.start();
    }

    //给这个player打电话
    private void callThisPlayer() {
        showToastMsg(getString(R.string.home_page_loading_call));

        Common.callVideo(this, targetUserId,0);
    }

    //显示聊天页面
    private void showChatPage(boolean isShowGift) {

        Common.startPrivatePage(this, targetUserId);

    }

    //显示贡献榜
    private void showContribution() {
        Intent intent = new Intent(this, UserContribuionRankActivity.class);
        intent.putExtra(UserContribuionRankActivity.TO_USER_ID, targetUserId);
        startActivity(intent);
    }

    //显示菜单
    private void showFloatMeun() {
        int[] a = {R.id.join_black_list, R.id.report_this_user, R.id.dialog_dis};
        dialog = showButtomDialog(R.layout.dialog_float_meun, a, 20);
        TextView tv = dialog.findViewById(R.id.join_black_list);
        if (targetUserData.getIs_black() == 1) {
            tv.setText(getResources().getString(R.string.relieve_black));
        }
        dialog.show();
    }

    //分享这个视频
    private void shareThisVideo() {
        concealView(shareUserMsg);
        showSimpleBottomSheetGrid();
    }

    //关注这个player
    private void loveThisPlayer() {
        Api.doLoveTheUser(
                targetUserId,
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JsonRequest requestObj = JsonRequest.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            concealView(userLoveme);//隐藏关注按钮
                            showToastMsg(getResources().getString(R.string.action_success));
                        } else {
                            log("关注当前player:" + requestObj.getMsg());
                        }
                    }
                }
        );
    }

    /**
     * 显示一个分享框
     */
    private void showSimpleBottomSheetGrid() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(HomePageActivity.this);
        builder.addItem(R.drawable.icon_weixin_friend, getString(R.string.share_wechat), TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_weixin_moment, getString(R.string.share_pyq), TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                showToastMsg(getString(R.string.share_wechat));
                                createImageText(Wechat.NAME);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                showToastMsg(getString(R.string.share_pyq));
                                createImageText(WechatMoments.NAME);
                                break;
                        }
                    }
                }).build().show();
    }

    //生成分享图文
    private void createImageText(final String platform) {

        Api.doGetInviteCode(SaveData.getInstance().getId(), SaveData.getInstance().getToken(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestGetInviteCode data = (JsonRequestGetInviteCode) JsonRequestBase.getJsonObj(s, JsonRequestGetInviteCode.class);
                if (data.getCode() == 1) {
                    String inviteCode = data.getInvite_code();
                    createImageText2(platform, inviteCode);
                }
            }
        });

    }

    private void createImageText2(final String platform, String inviteCode) {
        shareView = View.inflate(this, R.layout.view_share, null);

        Bitmap qrcodeBitmap = QRCodeUtil.createQRCodeBitmap(RequestConfig.getConfigObj().getInviteShareRegUrl() + "?invite_code=" + inviteCode, ConvertUtils.dp2px(150), ConvertUtils.dp2px(150));
        ImageView qrCodeImg = shareView.findViewById(R.id.iv_code);
        qrCodeImg.setImageBitmap(qrcodeBitmap);

        final TextView name = shareView.findViewById(R.id.tv_name);
        name.setText(targetUserData.getUser_nickname());
        ImageView avatar = (ImageView) shareView.findViewById(R.id.iv_avatar);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        avatar.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenWidth()));
        Utils.loadHttpImg(Utils.getCompleteImgUrl(targetUserData.getAvatar()), avatar);

        // 没有显示到界面上的view本身无大小可言，所以我们要手动指定一下
        SimpleUtils.layoutView(shareView, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());


//        ImageView imageView = new ImageView(this);
//        imageView.setImageBitmap(cacheBitmapFromView);
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(imageView);
//        dialog.show();


        tvVideoMoney.postDelayed(new Runnable() {
            @Override
            public void run() {
                // View生成截图
                Bitmap cacheBitmapFromView = SimpleUtils.getCacheBitmapFromView(shareView);

                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    cacheBitmapFromView.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    //System.out.println("___________保存的__sd___下_______________________");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showShare(file.getPath(), platform);
            }
        }, 3000);
    }


    private void showShare(String file, String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        //oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        //oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(file);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        //启动分享
        oks.show(this);
    }
}