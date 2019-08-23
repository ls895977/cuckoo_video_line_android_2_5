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
import com.ailiaovideo.videoline.ui.ToJoinActivity;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.InviteActivity;
import com.ailiaovideo.videoline.ui.PrivatePhotoActivity;
import com.ailiaovideo.videoline.ui.RechargeActivity;
import com.ailiaovideo.videoline.ui.SettingActivity;
import com.ailiaovideo.videoline.ui.ShortVideoActivity;
import com.ailiaovideo.videoline.ui.VideoAuthActivity;
import com.ailiaovideo.videoline.ui.WealthActivity;
import com.ailiaovideo.videoline.ui.WebViewActivity;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.ailiaovideo.videoline.widget.BGLevelTextView;
import com.ailiaovideo.videoline.widget.ForScrollViewGridView;
import com.ailiaovideo.videoline.widget.GradeShowLayout;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.QMUITopBar;

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
public class UserPage2Fragment extends BaseFragment {
    private QMUITopBar mTopBar;
    private FrameLayout buyOne, buyTwo, buyThree;
    private RelativeLayout userpageMyuserpage, userpageMoneyBtn;
    private ForScrollViewGridView buyGrid;
    private SimpleAdapter simpleAdapter;
    private String[] titles = {
            "认证",
            "小视频",
            "私照",
            "邀请好友",
            "新手引导",
            "我的等级",
            "公会/主播合作",
            "设置",
    };
    private int[] imgRess = {
            R.drawable.icon_video_verify,
            R.drawable.icon_small_video,
            R.drawable.icon_private_photo,
            R.drawable.icon_invite,
            R.drawable.icon_new_guide,
            R.drawable.icon_mine_level,
            R.drawable.icon_cooperation,
            R.drawable.icon_setting,

    };
    private Map<String, Object> map;
    private List<Map<String, Object>> list;

    private FrameLayout gradesLayout;

    private UserCenterData userCenterData;//个人中心接口返回信息

    private Dialog radioDialog;//分成比例dialog

    //显示数据
    private CircleImageView userImg;//用户头像
    private TextView userName;//用户名
    private ImageView userIsVerify;//用户是否验证图标

    private TextView aboutNumber;//关注人数
    private TextView fansNumber;//粉丝数
    private TextView ratioNumber;//分成比例

    private TextView moneyNumber;//聊币数
    private TextView oneMoney;//第一个充值钱数
    private TextView oneMoneyTo;//第一个充值钱数对应的货币数
    private TextView twoMoney;//第二个充值钱数
    private TextView twoMoneyTo;//第二个充值钱数对应的货币数
    private TextView threeMoney;//第三个充值钱数
    private TextView threeMoneyTo;//第三个充值钱数对应的货币数
    private TextView tvMoreChargeRule;//更多购买规则
    private TextView userpage_rechargetext;


    ////////////////////////////////////////////初始化操作////////////////////////////////////////////
    @Override
    protected View getBaseView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_user_page2, container, false);
    }

    @Override
    protected void initView(View view) {
        userImg = view.findViewById(R.id.userpage_img);
        userName = view.findViewById(R.id.userpage_nickname);
        userIsVerify = view.findViewById(R.id.userpage_isattestation);
        aboutNumber = view.findViewById(R.id.love_number);
        fansNumber = view.findViewById(R.id.fans_number);
        ratioNumber = view.findViewById(R.id.divide_number);
        moneyNumber = view.findViewById(R.id.userpage_money_number);
        oneMoney = view.findViewById(R.id.rechargeSellNumber);
        oneMoneyTo = view.findViewById(R.id.rechargeBuyNumber);
        twoMoney = view.findViewById(R.id.rechargeSellNumber2);
        twoMoneyTo = view.findViewById(R.id.rechargeBuyNumber2);
        threeMoney = view.findViewById(R.id.rechargeSellNumber3);
        threeMoneyTo = view.findViewById(R.id.rechargeBuyNumber3);
        tvMoreChargeRule = view.findViewById(R.id.tv_more_charge_rule);
        userpage_rechargetext = view.findViewById(R.id.userpage_rechargetext);
        tvMoreChargeRule.setOnClickListener(this);

        buyOne = view.findViewById(R.id.buyOne);
        buyTwo = view.findViewById(R.id.buyTwo);
        buyThree = view.findViewById(R.id.buyThree);
        mTopBar = view.findViewById(R.id.userpage_topbar);
        buyGrid = view.findViewById(R.id.userpage_grid);
        userpageMyuserpage = view.findViewById(R.id.userpage_myuserpage);
        userpageMoneyBtn = view.findViewById(R.id.userpage_money_btn);
        gradesLayout = view.findViewById(R.id.grades_layout);


    }

    @Override
    protected void initDate(View view) {
        //初始化数据源
        list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            map = new HashMap<>();
            map.put("titles", titles[i]);
            map.put("imgs", imgRess[i]);
            list.add(map);
        }
    }

    @Override
    protected void initSet(View view) {
        String[] from = {"titles", "imgs"};
        int[] to = {R.id.text_title, R.id.img_title};

        userpage_rechargetext.setText("购买" + RequestConfig.getConfigObj().getCurrency());
        simpleAdapter = new SimpleAdapter(getContext(), list, R.layout.adapter_grid_set, from, to);
        buyGrid.setAdapter(simpleAdapter);
        //mTopBar.setTitle("我的");
        mTopBar.addRightImageButton(R.drawable.mine_edit, R.id.mine_ed).setOnClickListener(this);

        //取消gridview的焦点控制
        buyGrid.setFocusable(false);

        //设置监听##关注页--粉丝页--分成比例对话框--更多购买--
        setOnclickListener(view, R.id.count_love_layout, R.id.count_fans_layout, R.id.count_divide_layout, R.id.by_money_btn);
        //设置监听##购买39--购买59--购买199--user信息页面--我的财富页面
        setOnclickListener(buyOne, buyTwo, buyThree, userpageMyuserpage, userpageMoneyBtn);

        buyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (titles[position].equals("注销")) {
                    //showToastMsg(getContext(),"注销成功!");
                    doLogout();
                } else if (titles[position].equals("公会/主播合作")) {
                    goActivity(getContext(), ToJoinActivity.class);
                } else {

                    clickMenu(position);
                }
            }
        });
    }

    //点击了宫格菜单
    private void clickMenu(int position) {

        switch (position) {

            case 0:
                //视频认证
                clickVideoAuth();
                break;
            case 1:
                //小视频
                ShortVideoActivity.startShortVideoActivity(getContext());
                break;
            case 2:
                //私照
                PrivatePhotoActivity.startPrivatePhotoActivity(getContext(), uId, "", 0);
                break;
            case 3:
                //邀请好友
                //InviteActivity.startInviteAcitivty(getContext());
                WebViewActivity.openH5Activity(getContext(), true, getString(R.string.inviting_friends), ConfigModel.getInitData().getApp_h5().getInvite_share_menu());
                break;
            case 4:
                //新手引导
                WebViewActivity.openH5Activity(getContext(), false, "新手引导", RequestConfig.getConfigObj().getNewBitGuideUrl());
                break;
            case 5:
                //我的等级
                WebViewActivity.openH5Activity(getContext(), true, "我的等级", RequestConfig.getConfigObj().getMyLevelUrl());
                break;
            case 6:
                //工会合作
                break;
            case 7:
                //设置

                SettingActivity.startSetting(getContext());
                break;

            default:
                break;
        }
    }

    //视频认证
    private void clickVideoAuth() {

        if (userCenterData == null) {
            return;
        }

        Intent intent = new Intent(getContext(), CuckooAuthFormActivity.class);
        intent.putExtra(CuckooAuthFormActivity.STATUS, StringUtils.toInt(userCenterData.getUser_auth_status()));
        startActivity(intent);
    }

    @Override
    protected void initDisplayData(View view) {
        requestUserData();//服务端请求用户数据并设置到页面
    }

    ////////////////////////////////////////////监听事件处理//////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more_charge_rule:

                break;
            case R.id.mine_ed:
                goActivity(getContext(), EditActivity.class);
                break;
            case R.id.buyOne:
                goBuyMoney(39);
                break;
            case R.id.buyTwo:
                goBuyMoney(59);
                break;
            case R.id.buyThree:
                goBuyMoney(199);
                break;
            case R.id.userpage_myuserpage:
                goMyUserPage();
                break;
            case R.id.userpage_money_btn:
                goMoneyPage();
                break;
            case R.id.count_love_layout:
                goMsgListPage(0);
                break;
            case R.id.count_fans_layout:
                goMsgListPage(1);
                break;
            case R.id.count_divide_layout:
                //showDialogRatio();
                break;
            case R.id.by_money_btn:
                goByMoneyMore();
                break;
            case R.id.dialog_left_btn:
                goMyGraderPage();
                break;
            case R.id.dialog_right_btn:
                goInvitePage();
                break;
            case R.id.dialog_close:
                radioDialog.dismiss();
                break;
            default:
                break;
        }
    }

    //////////////////////////////////////////业务逻辑处理////////////////////////////////////////////

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
                            //log(jsonRequestUserCenterInfo.toString());
                            refreshUserData();
                            refreshOtherData();

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

    /*
     * 填充除了个人信息以外的其他数据
     * */
    private void refreshOtherData() {

        //填充充值列表数据
        if (userCenterData.getPay_coin() != null) {

            if (userCenterData.getPay_coin().size() == 1) {
                oneMoney.setText(userCenterData.getPay_coin().get(0).getMoney());
                oneMoneyTo.setText(userCenterData.getPay_coin().get(0).getFormatCoin());
            } else if (userCenterData.getPay_coin().size() == 2) {
                oneMoney.setText(userCenterData.getPay_coin().get(0).getMoney());
                oneMoneyTo.setText(userCenterData.getPay_coin().get(0).getFormatCoin());
                twoMoney.setText(userCenterData.getPay_coin().get(1).getMoney());
                twoMoneyTo.setText(userCenterData.getPay_coin().get(1).getFormatCoin());
            } else if (userCenterData.getPay_coin().size() == 3) {
                oneMoney.setText(userCenterData.getPay_coin().get(0).getMoney());
                oneMoneyTo.setText(userCenterData.getPay_coin().get(0).getFormatCoin());
                twoMoney.setText(userCenterData.getPay_coin().get(1).getMoney());
                twoMoneyTo.setText(userCenterData.getPay_coin().get(1).getFormatCoin());
                threeMoney.setText(userCenterData.getPay_coin().get(2).getMoney());
                threeMoneyTo.setText(userCenterData.getPay_coin().get(2).getFormatCoin());
            }

        }

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
            Utils.loadHttpImg(getContext(), Utils.getCompleteImgUrl(userCenterData.getAvatar()), userImg);
        }
        userName.setText(userCenterData.getUser_nickname());
        gradesLayout.addView(new GradeShowLayout(getContext(), userCenterData.getLevel(), userCenterData.getSex()));//等级-性别

        //是否认证标识
        userIsVerify.setImageResource(SelectResHelper.getAttestationRes(StringUtils.toInt(userCenterData.getUser_auth_status())));
        aboutNumber.setText(userCenterData.getAttention_all());
        fansNumber.setText(userCenterData.getAttention_fans());
        ratioNumber.setText(userCenterData.getSplit());
        moneyNumber.setText(userCenterData.getCoin());
    }

    /**
     * 前往邀请页面
     */
    private void goInvitePage() {
        //showToastMsg(getContext(),"前往邀请页面");
        radioDialog.dismiss();
    }

    /**
     * 前往我的等级页面
     */
    private void goMyGraderPage() {
        //showToastMsg(getContext(),"前往我的等级页面");
        radioDialog.dismiss();
    }

    /**
     * 前往购买更多页面
     */
    private void goByMoneyMore() {
        //showToastMsg(getContext(),"购买更多");

        RechargeActivity.startRechargeActivity(getContext());
    }

    /**
     * 前往消息列表页面##0关注页--1粉丝页
     *
     * @param i 标识
     */
    private void goMsgListPage(int i) {
        if (i == 0) {
            //关注
            goActivity(getContext(), AboutFansActivity.class, "关注");
        } else {
            //粉丝
            goActivity(getContext(), AboutFansActivity.class, "粉丝");
        }
    }

    /**
     * 执行购买聊币操作
     *
     * @param money 购买的数量(单位/元[人民币])
     */
    private void goBuyMoney(int money) {
        showToastMsg(getContext(), "购买" + money * 100 + currency);
    }

    /**
     * 跳转到个人主页
     */
    private void goMyUserPage() {
        goActivity(getContext(), HomePageActivity.class, uId);
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

}
