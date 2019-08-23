package com.ailiaovideo.chat.model;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.chat.adapter.ChatAdapter;
import com.ailiaovideo.chat.utils.FileUtil;
import com.ailiaovideo.chat.utils.MediaUtil;
import com.ailiaovideo.videoline.R;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.utils.Utils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 语音消息数据
 */
public class VoiceMessage extends Message {

    private static final String TAG = "VoiceMessage";

    public VoiceMessage(TIMMessage message){
        this.message = message;
    }


    /**
     * 语音消息构造方法
     *
     * @param duration 时长
     * @param filePath 语音数据地址
     */
    public VoiceMessage(long duration,String filePath){
        message = new TIMMessage();
        TIMSoundElem elem = new TIMSoundElem();
        elem.setPath(filePath);
        elem.setDuration(duration);  //填写语音时长
        message.addElement(elem);
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context 显示消息的上下文
     */
    @Override
    public void showMessage(final ChatAdapter.ViewHolder viewHolder,final Context context) {
        if (checkRevoke(viewHolder)) return;
        LinearLayout linearLayout = new LinearLayout(MyApplication.getInstances());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        ImageView voiceIcon = new ImageView(MyApplication.getInstances());
        voiceIcon.setBackgroundResource(message.isSelf()? R.drawable.right_voice: R.drawable.left_voice);

        setSenderUserInfo(viewHolder,context,null);

        final AnimationDrawable frameAnimatio = (AnimationDrawable) voiceIcon.getBackground();

        TextView tv = new TextView(MyApplication.getInstances());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(MyApplication.getInstances().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        tv.setText(String.valueOf(((TIMSoundElem) message.getElement(0)).getDuration()) + "’");
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imageLp = new LinearLayout.LayoutParams(width, height);
        if (message.isSelf()){
            linearLayout.addView(tv);
            imageLp.setMargins(10, 0, 0, 0);
            voiceIcon.setLayoutParams(imageLp);
            linearLayout.addView(voiceIcon);
        }else{
            voiceIcon.setLayoutParams(imageLp);
            linearLayout.addView(voiceIcon);
            lp.setMargins(10, 0, 0, 0);
            tv.setLayoutParams(lp);
            linearLayout.addView(tv);
        }
        clearView(viewHolder);
        getBubbleView(viewHolder).addView(linearLayout);
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceMessage.this.playAudio(frameAnimatio);


            }
        });
        showStatus(viewHolder);
    }


    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        return MyApplication.getInstances().getString(R.string.summary_voice);
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private void playAudio(final AnimationDrawable frameAnimatio) {
        TIMSoundElem elem = (TIMSoundElem) message.getElement(0);
        final File tempAudio = FileUtil.getTempFile(FileUtil.FileType.AUDIO);
        elem.getSoundToFile(tempAudio.getAbsolutePath(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                try {
                    FileInputStream fis = new FileInputStream(tempAudio);
                    MediaUtil.getInstance().play(fis);
                    frameAnimatio.start();
                    MediaUtil.getInstance().setEventListener(new MediaUtil.EventListener() {
                        @Override
                        public void onStop() {
                            frameAnimatio.stop();
                            frameAnimatio.selectDrawable(0);
                        }
                    });
                }catch (Exception e) {

                }

            }
        });

    }
}
