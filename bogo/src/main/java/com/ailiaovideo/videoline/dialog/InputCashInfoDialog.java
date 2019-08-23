package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.drawable.BGDrawable;
import com.ailiaovideo.videoline.event.CuckooCashEvent;
import com.ailiaovideo.videoline.json.JsonRequestDoCashMoney;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.utils.BGViewUtil;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 魏鹏 on 2018/5/11.
 * email:1403102936@qq.com
 * 山东布谷鸟网络科技有限公司著
 */

public class InputCashInfoDialog extends BGDialogBase implements View.OnClickListener {

    private TextView tv_left;
    private TextView tv_right;

    private EditText mEtName;
    private EditText mEtNumber;
    
    public InputCashInfoDialog(@NonNull Context context) {
        super(context);
        
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_cash_info);
        BGViewUtil.setBackgroundDrawable(getContentView(), new BGDrawable().color(Color.WHITE).cornerAll(30));
        setHeight(ConvertUtils.dp2px(200));
        padding(50,0,50,0);

        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {

        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);
        mEtName = findViewById(R.id.et_name);
        mEtNumber = findViewById(R.id.et_number);

        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:

                dismiss();
                break;
            case R.id.tv_right:

                clickCash();
                break;

            default:

                break;
        }
    }

    //提现
    private void clickCash() {

        String name = mEtName.getText().toString();
        String number = mEtNumber.getText().toString();

        if(TextUtils.isEmpty(name)){
            ToastUtils.showLong("姓名不能为空");
            return;
        }

        if(TextUtils.isEmpty(number)){
            ToastUtils.showLong("收款账号不能为空");
            return;
        }

        showLoadingDialog("正在提交...");
        Api.doRequestCash(SaveData.getInstance().getId(),SaveData.getInstance().getToken(),name,number,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestDoCashMoney jsonObj  = (JsonRequestDoCashMoney) JsonRequestDoCashMoney.getJsonObj(s,JsonRequestDoCashMoney.class);
                if(jsonObj.getCode() == 1){
                    ToastUtils.showLong("提现成功,等待管理人员审核!");
                    CuckooCashEvent event = new CuckooCashEvent();
                    EventBus.getDefault().post(event);
                }else{

                    ToastUtils.showLong(jsonObj.getMsg());
                }
                dismiss();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                hideLoadingDialog();
                dismiss();
            }
        });
    }
}
