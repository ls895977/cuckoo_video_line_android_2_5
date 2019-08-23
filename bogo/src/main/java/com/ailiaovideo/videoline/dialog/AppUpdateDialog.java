package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.drawable.BGDrawable;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.ailiaovideo.videoline.utils.BGViewUtil;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;

/**
 * Created by 魏鹏 on 2018/3/30.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class AppUpdateDialog extends BGDialogBase implements View.OnClickListener {

    private TextView mTvDes;
    private TextView mTvLeft,mTvRight;


    public AppUpdateDialog(@NonNull Context context) {
        super(context);
        
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_app_update);
        setCanceledOnTouchOutside(false);
        BGViewUtil.setBackgroundDrawable(getContentView(), new BGDrawable().color(Color.WHITE).cornerAll(10));

        setHeight(ConvertUtils.dp2px(160));
        padding(50,0,50,0);

        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {

        mTvDes = findViewById(R.id.tv_des);
        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);
        mTvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);

        String des = ConfigModel.getInitData().getAndroid_app_update_des();
        if(des != null){
            mTvDes.setText(des);
        }else{
            mTvDes.setText("发现新的版本,请升级!");
        }

        //是否强制升级
        if(ConfigModel.getInitData().getIs_force_upgrade() == 1){

            mTvLeft.setVisibility(View.GONE);
        }

    }


    public static AppUpdateDialog checkUpdate(Context context){

        String version = ConfigModel.getInitData().getAndroid_version();

        //是否有新版本
        if(StringUtils.toInt(version) > StringUtils.toInt(AppUtils.getAppVersionCode())){

            AppUpdateDialog dialog = new AppUpdateDialog(context);
            //是否强制升级
            if(ConfigModel.getInitData().getIs_force_upgrade() == 1){

                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }

            dialog.show();

            return dialog;
        }

        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_left:

                dismiss();
                break;
            case R.id.tv_right:

                clickUpdate();
                break;
            default:

                break;
        }
    }

    private void clickUpdate() {

        String jumpUrl = ConfigModel.getInitData().getAndroid_download_url();
        if(jumpUrl == null || TextUtils.isEmpty(jumpUrl)){
            ToastUtils.showLong("下载地址为空!");
            return;
        }

        Utils.openWeb(getContext(),jumpUrl);
    }
}
