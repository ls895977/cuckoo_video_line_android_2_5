package com.ailiaovideo.videoline.wxpay;

import android.app.Activity;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.modle.ConfigModel;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/14.
 */
public class WChatPayService {
    IWXAPI msgApi;

    private Activity context;

    public WChatPayService(Activity context) {
        this.context = context;
        // 将该app注册到微信
        msgApi = WXAPIFactory.createWXAPI(context,null);
        msgApi.registerApp(ConfigModel.getInitData().getWx_appid());
    }


    public void callWxPay(JSONObject signInfo) {

        PayReq req = new PayReq();
        req.appId        = signInfo.getString("appid");
        req.partnerId    = signInfo.getString("partnerid");
        req.prepayId     = signInfo.getString("prepayid");//预支付会话ID
        req.packageValue = "Sign=WXPay";
        req.nonceStr     = signInfo.getString("noncestr");
        req.timeStamp    = signInfo.getString("timestamp");
        req.sign         = signInfo.getString("sign");
        if(msgApi.sendReq(req)){
            ToastUtils.showLong("微信支付");
        }else{
            ToastUtils.showLong("请查看您是否安装微信");
        }

    }
}
