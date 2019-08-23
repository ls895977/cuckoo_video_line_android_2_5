package com.ailiaovideo.videoline.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.adapter.FullyGridLayoutManager;
import com.ailiaovideo.videoline.adapter.GridImageAdapter;
import com.ailiaovideo.videoline.api.Api;
import com.ailiaovideo.videoline.audiorecord.AudioPlaybackManager;
import com.ailiaovideo.videoline.audiorecord.AudioRecordJumpUtil;
import com.ailiaovideo.videoline.audiorecord.entity.AudioEntity;
import com.ailiaovideo.videoline.audiorecord.util.PaoPaoTips;
import com.ailiaovideo.videoline.audiorecord.view.CommonSoundItemView;
import com.ailiaovideo.videoline.base.BaseActivity;
import com.ailiaovideo.videoline.event.EventBusConfig;
import com.ailiaovideo.videoline.event.RefreshMessageEvent;
import com.ailiaovideo.videoline.event.VoiceRecordEvent;
import com.ailiaovideo.videoline.json.JsonRequestBase;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.utils.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.callback.StringCallback;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PushDynamicActivity extends BaseActivity {

    @BindView(R.id.btn_voice_record)
    Button mBtnVoiceRecord;

    @BindView(R.id.et_input)
    EditText mEtInput;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.pp_sound_item_view)
    CommonSoundItemView soundItemView;

    private GridImageAdapter adapter;

    private boolean hasVoiceFile = false;
    private String voiceFilePath = "";
    private List<LocalMedia> selectList = new ArrayList<>();
    private int maxSelectNum = 9;

    @Override
    protected Context getNowContext() {
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_push_dynamic;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initSet() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPlayerDisplayData() {

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

            PictureSelector.create(PushDynamicActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(maxSelectNum)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };

    @OnClick({R.id.rl_input,R.id.btn_voice_record, R.id.tv_push, R.id.tv_cancel})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_input:
                KeyboardUtils.showSoftInput(mEtInput);
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_push:

                clickPushDynamic();
                break;
            case R.id.btn_voice_record:
                if (hasVoiceFile) {
                    File tempFile = new File(voiceFilePath);
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                    hasVoiceFile = false;
                    soundItemView.setVisibility(View.GONE);
                    mBtnVoiceRecord.setText("录制音频");
                    return;
                }

                AudioRecordJumpUtil.startRecordAudio(PushDynamicActivity.this);
                break;

//            case R.id.iv_add_img:
//
//
//                PictureSelector.create(this)
//                        .openGallery(PictureMimeType.ofImage())
//                        .maxSelectNum(9)
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
//                break;

            default:
                break;
        }
    }

    private void clickPushDynamic() {
        String content = mEtInput.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showLong("内容不能为空！");
            return;
        }

        showLoadingDialog("正在发布...");
        Api.doRequestPushDynamic(SaveData.getInstance().getId(), SaveData.getInstance().getToken(),
                content,
                hasVoiceFile ? 1 : 0,
                selectList, voiceFilePath, new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        hideLoadingDialog();
                        JsonRequestBase data = JsonRequestBase.getJsonObj(s, JsonRequestBase.class);
                        if (StringUtils.toInt(data.getCode()) == 1) {
                            ToastUtils.showLong("发布成功！");
                            EventBus.getDefault().post(new RefreshMessageEvent("refresh_dynamic_list"));
                            finish();
                        } else {
                            ToastUtils.showLong(data.getMsg());
                        }

                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VoiceRecordEvent mainThreadEvent) {
        if (mainThreadEvent.getWhat() == EventBusConfig.SOUND_FEED_RECORD_FINISH) {
            Object soundPath = mainThreadEvent.getObj();
            if (soundPath != null && soundPath instanceof String) {
                String path = (String) soundPath;
                voiceFilePath = path;
                AudioEntity entity = new AudioEntity();
                entity.setUrl(path);
                int duration = AudioPlaybackManager.getDuration(path);
                if (duration <= 0) {
                    //PPLog.d(TAG, "duration <= 0");
                    PaoPaoTips.showDefault(this, "无权限");

                    File tempFile = new File(path);
                    if (tempFile.exists()) {
                        tempFile.delete();
                        return;
                    }
                } else {
                    entity.setDuration(duration / 1000);
                    mBtnVoiceRecord.setText("删除音频");
                    soundItemView.setSoundData(entity);
                    soundItemView.setVisibility(View.VISIBLE);
                    hasVoiceFile = true;
                    //PPLog.d(TAG, "soundPath:" + path);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
