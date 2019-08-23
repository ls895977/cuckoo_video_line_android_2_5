package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.json.JsonRequest;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestDoGetVideoEndInfo;
import com.ailiaovideo.videoline.utils.Utils;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class VideoLineEndActivity extends BaseActivity {

    @BindView(R.id.tv_evaluate)
    TextView tv_evaluate;

    @BindView(R.id.tv_gift_name)
    TextView tv_gift_name;

    @BindView(R.id.tv_video_name)
    TextView tv_video_name;

    @BindView(R.id.tv_video_count)
    TextView tv_video_count;

    @BindView(R.id.tv_gift_count)
    TextView tv_gift_count;

    private ImageView mIvBg;
    private CircleImageView mIvHead;
    private TextView mTvName;//昵称
    private TextView mTvFollow;//关注
    private TextView mTvConsumption;//消费
    private TextView mTvLongTime;//时长
    private TextView mTvZan;//点赞
    private TextView mTvBack;//返回
    private TextView mTvTypeName;//收益或消费

    public static final String USER_NICKNAME = "USER_NICKNAME";
    public static final String USER_HEAD = "USER_HEAD";
    public static final String USER_ID = "USER_ID";

    public static final String LIVE_CHANNEL_ID = "LIVE_CHANNEL_ID";
    public static final String LIVE_LINE_TIME = "LIVE_LINE_TIME";
    public static final String IS_CALL_BE_USER = "IS_CALL_BE_USER";
    public static final String IS_FABULOUS = "IS_FABULOUS";

    private boolean isCallBeUser;//是否是被拨打用户
    private int isFabulous;//是否点过赞

    private String userAvatar;
    private String userId;
    private String channelId;


    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_video_line_end;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mIvBg = findViewById(R.id.iv_bg);
        mIvHead = findViewById(R.id.iv_head);
        mTvName = findViewById(R.id.tv_name);
        mTvFollow = findViewById(R.id.tv_follow);
        mTvConsumption = findViewById(R.id.tv_coin);
        mTvLongTime = findViewById(R.id.tv_long_time);
        mTvZan = findViewById(R.id.tv_zan);
        mTvBack = findViewById(R.id.tv_back);
        mTvTypeName = findViewById(R.id.tv_type_name);
        findViewById(R.id.tv_evaluate).setOnClickListener(this);

        mTvBack.setOnClickListener(this);
        mTvZan.setOnClickListener(this);
        mTvFollow.setOnClickListener(this);
    }

    @Override
    protected void initSet() {

        if (isCallBeUser) {
            mTvZan.setVisibility(View.GONE);
            mTvFollow.setVisibility(View.GONE);
            tv_gift_name.setText("礼物收益");
            tv_video_name.setText("通话收益");
            mTvTypeName.setText("总收益");
        } else {
            tv_evaluate.setVisibility(View.VISIBLE);
            tv_gift_name.setText("礼物消费");
            tv_video_name.setText("通话消费");
            mTvTypeName.setText("总消费");
        }
    }

    @Override
    protected void initData() {

        isCallBeUser = getIntent().getBooleanExtra(IS_CALL_BE_USER, false);
        userAvatar = getIntent().getStringExtra(USER_HEAD);
        userId = getIntent().getStringExtra(USER_ID);
        isFabulous = getIntent().getIntExtra(IS_FABULOUS, 1);
        channelId = getIntent().getStringExtra(LIVE_CHANNEL_ID);

        mTvName.setText(getIntent().getStringExtra(USER_NICKNAME));
        //mTvConsumption.setText(getIntent().getStringExtra(LIVE_LINE_CONSUMPTION));
        mTvLongTime.setText(getIntent().getStringExtra(LIVE_LINE_TIME));

        if (isFabulous == 0) {
            mTvZan.setVisibility(View.VISIBLE);
        }
        mTvZan.setVisibility(View.VISIBLE);

        if (userAvatar != null) {

            Glide.with(this)
                    .load(Utils.getCompleteImgUrl(userAvatar))
                    .apply(bitmapTransform(new BlurTransformation(25)).dontAnimate())
                    .into(mIvBg);

            Utils.loadHttpImg(this, userAvatar, mIvHead);
        }

        requestGetVideoEndInfo();

    }

    private void requestGetVideoEndInfo() {

        Api.doReuqestGetVideoEndInfo(uId, uToken, channelId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestDoGetVideoEndInfo jsonObj =
                        (JsonRequestDoGetVideoEndInfo) JsonRequestDoGetVideoEndInfo.getJsonObj(s, JsonRequestDoGetVideoEndInfo.class);
                if (jsonObj.getCode() == 1) {
                    mTvConsumption.setText(jsonObj.getTotal_count());
                    tv_gift_count.setText(jsonObj.getGift_count());
                    tv_video_count.setText(jsonObj.getVideo_count());
                    if (StringUtils.toInt(jsonObj.getIs_follow()) == 1) {
                        mTvFollow.setVisibility(View.GONE);
                    } else {
                        mTvFollow.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    /*
     * 点赞
     * */
    private void requestFabulous() {
        Api.doFabulousUser(uId, uToken, channelId, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestBase jsonObj = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonObj.getCode() == 1) {
                    showToastMsg(getString(R.string.zan_success));
                }
            }
        });
    }

    /*
     * 关注
     * */
    private void requestFollow() {
        Api.doLoveTheUser(
                userId,
                uId,
                uToken,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonRequest requestObj = JsonRequest.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            mTvFollow.setVisibility(View.GONE);
                            showToastMsg(getString(R.string.follow_success));
                        } else {
                            ToastUtils.showLong(requestObj.getMsg());
                        }
                    }
                }
        );
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    private void clickEvaluate() {

        Intent intent = new Intent(this, CuckooVideoEndEvaluateActivity.class);
        intent.putExtra(CuckooVideoEndEvaluateActivity.CHANNEL_ID, channelId);
        intent.putExtra(CuckooVideoEndEvaluateActivity.TO_USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_zan:
                mTvZan.setVisibility(View.GONE);
                requestFabulous();
                break;
            case R.id.tv_follow:
                requestFollow();
                break;
            case R.id.tv_evaluate:
                clickEvaluate();
                break;

            default:
                break;
        }
    }

}
