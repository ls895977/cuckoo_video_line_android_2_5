package com.ailiaovideo.videoline.inter;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import okhttp3.Call;
import okhttp3.Response;

/**
 * json返回接口类重写
 * Created by jiahengfei on 2018/1/19 0019.
 */

public abstract class JsonCallback extends StringCallback{
    private QMUITipDialog jsonTipDialog;
    private String msg = returnMsg();

    /**
     * 获取当前context
     * @return 返回一个context
     */
    public abstract Context getContextToJson();

    /**
     * 返回msg
     */
    public String returnMsg(){
        return null;
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //请求开始--UI线程
        /*jsonTipDialog = new QMUITipDialog.Builder(getContextToJson())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        jsonTipDialog.show();*/
    }

    @Override
    public void onSuccess(String s, Call call, Response response) {
        //请求成功--UI线程
        Log.i("json",msg+"JSON::::::::::::::::>\n"+s);
        //jsonTipDialog.dismiss();
    }

    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        //请求服务器失败--UI线程
        //jsonTipDialog.dismiss();
        Log.d("tag","服务器请求失败!####"+msg+"失败####"+getContextToJson().getClass().getName());
    }
}
