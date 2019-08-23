package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.api.ApiUtils;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.helper.ImageUtil;
import com.ailiaovideo.videoline.inter.JsonCallback;
import com.ailiaovideo.videoline.inter.MenuDialogClick;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.json.JsonRequestUser;
import com.ailiaovideo.videoline.json.jsonmodle.UserData;
import com.ailiaovideo.videoline.modle.UserImgModel;
import com.ailiaovideo.videoline.rests.adapter.RedactAdapter;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.ailiaovideo.videoline.utils.Utils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 编辑资料页面
 */
public class EditActivity extends BaseActivity {
    private QMUITopBar topBar;
    private RecyclerView redactRecycler;
    private CircleImageView headImg;
    private TextView redactNameText, redactSexText;
    private GridLayoutManager gridLayoutManager;
    private RedactAdapter redactAdapter;
    private RelativeLayout mRlChangeNameLayout;

    private File headImgFile;//头像列表
    private ArrayList<File> filesByAll = new ArrayList<>();//要上传的图片
    private ArrayList<UserImgModel> imgLoader = new ArrayList<>();//网络图片视图列表
    private int sex = 0;//性别

    //判断信息
    private boolean isAlterSex = false;//是否可以更改性别
    private boolean isAddPlus = false;//是否存在+号
    private boolean isAlterAll = false;//是否修改了信息
    private boolean isAddPlusAll = true;//是否添加"+"

    private ArrayList<String> path = new ArrayList<>();//记录选中的图片路径
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;//标记

    private int selectImageType = 0;
    private TextView signTv;
    private RelativeLayout signRl;
    private String sign;

    @Override
    protected Context getNowContext() {
        return EditActivity.this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_player_redact;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        topBar = findViewById(R.id.redact_topbar);
        redactRecycler = findViewById(R.id.redact_recycler);
        headImg = findViewById(R.id.head_img);
        redactNameText = findViewById(R.id.redact_nameText);
        redactSexText = findViewById(R.id.redact_sextext);
        mRlChangeNameLayout = findViewById(R.id.redact_name);
        gridLayoutManager = new GridLayoutManager(getNowContext(), 3);

        //个性签名
        signTv = findViewById(R.id.redact_sign_tv);
        signRl = findViewById(R.id.redact_sign);
        signRl.setOnClickListener(this);

        initAdapter();
    }

    @Override
    protected void initSet() {
        topBar.setBackgroundResource(R.color.white);
        topBar.setTitle(getString(R.string.edit_info)).setTextColor(getNowContext().getResources().getColor(R.color.black));
        Button button = topBar.addRightTextButton(getString(R.string.save), R.id.redact_savebtn);
        button.setTextColor(getNowContext().getResources().getColor(R.color.black));
        button.setOnClickListener(this);
        topBar.addLeftImageButton(R.drawable.icon_back_black, R.id.redact_backbtn).setOnClickListener(this);
        setOnclickListener(R.id.head_img, R.id.redact_name, R.id.redact_sex);
    }

    @Override
    protected void initData() {
        //初始化数据
        requestUserData(uId, uToken);
    }

    @Override
    protected void initPlayerDisplayData() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.redact_backbtn:
                finish();
                break;
            case R.id.redact_savebtn:
                sign = signTv.getText().toString().trim();
                saveUserData();
                break;
            case R.id.head_img:
                doChangeHead();
                break;
            case R.id.redact_name:
                showDialogEdNicknameText();
                break;
            case R.id.redact_sex:
                showDialogSex();
                break;
            case R.id.redact_sign:
                showDialogSignEdText();
                break;
            default:
                break;
        }
    }

    //////////////////////////////////////业务处理//////////////////////////////////////////////////

    /**
     * 请求用户资料
     *
     * @param uId
     * @param uToken
     * @return
     */
    private void requestUserData(String uId, String uToken) {
        Api.getUserDataAtCompile(uId, uToken, new JsonCallback() {
            @Override
            public Context getContextToJson() {
                return getNowContext();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {

                JsonRequestUser userJ = JsonRequestUser.getJsonObj(s);
                if (userJ.getCode() == 1) {
                    UserData userData = userJ.getData();
                    log(userData.toString());
                    //显示数据
                    if (ApiUtils.isTrueUrl(userData.getAvatar())) {
                        Utils.loadHttpImg(EditActivity.this, Utils.getCompleteImgUrl(userData.getAvatar()), headImg);
                    }
                    redactNameText.setText(userData.getUser_nickname());
                    if (userData.getSex() == 0) {
                        isAlterSex = true;
                    }
                    if (!TextUtils.isEmpty(userData.getSign())) {
                        signTv.setText(userData.getSign() + "");
                    } else {
                        signTv.setText("未知");
                    }

                    redactSexText.setText(Utils.getSex(userData.getSex()));
                    //保存数据
                    sex = userData.getSex();
                    imgLoader.clear();
                    imgLoader.addAll(userData.getImg());

                    if (ApiUtils.isTrueUrl(userData.getAvatar())) {
                        ApiUtils.getUrlBitmapToSD(userData.getAvatar(), "headImage");
                    }
                    if (imgLoader.size() == 0) {
                        isAddPlusAll = false;
                    }
                    isAddPlus = false;

                    if (StringUtils.toInt(userData.getIs_change_name()) != 1) {
                        mRlChangeNameLayout.setEnabled(false);
                    }
                    //网络刷新&&请求图片
                    refreshAdapter();
                } else {
                    showToastMsg(userJ.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                finishNow();//数据请求异常,退出当前页面
            }
        });
    }


    /**
     * 重置适配器
     */
    private void initAdapter() {

        //重置适配器
        redactAdapter = new RedactAdapter(this, imgLoader);
        //适配器监听
        redactAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

                if (position == 0) {
                    //添加操作
                    doSelectImage(1);
                } else {
                    //图片操作选项
                    showMenuDialog(new String[]{getString(R.string.show_big_img), getString(R.string.del_img)}, new MenuDialogClick() {
                        @Override
                        public void OnMenuItemClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                //显示大图
                                PerViewImgActivity.startPerViewImg(EditActivity.this, imgLoader.get(position).getImg());
                            } else {
                                doDelNowImg(position);
                            }
                        }
                    });
                }
            }
        });
        redactRecycler.setLayoutManager(gridLayoutManager);
        redactRecycler.setAdapter(redactAdapter);

    }

    private void refreshAdapter() {

        ArrayList<UserImgModel> userImgModels = new ArrayList<>();
        //添加"+"按钮
        if (!isAddPlus) {
            isAddPlus = true;
            UserImgModel userImgModel = new UserImgModel();
            userImgModel.setId("0");
            userImgModel.setIsLocal(1);
            userImgModels.add(userImgModel);
        }
        userImgModels.addAll(imgLoader);
        imgLoader.clear();
        imgLoader.addAll(userImgModels);
        redactAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                    if (selectImageType == 0) {

                        headImgFile = new File(selectList.get(0).getPath());
                        headImg.setImageBitmap(ImageUtil.getBitmapByPath(headImgFile.getAbsolutePath()));
                    } else if (selectImageType == 1) {
                        //选择成功
                        path.clear();
                        //imgLoader.clear();
                        filesByAll.clear();
                        for (LocalMedia item : selectList) {
                            path.add(item.getPath());//选择的文件路径
                            filesByAll.add(new File(item.getPath()));
                        }
                        for (File urlF : filesByAll) {
                            String urlS = urlF.getPath();
                            UserImgModel userImgModel = new UserImgModel();
                            userImgModel.setImg(urlS);
                            userImgModel.setIsLocal(1);
                            userImgModel.setId("-1");
                            imgLoader.add(userImgModel);//设置image显示网络列表
                        }
                        refreshAdapter();
                    }
                    break;
            }
        }
    }


    /**
     * 删除当前图片
     *
     * @param position 索引
     */
    private void doDelNowImg(int position) {

        //删除时执行显示删除,不真正从服务器端进行删除操作
        Api.delectUserImage(
                uId,
                uToken,
                imgLoader.get(position).getId(),
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JsonRequestUser requestObj = JsonRequestUser.getJsonObj(s);
                        if (requestObj.getCode() == 1) {
                            requestUserData(uId, uToken);
                        } else {
                            showToastMsg(getString(R.string.del_error));
                        }
                    }
                }
        );
    }

    /**
     * 提交保存修改信息
     */
    private void saveUserData() {

        showLoadingDialog(getString(R.string.loading_upload_data));
        Api.saveUserDataAtCompile(
                uId,
                uToken,
                redactNameText.getText().toString(),
                headImgFile,
                sex,
                filesByAll,
                sign,
                new JsonCallback() {
                    @Override
                    public Context getContextToJson() {
                        return getNowContext();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        hideLoadingDialog();
                        tipD = new QMUITipDialog.Builder(getNowContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord(getString(R.string.submit_success))
                                .create();
                        showThenDialog(tipD);
                        //重新请求数据
                        requestUserData(uId, uToken);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        hideLoadingDialog();
                    }
                });
    }

    private void editUserNickname(String nickName) {

        showLoadingDialog("正在提交...");
        Api.editUserNickname(uId, uToken, nickName, new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                hideLoadingDialog();
                JsonRequestBase jsonRequestBase = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                if (jsonRequestBase.getCode() == 1) {
                    ToastUtils.showLong("提交成功，等待管理员审核！");
                } else {
                    ToastUtils.showLong(jsonRequestBase.getMsg());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                hideLoadingDialog();
            }
        });
    }


    /**
     * 执行选择图片添加操作
     */
    private void doSelectImage(int type) {
        selectImageType = type;

        if (selectImageType == 0) {

            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .enableCrop(true)// 是否裁剪 true or false
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        } else {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(5)
                    .enableCrop(true)// 是否裁剪 true or false
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }

    /**
     * 换头像
     */
    private void doChangeHead() {
        if (isChangeHead()) {
            doSelectImage(0);
        } else {
            showDialogMsg(getString(R.string.head_img));
        }
    }

    /**
     * 更换性别
     */
    private void showDialogSex() {
        if (isAlterSex) {
            showDialogChecked();
        } else {
            showToastMsg(getString(R.string.sex_not_change));
        }
    }

    /**
     * 头像是否可以修改
     *
     * @return true-false
     */
    private boolean isChangeHead() {
        return true;
    }

    /**
     * 显示一个消息提示框
     */
    private void showDialogMsg(String string) {
        new QMUIDialog.MessageDialogBuilder(getNowContext())
                .setTitle(string + getString(R.string.not_change))
                .setMessage("当前无法修改" + string + "," + string + "一个月只能修改一次!")
                .addAction(0, "知道了", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 显示一个文本输入框
     */
    private void showDialogEdNicknameText() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getNowContext());
        builder.setTitle(getString(R.string.edit_nickname))
                .setPlaceholder("请输入昵称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("不改了", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确认修改", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 6) {
                            showToastMsg("昵称长度超过最大6个字符！");
                            return;
                        }
                        if (text != null && text.length() > 0) {
                            //执行修改操作
                            //redactNameText.setText(text);
                            editUserNickname(text.toString());
                            dialog.dismiss();
                        } else {
                            //未输入
                            showToastMsg("昵称不可以为空!");
                        }
                    }
                }).show();
    }

    /**
     * 显示一个文本输入框
     */
    private void showDialogSignEdText() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getNowContext());
        builder.setTitle(getString(R.string.edit_sign))
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("不改了", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确认修改", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 15) {
                            showToastMsg("签名长度超过最大15个字符！");
                            return;
                        }
                        if (text != null && text.length() > 0) {
                            //执行修改操作
                            signTv.setText(text);
                            dialog.dismiss();
                        } else {
                            //未输入
                            showToastMsg("签名不可以为空!");
                        }
                    }
                }).show();
    }

    /**
     * 显示一个选择框
     */
    private void showDialogChecked() {
        final String[] items = new String[]{"男", "女"};
        new QMUIDialog.MenuDialogBuilder(getNowContext())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redactSexText.setText(items[which]);
                        sex = which + 1;
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
