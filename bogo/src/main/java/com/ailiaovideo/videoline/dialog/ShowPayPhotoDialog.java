package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.drawable.BGDrawable;
import com.ailiaovideo.videoline.json.JsonRequestDoBuyPhoto;
import com.ailiaovideo.videoline.manage.RequestConfig;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.PerViewImgActivity;
import com.ailiaovideo.videoline.utils.BGViewUtil;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by weipeng on 2018/2/28.
 */

public class ShowPayPhotoDialog extends BGDialogBase implements View.OnClickListener {
    private TextView mTvContent;
    private TextView mTvRight;
    private TextView mTvLeft;
    private UserModel userModel;

    private String imgId;

    public ShowPayPhotoDialog(@NonNull Context context,String imgId) {
        super(context, R.style.dialogBlackBg);

        this.imgId = imgId;
        init();
    }

    private void init() {

        setContentView(R.layout.dialog_message);
        BGViewUtil.setBackgroundDrawable(getContentView(), new BGDrawable().color(Color.WHITE).cornerAll(30));
        setHeight(ConvertUtils.dp2px(150));
        padding(50,0,50,0);

        initView();
        initData();
    }

    private void initData() {
        userModel = SaveData.getInstance().getUserInfo();
    }

    private void initView() {
        mTvContent = findViewById(R.id.tv_content);
        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);

        mTvContent.setOnClickListener(this);
        mTvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);


        mTvContent.setText("查看需要消耗" + RequestConfig.getConfigObj().getPrivatePhotosCoin() + "币,确认支付？");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_right:
                requestPayVideo();
                break;
            case R.id.tv_left:

                dismiss();
                break;
        }
    }

    //购买图片
    private void requestPayVideo() {

        final QMUITipDialog tipD = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("")
                .create();
        tipD.show();

        Api.doRequestBuyPhoto(userModel.getId(),userModel.getToken(),imgId,new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                tipD.dismiss();
                JsonRequestDoBuyPhoto jsonObj = (JsonRequestDoBuyPhoto) JsonRequestDoBuyPhoto.getJsonObj(s,JsonRequestDoBuyPhoto.class);
                if(jsonObj.getCode() == 1){

                    PerViewImgActivity.startPerViewImg(getContext(),jsonObj.getImg());
                    dismiss();
                }else{
                    ToastUtils.showLong(jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                tipD.dismiss();
            }
        });
    }
}
