package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestPerfectRegisterInfo;
import com.ailiaovideo.videoline.modle.UserModel;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class PerfectRegisterInfoActivity extends BaseActivity {

    private CircleImageView mIvUserHear;
    private EditText mEtUserNickname;
    private CheckBox mCbMan, mCbFemale;
    private Button mBtnSubmit;

    private List<LocalMedia> localMediaList;
    private UserModel userModel;

    public static final String USER_LOGIN_INFO = "USER_LOGIN_INFO";

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_perfect_register_info;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mIvUserHear = findViewById(R.id.iv_avatar);
        mEtUserNickname = findViewById(R.id.et_name);
        mCbMan = findViewById(R.id.cb_man);
        mCbFemale = findViewById(R.id.cb_female);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mIvUserHear.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mCbMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mCbFemale.setChecked(false);
                }
            }
        });

        mCbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mCbMan.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {
        userModel = getIntent().getParcelableExtra(USER_LOGIN_INFO);
    }

    @Override
    protected void initPlayerDisplayData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_avatar:

                selectPhoto();
                break;
            case R.id.btn_submit:

                submitInfo();
                break;

            default:
                break;
        }
    }

    private void submitInfo() {
        String userNickname = mEtUserNickname.getText().toString();
        int sex = 0;
        if (mCbMan.isChecked()) {
            sex = 1;
        } else if (mCbFemale.isChecked()) {
            sex = 2;
        }

        if (TextUtils.isEmpty(userNickname)) {
            showToastMsg(getString(R.string.reg_nickname_not_empty));
            return;
        }

        if (sex == 0) {
            showToastMsg(getString(R.string.reg_plase_select_sex));
            return;
        }

//        if (localMediaList == null || localMediaList.size() == 0) {
//            showToastMsg(getString(R.string.reg_plase_upload_headimg));
//            return;
//        }

//        File avatar = new File(localMediaList.get(0).getPath());
//        if (!avatar.exists()) {
//            showToastMsg(getString(R.string.file_not_fount));
//            return;
//        }

        if (userModel == null) {
            return;
        }

        showLoadingDialog(getString(R.string.loading_upload_data));
        Api.doRequestPerfectInfo(userModel.getId(), userModel.getToken(), userNickname, sex, null, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {

                hideLoadingDialog();
                JsonRequestPerfectRegisterInfo jsonObj = (JsonRequestPerfectRegisterInfo) JsonRequestPerfectRegisterInfo.getJsonObj(s, JsonRequestPerfectRegisterInfo.class);
                if (jsonObj.getCode() == 1) {
                    userModel.setUser_nickname(jsonObj.getData().getUser_nickname());
                    userModel.setAvatar(jsonObj.getData().getAvatar());
                    userModel.setSex(mCbMan.isChecked() ? 1 : 2);

//                    if (mCbMan.isChecked()) {
//                        //关注
//                        Intent intent = new Intent(PerfectRegisterInfoActivity.this, RecommendActivity.class);
//                        intent.putExtra("userModel", userModel);
//                        startActivity(intent);
//                    } else {
//                        LoginUtils.doLogin(getNowContext(), userModel);
//                    }

                    LoginUtils.doLogin(getNowContext(), userModel);

                } else {
                    showToastMsg(jsonObj.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
                showToastMsg(getString(R.string.upload_data_error));
            }
        });

    }

    /**
     * 选择相册图片
     */
    private void selectPhoto() {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .forResult(PictureConfig.CHOOSE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    localMediaList = PictureSelector.obtainMultipleResult(data);

                    if (localMediaList.size() > 0) {
                        mIvUserHear.setImageBitmap(BitmapFactory.decodeFile(localMediaList.get(0).getPath()));
                    }
                    break;
                default:
                    break;
            }
        }
    }


}
