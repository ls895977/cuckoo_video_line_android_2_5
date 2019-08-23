package com.ailiaovideo.videoline.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.ApiConstantDefine;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseFragment;
import com.ailiaovideo.videoline.helper.SelectResHelper;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestUserCenterInfo;
import com.ailiaovideo.videoline.json.jsonmodle.UserCenterData;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.msg.ui.AboutFansActivity;
import com.ailiaovideo.videoline.paypal.PayPalHandle;
import com.ailiaovideo.videoline.ui.CuckooAuthFormActivity;
import com.ailiaovideo.videoline.ui.EditActivity;
import com.ailiaovideo.videoline.ui.RecommendActivity;
import com.ailiaovideo.videoline.ui.ToJoinActivity;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.InviteActivity;
import com.ailiaovideo.videoline.ui.PrivatePhotoActivity;
import com.ailiaovideo.videoline.ui.RechargeActivity;
import com.ailiaovideo.videoline.ui.SettingActivity;
import com.ailiaovideo.videoline.ui.ShortVideoActivity;
import com.ailiaovideo.videoline.ui.VideoAuthActivity;
import com.ailiaovideo.videoline.ui.VideoLineActivity;
import com.ailiaovideo.videoline.ui.WealthActivity;
import com.ailiaovideo.videoline.ui.WebViewActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.ailiaovideo.videoline.ui.dialog.AdManager;
import com.ailiaovideo.videoline.utils.SharedPreferencesUtils;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.BGLevelTextView;
import com.ailiaovideo.videoline.widget.ForScrollViewGridView;
import com.ailiaovideo.videoline.widget.GradeShowLayout;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.youth.banner.transformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的
 */
public class UserPageFragment extends BaseFragment {
    private RelativeLayout userpageMyuserpage, userpageMoneyBtn;
    private BGLevelTextView tv_level;

    private Dialog radioDialog;//分成比例dialog

    //显示数据
    private CircleImageView userImg;//用户头像
    private TextView userName;//用户名
    private ImageView userIsVerify;//用户是否验证图标

    private TextView aboutNumber;//关注人数
    private TextView fansNumber;//粉丝数
    private TextView ratioNumber;//分成比例


    private TextView moneyNumber;//聊币数
    private TextView userpage_rechargetext;
    private RelativeLayout ll_video_auth;
    private RelativeLayout ll_short_video;
    private RelativeLayout ll_private_photo;
    private RelativeLayout ll_invite;
    private RelativeLayout ll_new_guide;
    private RelativeLayout ll_cooperation;
    private RelativeLayout ll_setting;
    private RelativeLayout ll_level;
    private RelativeLayout ll_buyVip;
    private RelativeLayout ll_switch_disturb;

    @BindView(R.id.userpage_id)
    TextView userpage_id;

    @BindView(R.id.iv_switch_disturb)
    ImageView iv_switch_disturb;

    @BindView(R.id.ll_emcee_menu)
    LinearLayout ll_emcee_menu;


    private UserCenterData userCenterData;//个人中心接口返回信息

    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_userpage, container, false);
    }

    @Override
    protected void initView(View view) {
        userImg = view.findViewById(R.id.userpage_img);
        userName = view.findViewById(R.id.userpage_nickname);
        userIsVerify = view.findViewById(R.id.userpage_is_auth);
        aboutNumber = view.findViewById(R.id.love_number);
        fansNumber = view.findViewById(R.id.fans_number);
        ratioNumber = view.findViewById(R.id.divide_number);
        moneyNumber = view.findViewById(R.id.userpage_money_number);
        userpage_rechargetext = view.findViewById(R.id.userpage_rechargetext);

        ll_video_auth = view.findViewById(R.id.ll_video_auth);
        ll_short_video = view.findViewById(R.id.ll_short_video);
        ll_private_photo = view.findViewById(R.id.ll_private_photo);
        ll_invite = view.findViewById(R.id.ll_invite);
        ll_new_guide = view.findViewById(R.id.ll_new_guide);
        ll_cooperation = view.findViewById(R.id.ll_cooperation);
        ll_setting = view.findViewById(R.id.ll_setting);
        ll_level = view.findViewById(R.id.ll_level);
        ll_switch_disturb = view.findViewById(R.id.ll_switch_disturb);
        ll_buyVip = view.findViewById(R.id.ll_buy_vip);
        ll_buyVip.setOnClickListener(this);

        userpageMyuserpage = view.findViewById(R.id.userpage_myuserpage);
        userpageMoneyBtn = view.findViewById(R.id.userpage_money_btn);
        tv_level = view.findViewById(R.id.tv_level);

    }

    @Override
    protected void initDate(View view) {

    }

    @Override
    protected void initSet(View view) {

        userpage_rechargetext.setText(getString(R.string.recharge) + RequestConfig.getConfigObj().getCurrency());
        userpage_id.setText("ID: " + SaveData.getInstance().getId());

        setOnclickListener(view, R.id.count_love_layout, R.id.count_fans_layout, R.id.count_divide_layout,
                R.id.ll_video_auth, R.id.ll_short_video, R.id.ll_private_photo, R.id.ll_invite, R.id.ll_new_guide, R.id.ll_cooperation, R.id.ll_setting, R.id.ll_level);

        setOnclickListener(userpageMyuserpage, userpageMoneyBtn);

        if (StringUtils.toInt(ConfigModel.getInitData().getOpen_invite()) == 1) {
            ll_invite.setVisibility(View.VISIBLE);
        }

        ll_switch_disturb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChangeDoNotDisturbStatus(!userCenterData.isOpenDoNotDisturb());
            }
        });
    }

    @Override
    protected void initDisplayData(View view) {
    }


    @OnClick({R.id.ll_wallet, R.id.ll_recharge})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_recharge:

                RechargeActivity.startRechargeActivity(getContext());
                break;
            //财富
            case R.id.ll_wallet:
                goMoneyPage();
                break;
            case R.id.ll_video_auth:
                //视频认证
                clickVideoAuth();
                break;
            case R.id.ll_short_video:
                //小视频
                ShortVideoActivity.startShortVideoActivity(getContext());
                break;
            case R.id.ll_private_photo:
                //私照
                PrivatePhotoActivity.startPrivatePhotoActivity(getContext(), uId, "", 0);
                break;
            case R.id.ll_level:
                //我的等级
                WebViewActivity.openH5Activity(getContext(), true, getString(R.string.my_level), RequestConfig.getConfigObj().getMyLevelUrl());
                break;
            case R.id.ll_new_guide:
                //新手引导
                WebViewActivity.openH5Activity(getContext(), false, getString(R.string.novice_guide), RequestConfig.getConfigObj().getNewBitGuideUrl());
                break;
            case R.id.ll_setting:
                //设置
                SettingActivity.startSetting(getContext());
                break;
            case R.id.ll_cooperation:
                Intent intent = new Intent(getContext(), ToJoinActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_invite:
                //邀请好友
                //InviteActivity.startInviteAcitivty(getContext());
                WebViewActivity.openH5Activity(getContext(), true, getString(R.string.inviting_friends), ConfigModel.getInitData().getApp_h5().getInvite_share_menu());
                break;
            case R.id.mine_ed:
                goActivity(getContext(), EditActivity.class);
                break;
            //个人主页
            case R.id.userpage_myuserpage:
                goMyUserPage();
                break;
            case R.id.userpage_money_btn:
                goMoneyPage();
                break;
            //关注
            case R.id.count_love_layout:
                goMsgListPage(0);
                break;
            //粉丝
            case R.id.count_fans_layout:
                goMsgListPage(1);
                break;
            case R.id.count_divide_layout:
                //showDialogRatio();
                break;
            case R.id.dialog_close:
                radioDialog.dismiss();
                break;
            case R.id.ll_buy_vip:
                WebViewActivity.openH5Activity(getContext(), true, getString(R.string.vip), ConfigModel.getInitData().getApp_h5().getVip_url());
                break;
            default:
                break;
        }
    }


    //视频认证
    private void clickVideoAuth() {

//        if(StringUtils.toInt(userCenterData.getSex()) == 1){
//            ToastUtils.showLong("男性用户无法认证!");
//            return;
//        }

        if (userCenterData == null) {
            return;
        }

        Intent intent = new Intent(getContext(), CuckooAuthFormActivity.class);
        intent.putExtra(CuckooAuthFormActivity.STATUS, StringUtils.toInt(userCenterData.getUser_auth_status()));
        startActivity(intent);

//        switch (StringUtils.toInt(userCenterData.getUser_auth_status())) {
//
//            case -1: {
//                Intent intent = new Intent(getContext(), CuckooAuthFormActivity.class);
//                startActivity(intent);
//            }
//            break;
//            case 1: {
//                ToastUtils.showLong("视频认证已通过");
//                break;
//            }
//            case 2: {
//                Intent intent = new Intent(getContext(), CuckooAuthFormActivity.class);
//                startActivity(intent);
//            }
//            break;
//            case 0:
//
//                ToastUtils.showLong("正在审核中...");
//                break;
//            default:
//
//                break;
//        }
    }

    /**
     * 显示分成比例对话框
     */
    private void showDialogRatio() {
        radioDialog = showViewDialog(getContext(), R.layout.dialog_ratio_view, new int[]{R.id.dialog_close, R.id.dialog_left_btn, R.id.dialog_right_btn});
        TextView text = radioDialog.findViewById(R.id.radio_radio_text);
        text.setText(userCenterData.getSplit());
    }

    /**
     * 刷新用户资料页面显示
     */
    private void refreshUserData() {
        if (ApiUtils.isTrueUrl(userCenterData.getAvatar())) {
            Utils.loadHeadHttpImg(getContext(), Utils.getCompleteImgUrl(userCenterData.getAvatar()), userImg);
        }
        userName.setText(userCenterData.getUser_nickname());
        tv_level.setLevelInfo(userCenterData.getSex(), userCenterData.getLevel());
        //是否认证标识
        userIsVerify.setImageResource(SelectResHelper.getAttestationRes(StringUtils.toInt(userCenterData.getUser_auth_status())));
        aboutNumber.setText(userCenterData.getAttention_all());
        fansNumber.setText(userCenterData.getAttention_fans());
        ratioNumber.setText(userCenterData.getSplit());
        moneyNumber.setText(userCenterData.getCoin());

        if (StringUtils.toInt(userCenterData.getIs_open_do_not_disturb()) == 1) {
            iv_switch_disturb.setImageResource(R.drawable.me_icon_disturb_on);
        } else {
            iv_switch_disturb.setImageResource(R.drawable.me_icon_disturb_off);
        }
    }

    /**
     * 服务端请求用户数据
     */
    private void requestUserData() {
        Api.getUserDataByMe(
                uId,
                uToken,
                new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonRequestUserCenterInfo jsonRequestUserCenterInfo = JsonRequestUserCenterInfo.getJsonObj(s);
                        if (jsonRequestUserCenterInfo.getCode() == 1) {

                            userCenterData = jsonRequestUserCenterInfo.getData();
                            UserModel userModel = SaveData.getInstance().getUserInfo();
                            userModel.setIs_open_do_not_disturb(userCenterData.getIs_open_do_not_disturb());
                            SaveData.getInstance().saveData(userModel);

                            if (StringUtils.toInt(userCenterData.getSex()) == 2) {
                                userIsVerify.setVisibility(View.VISIBLE);
                            } else {
                                userIsVerify.setVisibility(View.GONE);
                            }

                            //这里只有实名了的女性 预约我的  其他 我的预约
                            if (userCenterData.getSex() == 2 && "1".equals(userCenterData.getUser_auth_status())) {
                                SharedPreferencesUtils.setParam(getContext(), "AccountNature", "anchor");
                            } else {
                                SharedPreferencesUtils.setParam(getContext(), "AccountNature", "boss");
                            }

                            if (userCenterData.getSex() == 2) {
                                ll_emcee_menu.setVisibility(View.VISIBLE);
                            }
                            //log(jsonRequestUserCenterInfo.toString());
                            refreshUserData();

                        } else if (jsonRequestUserCenterInfo.getCode() == ApiConstantDefine.ApiCode.LOGIN_INFO_ERROR) {

                            doLogout();
                        } else {

                            showToastMsg(getContext(), jsonRequestUserCenterInfo.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        ToastUtils.showLong("刷新用户数据失败!");
                    }
                }
        );
    }

    //修改免打扰状态
    private void clickChangeDoNotDisturbStatus(final boolean b) {

        int type = b ? 1 : 2;
        Api.doRequestSetDoNotDisturb(type, SaveData.getInstance().getId(), SaveData.getInstance().getToken(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {

                    UserModel userModel = SaveData.getInstance().getUserInfo();
                    userModel.setIs_open_do_not_disturb(b ? "1" : "0");
                    SaveData.getInstance().saveData(userModel);

                    userCenterData.setIs_open_do_not_disturb(userModel.getIs_open_do_not_disturb());
                    if (StringUtils.toInt(userModel.getIs_open_do_not_disturb()) == 1) {
                        iv_switch_disturb.setImageResource(R.drawable.me_icon_disturb_on);
                    } else {
                        iv_switch_disturb.setImageResource(R.drawable.me_icon_disturb_off);
                    }

                } else {
                    showToastMsg(getContext(), jsonObj.getMsg());
                }
            }
        });
    }


    /**
     * 前往消息列表页面##0关注页--1粉丝页
     *
     * @param i 标识
     */
    private void goMsgListPage(int i) {
        if (i == 0) {
            //关注
            goActivity(getContext(), AboutFansActivity.class, getString(R.string.follow));
        } else {
            //粉丝
            goActivity(getContext(), AboutFansActivity.class, getString(R.string.fans));
        }
    }

    /**
     * 执行购买聊币操作
     *
     * @param money 购买的数量(单位/元[人民币])
     */
    private void goBuyMoney(int money) {
        showToastMsg(getContext(), getString(R.string.buy) + money * 100 + currency);
    }

    /**
     * 跳转到个人主页
     */
    private void goMyUserPage() {

        Common.jumpUserPage(getContext(), uId);
    }

    /**
     * 跳转到个人财富主页
     */
    private void goMoneyPage() {
        //showToastMsg(getContext(),"个人财富");
        WealthActivity.startWealthActivity(getContext());
    }

    /**
     * 退出/注销方法
     */
    private void doLogout() {

        LoginUtils.doLoginOut(getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        requestUserData();//服务端请求用户数据并设置到页面
    }
}
