package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.chat.ui.ChatActivity;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.common.Common;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.ailiaovideo.videoline.utils.CuckooSharedPreUtil;
import com.ailiaovideo.videoline.utils.DialogHelp;
import com.ailiaovideo.videoline.utils.SharedPreferencesUtils;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.qcloud.presentation.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author 魏鹏
 * email 1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 * @dw 设置
 */
public class SettingActivity extends BaseActivity {

    private QMUITopBar qmuiTopBar;
    private QMUIGroupListView groupListView;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        groupListView = findViewById(R.id.groupListView);
        qmuiTopBar = findViewById(R.id.qmui_top_bar);
    }

    @Override
    protected void initSet() {

        initTopBar();
        initGroupList();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    private void initTopBar() {

        qmuiTopBar.addLeftImageButton(R.drawable.icon_back_black, R.id.all_backbtn).setOnClickListener(this);
        qmuiTopBar.setTitle(getString(R.string.setting));

    }

    private void initGroupList() {

        QMUICommonListItemView itemCustomQQView = groupListView.createItemView(getString(R.string.contact_custom_service));
        itemCustomQQView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemCustomQQView.setId(R.id.custom_qq);

        //获取当前APP版本号
        String version = AppUtils.getAppVersionName();

        QMUICommonListItemView itemVersionView = groupListView.createItemView(getString(R.string.version_to_update));
        itemVersionView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemVersionView.setDetailText(version);


        QMUICommonListItemView itemLoginOutView = groupListView.createItemView(getString(R.string.login_out));
        itemLoginOutView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemLoginOutView.setId(R.id.sett_login_out);


        QMUICommonListItemView itemBlackView = groupListView.createItemView(getString(R.string.black_list));
        itemBlackView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemBlackView.setId(R.id.sett_black);

        QMUICommonListItemView itemSettingVideoMoney = groupListView.createItemView(getString(R.string.set_call_mony));
        itemSettingVideoMoney.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemSettingVideoMoney.setId(R.id.sett_custom_video_coin);

        QMUICommonListItemView itemAboutView = groupListView.createItemView(getString(R.string.about_me));
        itemAboutView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemAboutView.setId(R.id.sett_about_me);


        QMUICommonListItemView itemSwitchLanguage = groupListView.createItemView(getString(R.string.switch_language));
        itemSwitchLanguage.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemSwitchLanguage.setId(R.id.sett_switch_language);


        QMUICommonListItemView itemDoNotView = groupListView.createItemView("免打扰");
        itemDoNotView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemDoNotView.setId(R.id.sett_do_not);

        if (StringUtils.toInt(SaveData.getInstance().getUserInfo().getIs_open_do_not_disturb()) == 1) {
            itemDoNotView.getSwitch().setChecked(true);
        }
        itemDoNotView.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                clickChangeDoNotDisturbStatus(b);
            }
        });

        QMUIGroupListView.Section section = QMUIGroupListView.newSection(this)
                .addItemView(itemCustomQQView, this)
                .addItemView(itemBlackView, this)
                .addItemView(itemVersionView, this)
                .addItemView(itemSwitchLanguage, this)
                .addItemView(itemAboutView, this)
                .addItemView(itemLoginOutView, this);

        if (ConfigModel.getInitData().getOpen_custom_video_charge_coin() == 1
                && SaveData.getInstance().getUserInfo().getSex() == 2) {
            section.addItemView(itemSettingVideoMoney, this);
        }
        section.addTo(groupListView);

    }

    //退出登录
    private void loginOut() {

        LoginUtils.doLoginOut(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.custom_qq:
                openCustomServiceQQ();
                break;
            case R.id.sett_login_out:
                //退出登录
                new MaterialDialog.Builder(this)
                        .content("是否退出登录？")
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                loginOut();
                            }
                        })
                        .show();

                break;
            case R.id.sett_about_me:
                WebViewActivity.openH5Activity(this, false, getString(R.string.about_me), RequestConfig.getConfigObj().getAboutUrl());
                //关于我们
                break;
            case R.id.all_backbtn:

                finish();
                break;
            case R.id.sett_black:

                Intent intent = new Intent(this, BlackListActivity.class);
                startActivity(intent);
                break;

            case R.id.sett_custom_video_coin:

                clickCustomVideoCoin();
                break;
            case R.id.sett_switch_language:

                clickSwitchLanguage();
                break;
            default:
                break;
        }
    }

    //切换语言
    private void clickSwitchLanguage() {
        DialogHelp.getSelectDialog(this, new String[]{"中文简体", "中文繁体"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    switchLanguage("zh_simple");
                } else {
                    switchLanguage("zh_traditional");
                }
            }
        }).show();
    }

    private void openCustomServiceQQ() {
        Common.openCustomServiceQQ(this);
    }

    private void clickCustomVideoCoin() {
        WebViewActivity.openH5Activity(this, true, "自定义金额", ConfigModel.getInitData().getApp_h5().getUser_fee());
//        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getNowContext());
//        builder.setTitle(getString(R.string.change_mony))
//                .setPlaceholder("0代表系统默认价格")
//                .setInputType(InputType.TYPE_CLASS_NUMBER)
//                .addAction(getString(R.string.not_change), new QMUIDialogAction.ActionListener() {
//                    @Override
//                    public void onClick(QMUIDialog dialog, int index) {
//
//                        dialog.dismiss();
//                    }
//                })
//                .addAction(getString(R.string.determine_change), new QMUIDialogAction.ActionListener() {
//                    @Override
//                    public void onClick(QMUIDialog dialog, int index) {
//                        CharSequence text = builder.getEditText().getText();
//
//                        changeVideoCoin(text.toString());
//                        dialog.dismiss();
//
//                    }
//                }).show();

    }

    private void changeVideoCoin(String coin) {

        showLoadingDialog(getString(R.string.loading_now_change));
        Api.doChangeVideoChargeCoin(uId, uToken, coin, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.change_success));
                } else {
                    showToastMsg(jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
                showToastMsg(getString(R.string.change_fail));
            }
        });
    }

    private void clickChangeDoNotDisturbStatus(final boolean b) {

        showLoadingDialog(getString(R.string.loading_now_change));
        int type = b ? 1 : 2;
        Api.doRequestSetDoNotDisturb(type, SaveData.getInstance().getId(), SaveData.getInstance().getToken(), new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.change_success));

                    UserModel userModel = SaveData.getInstance().getUserInfo();
                    userModel.setIs_open_do_not_disturb(b ? "1" : "0");
                    SaveData.getInstance().saveData(userModel);
                } else {
                    showToastMsg(jsonObj.getMsg());
                }
            }
        });
    }

    public static void startSetting(Context context) {

        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 切换语言
     *
     * @param language
     */
    private void switchLanguage(String language) {

        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();

        if (language.equals("zh_simple")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
            CuckooSharedPreUtil.put(this, "LANGUAGE", "zh_simple");
        } else if (language.equals("zh_traditional")) {
            config.locale = Locale.TRADITIONAL_CHINESE;
            CuckooSharedPreUtil.put(this, "LANGUAGE", "zh_traditional");
        } else {
            config.locale = Locale.getDefault();
        }

        resources.updateConfiguration(config, dm);

        //更新语言后，destroy当前页面，重新绘制
        finish();
        Intent it = new Intent(this, MainActivity.class);
        //清空任务栈确保当前打开activit为前台任务栈栈顶
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(it);

    }

}
