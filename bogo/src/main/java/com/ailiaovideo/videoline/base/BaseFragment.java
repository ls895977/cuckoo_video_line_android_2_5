package com.ailiaovideo.videoline.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.helper.ContentUtils;
import com.ailiaovideo.videoline.inter.MsgDialogClick;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.ui.HomePageActivity;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 基础的Fragment类
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener
        ,BaseQuickAdapter.RequestLoadMoreListener {
    private View view;

    //数据
    protected String uId;//id
    protected String uToken;//token
    protected String currency;//全局货币信息

    protected QMUITipDialog tipD;//声明一个QMUITipDialog对象

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;
    private boolean isLoadMore;

    //////////////////////////////////////////抽象方法///////////////////////////////////////////////
    /**
     * 返回当前引入的根布局
     * @return View
     * @param inflater
     * @param container
     */
    protected abstract View getBaseView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化控件
     * @param view 根布局
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     * @param view 根布局
     */
    protected abstract void initDate(View view);

    /**
     * 初始化设置
     * @param view 根布局
     */
    protected abstract void initSet(View view);

    /**
     * 初始化显示数据/操作
     * @param view 根布局
     */
    protected abstract void initDisplayData(View view);

    ////////////////////////////////////////初始化策略&&生命周期管理///////////////////////////////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = getBaseView(inflater,container);
        //初始化静态数据
        initStaticData();
        init(view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public void fetchData(){

    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    /**
     * 初始化操作
     * @param view
     */
    protected void init(View view){
        ButterKnife.bind(this,view);
        initView(view);
        initDate(view);
        initSet(view);
    }

    protected void requestGetData(boolean isCache){

    }

    /**
     * 初始化静态数据
     */
    protected void initStaticData(){
        //用户id和token
        uId = SaveData.getInstance().getId();
        uToken = SaveData.getInstance().getToken();
        //常量
        currency = RequestConfig.getConfigObj().getCurrency();
    }

    @Override
    public void onStart() {
        initDisplayData(view);
        super.onStart();

        if(isRegEvent()){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRegEvent()){
            EventBus.getDefault().unregister(this);
        }
    }

    protected boolean isRegEvent(){
        return false;
    }

    ///////////////////////////////////////////工具方法///////////////////////////////////////////////
    /**
     * 获取解析的map集合
     * @param jsonStr
     * @return
     */
    public static Map<String,Object> getJsonObj(String jsonStr){
        Map<String,Object> map = new HashMap<>();
        map = JSON.parseObject(jsonStr,new TypeReference<HashMap<String,Object>>(){});
        return map;
    }

    /**
     * 获取解析的map集合
     * @param jsonStr
     * @return
     */
    public static JSONObject getJson(String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject;
    }
    /**
     * 批量更改控件显示状态
     * @param view
     */
    protected void showOrConceal(View... view){
        for (View v:view) {
            v.setVisibility(v.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示控件
     * @param view
     * @return
     */
    protected View showView(View view){
        view.setVisibility(View.VISIBLE);
        return view;
    }

    /**
     * 隐藏控件
     * @param view
     * @return
     */
    protected View concealView(View view){
        view.setVisibility(View.GONE);
        return view;
    }

    /**
     * Activity跳转方法
     * @param cls
     * @return 返回Activity对象
     */
    protected Activity goActivity(Context context,Class cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
        return (Activity)context;
    }

    /**
     * Activity跳转方法
     * @param cls 目标位置
     * @param str 字符串参数
     * @return
     */
    protected Activity goActivity(Context context,Class cls,String str){
        Intent intent = new Intent(context,cls);
        intent.putExtra("str",str);
        startActivity(intent);
        return (Activity)context;
    }

    /**
     * Activity跳转方法(跳转到用户主页)
     * @param id 目标用户id
     * @return 返回Activity对象
     */
    protected Activity goActivityMain(Context context,String id) {
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        return (Activity)context;
    }

        /**
         * Activity跳转方法
         * @param cls 目标位置
         * @param inter 整型参数
         * @return
         */
    protected Activity goActivity(Context context,Class cls,int inter){
        Intent intent = new Intent(context,cls);
        intent.putExtra("inter",inter);
        startActivity(intent);
        return (Activity)context;
    }

    /**
     * Activity跳转方法
     * @param cls 目标位置
     * @param obj 序列化之后的对象
     * @return 获取:Object obj = getIntent().getParcelableExtra("obj");
     */
    protected Activity goActivity(Context context,Class cls,Object obj){
        Intent intent = new Intent(context,cls);
        intent.putExtra("obj",(Parcelable)obj);
        startActivity(intent);
        return (Activity)context;
    }

    /**
     * log
     * @param msg
     */
    protected void log(String msg){
        Log.d("tag",msg);
    }

    /**
     * 显示一条toast
     * @param msg
     */
    protected  void showToastMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 给控件对象批量设置点击监听
     * @param view
     */
    protected void setOnclickListener(View... view){
        if (view != null && view.length != 0){
            for (View v:view) {
                v.setOnClickListener(this);
            }
        }
    }

    /**
     * 根据控件id批量设置点击监听(引用布局)
     * @param ids
     */
    protected void setOnclickListener(View view,int... ids){
        if (ids.length != 0 && view != null){
            for (int id:ids) {
                view.findViewById(id).setOnClickListener(this);
            }
        }
    }

    /**
     * 显示并延时关闭dialog
     * @param dialog 要关闭的dialog
     */
    protected void showThenDialog(final QMUITipDialog dialog){
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1000);
    }

    /**
     * 显示一个信息提示框,自行执行了show方法,内置监听自带dismiss方法
     * @param msg 提示消息
     * @param msgDialogClick 点击回调监听
     * @return MessageDialogBuilder实例
     */
    protected QMUIDialog.MessageDialogBuilder showMsgDialog(Context context,String title, String msg, final MsgDialogClick msgDialogClick){
        QMUIDialog.MessageDialogBuilder messageDialogBuilder =  new QMUIDialog.MessageDialogBuilder(context);
        messageDialogBuilder .setTitle(title)
                .setMessage(msg)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        msgDialogClick.doNo(dialog,index);
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        msgDialogClick.doYes(dialog,index);
                        dialog.dismiss();
                    }
                })
                .show();
        return messageDialogBuilder;
    }

    /**
     * 显示一个中间弹出的自定义布局提示框
     * @return dialog对象
     */
    protected Dialog showViewDialog(Context context,int layoutRes, int[] ids){
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(context).inflate(layoutRes, null);
        //调用接口方法
        setOnclickListener(inflate,ids);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity( Gravity.CENTER);
        dialog.show();
        return dialog;
    }

    //////////////////////////////////////////监听事件处理////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        //点击监听
    }

    @Override
    public void onRefresh() {
        //上拉加载
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(getContext());
    }


    protected void showLoadingDialog(String msg){

        if(tipD != null){
            tipD.dismiss();
        }
        tipD = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create();
        tipD.show();
    }

    protected void hideLoadingDialog(){
        if(tipD != null){
            tipD.dismiss();
        }
    }
}
