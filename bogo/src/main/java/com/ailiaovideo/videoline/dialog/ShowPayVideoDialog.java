package com.ailiaovideo.videoline.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.ApiConstantDefine;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.drawable.BGDrawable;
import com.ailiaovideo.videoline.event.CuckooBuyVideoCommonEvent;
import com.ailiaovideo.videoline.json.JsonRequestDoBuyVideo;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.RechargeActivity;
import com.ailiaovideo.videoline.ui.VideoPlayerActivity;
import com.ailiaovideo.videoline.utils.BGViewUtil;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by weipeng on 2018/2/27.
 */

public class ShowPayVideoDialog extends BGDialogBase implements View.OnClickListener {

    private TextView mTvContent;
    private TextView mTvRight;
    private TextView mTvLeft;
    private UserModel userModel;

    private VideoModel videoData;
    private DialogClickCallback dialogClickCallback;

    public ShowPayVideoDialog(@NonNull Context context,VideoModel videoData) {
        super(context,R.style.dialogBlackBg);

        this.videoData = videoData;
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

    public void setDialogClickCallback(DialogClickCallback dialogClickCallback) {
        this.dialogClickCallback = dialogClickCallback;
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

        mTvContent.setText("查看需要消耗" + videoData.getCoin() + "币,确认支付？");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_right:
                if(dialogClickCallback != null){
                    dialogClickCallback.onClickRight();
                }
                requestPayVideo();
                break;
            case R.id.tv_left:
                if(dialogClickCallback != null){
                    dialogClickCallback.onClickLeft();
                }
                dismiss();
                break;
        }
    }

    //购买视频
    private void requestPayVideo() {

        final QMUITipDialog tipD = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("")
                .create();
        tipD.show();

        Api.doRequestBuyVideo(userModel.getId(),userModel.getToken(),videoData.getId(),new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {

                tipD.dismiss();
                JsonRequestDoBuyVideo jsonObj = (JsonRequestDoBuyVideo) JsonRequestDoBuyVideo.getJsonObj(s,JsonRequestDoBuyVideo.class);
                if(jsonObj.getCode() == 1){
                    CuckooBuyVideoCommonEvent event = new CuckooBuyVideoCommonEvent();
                    event.setVideoId(videoData.getId());
                    EventBus.getDefault().post(event);
                    //VideoPlayerActivity.startVideoPlayerActivity(getContext(),videoData);
                    dismiss();
                }else if(jsonObj.getCode() == ApiConstantDefine.ApiCode.BALANCE_NOT_ENOUGH){

                    RechargeActivity.startRechargeActivity(getContext());
                    ToastUtils.showLong(jsonObj.getMsg());
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

    public interface DialogClickCallback{
        void onClickLeft();
        void onClickRight();
    }
}
