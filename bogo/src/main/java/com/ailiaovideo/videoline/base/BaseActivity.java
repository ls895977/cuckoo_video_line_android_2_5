package com.ailiaovideo.videoline.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.dialog.DoCallVideoWaitDialog;
import com.ailiaovideo.videoline.event.BGEventObserver;
import com.ailiaovideo.videoline.event.BaseEvent;
import com.ailiaovideo.videoline.event.CuckooOnLoginTimSuccessEvent;
import com.ailiaovideo.videoline.event.EImVideoCallMessages;
import com.ailiaovideo.videoline.helper.ContentUtils;
import com.ailiaovideo.videoline.helper.TxLogin;
import com.ailiaovideo.videoline.inter.MenuDialogClick;
import com.ailiaovideo.videoline.inter.MsgDialogClick;
import com.ailiaovideo.videoline.manage.AppManager;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsg;
import com.ailiaovideo.videoline.modle.custommsg.CustomMsgVideoCall;
import com.ailiaovideo.videoline.ui.CuckooVideoCallWaitActivity;
import com.ailiaovideo.videoline.ui.CuckooVoiceCallActivity;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.ailiaovideo.videoline.ui.RegisterSelectActivity;
import com.ailiaovideo.videoline.utils.BGViewUtil;
import com.ailiaovideo.videoline.utils.IMUtils;
import com.ailiaovideo.videoline.utils.UserOnlineHeartUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.imsdk.TIMCallBack;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 基础Activity类
 * Created by fly on 2017/12/23 0023.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener, BGEventObserver {


    @BindView(R.id.bast_top_bar)
    QMUITopBar mTopBar;

    //当前页上下文关系
    private Context context;
    private String exceptionMsg;
    //当前页是否点击返回退出app
    private boolean isGoBack;

    //数据
    protected String uId;//id
    protected String uToken;//token
    protected String currency;//全局货币信息

    protected QMUITipDialog tipD;//声明一个QMUITipDialog对象

    //log初始化参数
    private LogUtils.Builder mBuilder = new LogUtils.Builder();

    private DoCallVideoWaitDialog doCallVideoWaitDialog;
    private LinearLayout rootLayout;

    /////////////////////////////////////////抽象方法////////////////////////////////////////////////

    /**
     * 获取当前上下文
     *
     * @return context
     */
    protected abstract Context getNowContext();

    /**
     * 获取当前布局文件资源id
     *
     * @return layoutResId
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化设置
     */
    protected abstract void initSet();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected boolean isFullScreen() {
        return false;
    }

    /**
     * 初始化页面显示
     */
    protected abstract void initPlayerDisplayData();

    ///////////////////////////////////初始化策略&&生命周期管理////////////////////////////////////////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFullScreen()) {
            //去除标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //去除状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
        setContentView(R.layout.act_base_layout);
        if (getLayoutRes() != 0) {
            setContentView(View.inflate(this, getLayoutRes(), null));
        }
        //初始化静态数据
        initStaticData();
        init();//初始化方法
        context = getNowContext();//初始化上下文
        isGoBack = currentPageFinsh();//初始化isGoBack
        AppManager.getAppManager().addActivity((Activity) context);//创建时将当前activity纳入AppManager管理
        mBuilder.setGlobalTag("Cuckoo_Log")
                .setLogHeadSwitch(true)
                .setLog2FileSwitch(false)
                .setBorderSwitch(true);

    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    private void initBar() {
        mTopBar.setBackgroundResource(R.color.white);

        ImageView back = new ImageView(this);
        back.setImageResource(R.drawable.icon_back_black);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(QMUIDisplayHelper.dp2px(this, 20), QMUIDisplayHelper.dp2px(this, 20));
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.leftMargin = ConvertUtils.dp2px(10);
        back.setLayoutParams(params);
        getTopBar().addLeftView(back, R.id.all_backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (hasTopBar()) {
            mTopBar.setVisibility(View.VISIBLE);
        }
    }

    protected QMUITopBar getTopBar() {
        return mTopBar;
    }

    /**
     * 初始化静态数据
     */
    protected void initStaticData() {
        //用户id和token
        uId = SaveData.getInstance().getId();
        uToken = SaveData.getInstance().getToken();
        //常量
        currency = RequestConfig.getConfigObj().getCurrency();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalVideoCallEvent(EImVideoCallMessages var1) {
        if (MyApplication.getInstances().isInVideoCallWait()) {
            return;
        }

        LogUtils.i("收到消息一对一视频请求消息:" + var1.msg.getCustomMsg().getSender().getUser_nickname());

        try {
            CustomMsg customMsg = var1.msg.getCustomMsg();
//            doCallVideoWaitDialog = new DoCallVideoWaitDialog(this, customMsg.getSender(), ((CustomMsgVideoCall) customMsg).getChannel(), ((CustomMsgVideoCall) customMsg).getIs_free());
//            doCallVideoWaitDialog.show();
            Intent intent = new Intent(this, CuckooVideoCallWaitActivity.class);
            intent.putExtra(CuckooVideoCallWaitActivity.CALL_USER_INFO, customMsg.getSender());
            intent.putExtra(CuckooVideoCallWaitActivity.CHANNEL_ID, ((CustomMsgVideoCall) customMsg).getChannel());
            intent.putExtra(CuckooVideoCallWaitActivity.IS_USE_FREE, ((CustomMsgVideoCall) customMsg).getIs_free());
            intent.putExtra(CuckooVideoCallWaitActivity.CALL_TYPE, ((CustomMsgVideoCall) customMsg).getCall_type());
            startActivity(intent);
        } catch (Exception e) {
            LogUtils.i("跳转接通电话页面错误error" + e.getMessage());
        }

    }


    @Override
    protected void onStart() {
        //Activity创建或者从后台重新回到前台时被调用
        super.onStart();
        EventBus.getDefault().register(this);
        initPlayerDisplayData();//初始化页面显示信息
    }


    @Override
    protected void onStop() {
        //退出当前Activity或者跳转到新Activity时被调用
        super.onStop();
        EventBus.getDefault().unregister(this);
        //初始化心跳
        //UserOnlineHeartUtils.getInstance().stopHeartTime();
        doStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Activity创建或者从被覆盖、后台重新回到前台时被调用
        refreshActivity();

        MobclickAgent.onResume(this);

        if (SaveData.getInstance().isIsLogin()) {

            TxLogin.loginIm(
                    SaveData.getInstance().getId(),
                    SaveData.getInstance().getUserSig(),
                    new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            //登录失败
                            log("腾讯云登录有误!" + s);
                        }

                        @Override
                        public void onSuccess() {
                            //登录成功
                            log("腾讯云登录成功!");
                        }
                    }
            );
        }
    }

    @Override
    protected void onPause() {
        //Activity被覆盖到下面或者锁屏时被调用
        super.onPause();
        //有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据
        saveData();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        //Activity从后台重新回到前台时被调用
        super.onRestart();
        doBack();
    }

    @Override
    protected void onDestroy() {
        //退出当前Activity时被调用,调用之后Activity就结束了
        super.onDestroy();
        //doLogout();
        //AppManager.getAppManager().finishActivity((Activity)context);//结束时将当前activity在栈中清除
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 初始化操作
     */
    private void init() {
        ButterKnife.bind(this);
        initBar();
        initView();//实例化控件
        initData();//初始化数据
        initSet();//设置控件属性
        //registerObserver();//注册登录观察者
        //isOnLine();//注册在线状态观察者
    }

    //////////////////////////////////////////可重写方法//////////////////////////////////////////////

    protected boolean hasTopBar() {
        return false;
    }

    /**
     * 当前页是否返回主页,默认false,重写返回true可以实现当前页点击返回退出app操作
     *
     * @return
     */
    protected boolean currentPageFinsh() {
        return false;
    }

    /**
     * 保存数据方法,在onPause中执行;
     * 默认无操作,可重写;
     */
    protected void saveData() {
    }

    /**
     * 刷新相关操作,在onResume中执行;
     * 默认无操作,可重写;
     */
    protected void refreshActivity() {
    }

    /**
     * 停止相关操作,在onStop中执行;
     * 默认无操作,可重写;
     */
    protected void doStop() {
    }

    /**
     * 返回相关操作,在onRestart中执行
     * 默认无操作,可重写;
     */
    protected void doBack() {
    }

    /**
     * 执行注销操作,在onDestroy中执行
     * 默认无操作,可重写;
     */
    protected void doLogout() {
    }

    ///////////////////////////////////////////工具方法//////////////////////////////////////////////

    /**
     * 批量更改控件显示状态
     *
     * @param view
     */
    protected void showOrConceal(View... view) {
        for (View v : view) {
            v.setVisibility(v.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 批量更改控件显示状态
     *
     * @param ids
     */
    protected void showOrConceal(int... ids) {
        View v = null;
        for (int id : ids) {
            v = findViewById(id);
            v.setVisibility(v.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示控件
     *
     * @param view
     * @return
     */
    protected View showView(View view) {
        view.setVisibility(View.VISIBLE);
        return view;
    }

    /**
     * 隐藏控件
     *
     * @param view
     * @return
     */
    protected View concealView(View view) {
        view.setVisibility(View.GONE);
        return view;
    }

    /**
     * 显示控件
     *
     * @param idRes
     * @return
     */
    protected void showView(int idRes) {
        findViewById(idRes).setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏控件(GONE)
     *
     * @param idRes
     * @return
     */
    protected void concealView(int idRes) {
        findViewById(idRes).setVisibility(View.GONE);
    }

    /**
     * 隐藏控件(INVISIBLE)
     *
     * @param idRes
     * @return
     */
    protected void hideView(int idRes) {
        findViewById(idRes).setVisibility(View.INVISIBLE);
    }

    /**
     * Activity跳转方法
     *
     * @param cls
     * @return 返回Activity对象
     */
    protected Activity goActivity(Class cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        return (Activity) context;
    }



    /**
     * Activity跳转方法(跳转到用户主页)
     *
     * @param id 目标用户id
     * @return 返回Activity对象
     */
    protected Activity goActivityMain(String id) {
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        return (Activity) context;
    }

    /**
     * Activity跳转方法
     *
     * @param cls 目标位置
     * @param str 字符串参数
     * @return 获取:String str = getIntent().getStringExtra("str");
     */
    protected Activity goActivity(Class cls, String str) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("str", str);
        startActivity(intent);
        return (Activity) context;
    }

    /**
     * Activity跳转方法
     *
     * @param cls   目标位置
     * @param inter 整型参数
     * @return 获取:int inter = getIntent().getInterExtra("inter");
     */
    protected Activity goActivity(Class cls, int inter) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("inter", inter);
        startActivity(intent);
        return (Activity) context;
    }

    /**
     * Activity跳转方法
     *
     * @param cls 目标位置
     * @param obj 序列化之后的对象
     * @return 获取:Object obj = getIntent().getParcelableExtra("obj");
     */
    protected Activity goActivity(Class cls, Object obj) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("obj", (Parcelable) obj);
        startActivity(intent);
        return (Activity) context;
    }

    /**
     * Activity跳转方法
     *
     * @param cls    目标位置
     * @param bundle bundle对象
     * @return
     */
    protected Activity goActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        return (Activity) context;
    }

    /**
     * log
     *
     * @param msg
     */
    protected void log(String msg) {
        LogUtils.i(msg);
    }

    /**
     * 删除当前页
     */
    protected void finishNow() {
        AppManager.getAppManager().finishActivity();
    }

    /**
     * 删除指定的activity
     *
     * @param activity activity
     */
    protected void finishActivity(Activity activity) {
        AppManager.getAppManager().finishActivity(activity);
    }

    /**
     * 删除指定的class
     *
     * @param cls cls
     */
    protected void finishActivity(Class<?> cls) {
        AppManager.getAppManager().finishActivity(cls);
    }

    /**
     * 获取playerId
     *
     * @param
     * @return
     */
    protected int getPlayerId() {
        return getIntent().getIntExtra(ContentUtils.INTENT.KEY_PLAYER_ID, ContentUtils.INTENT.DEFLAULT_VALUE);
    }

    /**
     * 显示一条toast
     *
     * @param msg
     */
    protected void showToastMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取解析的map集合
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getJsonObj(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        map = JSON.parseObject(jsonStr, new TypeReference<HashMap<String, Object>>() {
        });
        return map;
    }

    /**
     * 获取解析的对象
     *
     * @param jsonStr
     * @return
     */
    public static JSONObject getJson(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 给控件对象批量设置点击监听
     *
     * @param view
     */
    protected void setOnclickListener(View... view) {
        if (view != null && view.length != 0) {
            for (View v : view) {
                v.setOnClickListener(this);
            }
        }
    }

    /**
     * 根据控件id批量设置点击监听
     *
     * @param ids
     */
    protected void setOnclickListener(int... ids) {
        if (ids.length != 0) {
            for (int id : ids) {
                findViewById(id).setOnClickListener(this);
            }
        }
    }

    /**
     * 根据控件id批量设置点击监听(引用布局)
     *
     * @param ids
     */
    protected void setOnclickListener(View view, int[] ids) {
        if (ids.length != 0 && view != null) {
            for (int id : ids) {
                view.findViewById(id).setOnClickListener(this);
            }
        }
    }

    /**
     * obj转string
     *
     * @param obj
     * @return
     */
    protected String objToString(Object obj) {
        if (obj != null)
            return String.valueOf(obj);
        else
            return "";
    }

    /**
     * 注销操作
     */
    protected void logout() {
        //注销业务##############
        //注销腾讯云
        TxLogin.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                //注销失败
            }

            @Override
            public void onSuccess() {
                //注销成功
            }
        });
        //跳转页面
        goActivity(RegisterSelectActivity.class).finish();
    }

    /**
     * 显示并延时关闭dialog
     *
     * @param dialog 要关闭的dialog
     */
    protected void showThenDialog(final QMUITipDialog dialog) {
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (dialog != null) {

                    dialog.dismiss();
                }
            }
        }, 1000);
    }

    /**
     * 显示一个信息提示框,自行执行了show方法,内置监听自带dismiss方法
     *
     * @param msg            提示消息
     * @param msgDialogClick 点击回调监听
     * @return MessageDialogBuilder实例
     */
    protected QMUIDialog.MessageDialogBuilder showMsgDialog(String title, String msg, final MsgDialogClick msgDialogClick) {
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(context);
        messageDialogBuilder.setTitle(title)
                .setMessage(msg)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        msgDialogClick.doNo(dialog, index);
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        msgDialogClick.doYes(dialog, index);
                        dialog.dismiss();
                    }
                })
                .show();
        return messageDialogBuilder;
    }

    /**
     * 显示一个列表提示框,自行执行了show方法,内置监听自带dismiss方法
     *
     * @param items           列表选项名称
     * @param menuDialogClick 点击监听
     * @return MenuDialogBuilder实例
     */
    protected QMUIDialog.MenuDialogBuilder showMenuDialog(String[] items, final MenuDialogClick menuDialogClick) {
        QMUIDialog.MenuDialogBuilder menuDialogBuilder = new QMUIDialog.MenuDialogBuilder(context);
        menuDialogBuilder.addItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                menuDialogClick.OnMenuItemClick(dialog, which);
                dialog.dismiss();
            }
        })
                .show();
        return menuDialogBuilder;
    }

    /**
     * 显示一个中间弹出的自定义布局提示框
     *
     * @return dialog对象
     */
    protected Dialog showRadioDialog(int layoutRes, int[] ids) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        //调用接口方法
        setOnclickListener(inflate, ids);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();
        return dialog;
    }

    /**
     * 显示一个底部弹出的对话框(样式自定)
     *
     * @param layoutRes
     * @param ids       设置监听的对象id数组
     * @param yValue    距离底部的位置
     * @return dialog对象  不提供shwo()方法
     */
    protected Dialog showButtomDialog(int layoutRes, int[] ids, int yValue) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        //调用接口方法
        setOnclickListener(inflate, ids);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性

//        Display display = this.getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = yValue;//设置Dialog距离底部的距离
//        lp.width = width/2;
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    /**
     * 显示一个底部弹出的对话框(样式自定)(Dialog以外区域不模糊)
     *
     * @param layoutRes
     * @param ids       设置监听的对象id数组
     * @param yValue    距离底部的位置
     * @return dialog对象  不提供shwo()方法
     */
    protected Dialog showButtomDialogWhite(int layoutRes, int[] ids, int yValue) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyleWhite);
        //填充对话框的布局
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        //调用接口方法
        setOnclickListener(inflate, ids);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = yValue;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    ///////////////////////////////////////////总体设置方法///////////////////////////////////////////

    /**
     * 监听Back键按下事件
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
        /*if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isGoBack){
                showMsgDialog("退出", "是否确定退出" + getResources().getString(R.string.app_name) + "?", new MsgDialogClick() {
                    @Override
                    public void doYes(QMUIDialog dialog, int index) {
                        AppManager.getAppManager().appExit(context);
                    }
                    @Override
                    public void doNo(QMUIDialog dialog, int index) {
                    }
                });
            }else
                this.finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEventMainThread(BaseEvent var1) {

    }

    protected void showLoadingDialog(String msg) {

        if (tipD != null) {
            tipD.dismiss();
        }
        if (isFinishing()) {
            return;
        }
        tipD = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        tipD.show();
    }

    protected void hideLoadingDialog() {
        if (tipD != null) {
            tipD.dismiss();
        }
    }
}
