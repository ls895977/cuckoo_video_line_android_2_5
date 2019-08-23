package com.ailiaovideo.videoline.modle.custommsg;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;

import com.ailiaovideo.videoline.event.EventPrivateChatMessage;
import com.ailiaovideo.videoline.ui.common.LoginUtils;
import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.MyApplication;
import com.ailiaovideo.videoline.event.EImOnCloseVideoLine;
import com.ailiaovideo.videoline.event.EImVideoCallEndMessages;
import com.ailiaovideo.videoline.event.EImVideoCallMessages;
import com.ailiaovideo.videoline.event.EImVideoCallReplyMessages;
import com.ailiaovideo.videoline.event.EImOnPrivateMessage;
import com.ailiaovideo.videoline.manage.AppConfig;
import com.ailiaovideo.videoline.ui.CuckooVideoCallWaitActivity;
import com.ailiaovideo.videoline.ui.MainActivity;
import com.ailiaovideo.videoline.utils.BGEventManage;
import com.ailiaovideo.videoline.utils.Utils;
import com.tencent.imcore.MsgStatus;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMGroupSystemElem;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMProfileSystemElem;
import com.tencent.imsdk.TIMSNSSystemElem;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMVideoElem;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageExt;

public class TIMMsgModel extends MsgModel {
    private TIMMessage timMessage;
    private TIMCustomElem timCustomElem;
    private TIMFaceElem timFaceElem;
    private TIMFileElem timFileElem;
    private TIMGroupSystemElem timGroupSystemElem;
    private TIMGroupTipsElem timGroupTipsElem;
    private TIMImageElem timImageElem;
    private TIMLocationElem timLocationElem;
    private TIMProfileSystemElem timProfileSystemElem;
    private TIMSNSSystemElem timSnsSystemElem;
    private TIMSoundElem timSoundElem;
    private TIMTextElem timTextElem;
    private TIMVideoElem timVideoElem;

    private boolean printLog = false;

    public TIMMsgModel(TIMMessage timMessage) {
        super();
        setTimMessage(timMessage);
    }

    public TIMMsgModel(TIMMessage timMessage, boolean printLog) {
        super();
        this.printLog = printLog;
        setTimMessage(timMessage);
    }

    @Override
    public void remove() {
        if (timMessage != null) {
            TIMMessageExt msgExt = new TIMMessageExt(timMessage);
            msgExt.remove();
        }
    }

    public TIMMessage getTimMessage() {
        return timMessage;
    }

    @Override
    public void setTimMessage(TIMMessage timMessage) {
        // 解析消息
        this.timMessage = timMessage;
        readElement();
        parseCustomElem();
    }

    public TIMCustomElem getTimCustomElem() {
        return timCustomElem;
    }

    public void setTimCustomElem(TIMCustomElem timCustomElem) {
        this.timCustomElem = timCustomElem;
    }

    /**
     * 将消息的elem解析出来
     */
    private void readElement() {
        if (timMessage != null) {
            switch (timMessage.status()) {
                case SendFail:
                    setStatus(MsgStatus.kSendFail);
                    break;
                case Sending:
                    setStatus(MsgStatus.kSending);
                    break;
                case SendSucc:
                    setStatus(MsgStatus.kSendSucc);
                    break;
                case HasDeleted:
                    setStatus(MsgStatus.kHasDeleted);
                    break;
                default:
                    setStatus(MsgStatus.kSendFail);
                    break;
            }

            switch (timMessage.getConversation().getType()) {
                case C2C:
                    setConversationType(ConversationType.C2C);
                    break;
                case Group:
                    setConversationType(ConversationType.Group);
                    break;
                case System:
                    setConversationType(ConversationType.System);
                    break;
                default:
                    setConversationType(ConversationType.Invalid);
                    break;
            }

            setSelf(timMessage.isSelf());
            String peer = timMessage.getConversation().getPeer();
            setConversationPeer(peer);
            TIMConversationExt conExt = new TIMConversationExt(timMessage.getConversation());
            setUnreadNum(conExt.getUnreadMessageNum());
            setTimestamp(timMessage.timestamp());

            long count = timMessage.getElementCount();
            TIMElem elem = null;
            for (int i = 0; i < count; i++) {
                elem = timMessage.getElement(i);
                if (elem == null) {
                    continue;
                }
                TIMElemType elemType = elem.getType();
                switch (elemType) {
                    case Custom:
                        setTimCustomElem((TIMCustomElem) elem);
                        break;
                    case Face:
                        setTimFaceElem((TIMFaceElem) elem);
                        break;
                    case File:
                        setTimFileElem((TIMFileElem) elem);
                        break;
                    case GroupSystem:
                        setTimGroupSystemElem((TIMGroupSystemElem) elem);
                        peer = getTimGroupSystemElem().getGroupId();
                        setConversationPeer(peer);
                        break;
                    case GroupTips:
                        setTimGroupTipsElem((TIMGroupTipsElem) elem);
                        break;
                    case Image:
                        setTimImageElem((TIMImageElem) elem);
                        break;
                    case Invalid:

                        break;
                    case Location:
                        setTimLocationElem((TIMLocationElem) elem);
                        break;
                    case ProfileTips:
                        setTimProfileSystemElem((TIMProfileSystemElem) elem);
                        break;
                    case SNSTips:
                        setTimSnsSystemElem((TIMSNSSystemElem) elem);
                        break;
                    case Sound:
                        setTimSoundElem((TIMSoundElem) elem);
                        break;
                    case Text:
                        setTimTextElem((TIMTextElem) elem);
                        break;
                    case Video:
                        setTimVideoElem((TIMVideoElem) elem);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * 将TIMCustomElem解析成自定义消息
     */
    private void parseCustomElem() {
        if (timCustomElem != null || timGroupSystemElem != null) {
            CustomMsg customMsg = parseToModel(CustomMsg.class);
            if (customMsg != null) {
                int type = customMsg.getType();
                if (AppConfig.DEBUG && printLog) {
                    LogUtils.i("TIMMsgModel--------result:" + getConversationType() + " " + getConversationPeer() + " type:" + type);
                }

                //UserModel sender = customMsg.getSender();
                //UserModelDao.updateLevelUp(sender);

                Class realCustomMsgClass = LiveConstant.mapCustomMsgClass.get(type);
                if (realCustomMsgClass == null) {
                    return;
                }
                if (AppConfig.DEBUG && printLog) {
                    LogUtils.i("realCustomMsgClass:" + realCustomMsgClass.getName());
                }
                CustomMsg realCustomMsg = parseToModel(realCustomMsgClass);
                setCustomMsg(realCustomMsg);

                switch (type) {
                    case LiveConstant.CustomMsgType.MSG_TEXT:
                        CustomMsgText customMsgText = getCustomMsgReal();
                        if (timTextElem != null) {
                            customMsgText.setText(timTextElem.getText());
                        }
                        break;
                    case LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL: {
                        TIMMessageExt msgExt = new TIMMessageExt(timMessage);
                        if (!msgExt.isRead() || MyApplication.getInstances().isInPrivateChatPage()) {
                            setMessageRead();
                            LogUtils.i("消息时间：" + timMessage.timestamp());

                            if (Utils.isBackground()) {
                                MyApplication.getInstances().setPushVideoCallMessage(this);
                                //启动后台应用
                                Intent intent = new Intent(MyApplication.getInstances(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PendingIntent pendingIntent =
                                        PendingIntent.getActivity(MyApplication.getInstances(), 0, intent, 0);
                                try {
                                    pendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                EImVideoCallMessages event = new EImVideoCallMessages();
                                event.msg = this;
                                BGEventManage.post(event);
                            }
                        }
                        break;
                    }
                    case LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL_REPLY: {
                        TIMMessageExt msgExt = new TIMMessageExt(timMessage);
                        if (!msgExt.isRead() || MyApplication.getInstances().isInPrivateChatPage()) {
                            setMessageRead();
                            EImVideoCallReplyMessages event = new EImVideoCallReplyMessages();
                            event.msg = this;
                            BGEventManage.post(event);
                        }
                        break;
                    }
                    case LiveConstant.CustomMsgType.MSG_VIDEO_LINE_CALL_END: {
                        TIMMessageExt msgExt = new TIMMessageExt(timMessage);
                        if (!msgExt.isRead() || MyApplication.getInstances().isInPrivateChatPage()) {
                            CustomMsgVideoCallEnd customMsgVideoCallEnd = getCustomMsgReal();
                            setMessageRead();
                            EImVideoCallEndMessages event = new EImVideoCallEndMessages();
                            event.customMsgVideoCallEnd = customMsgVideoCallEnd;
                            event.msg = this;
                            BGEventManage.post(event);
                        }
                        break;
                    }
                    case LiveConstant.CustomMsgType.MSG_PRIVATE_GIFT: {

                        CustomMsgPrivateGift customMsgPrivateGift = (CustomMsgPrivateGift) realCustomMsg;
                        EImOnPrivateMessage event = new EImOnPrivateMessage();
                        event.customMsgPrivateGift = customMsgPrivateGift;
                        BGEventManage.post(event);
                        //LogUtils.i("收到礼物LiveConstant.CustomMsgType.MSG_PRIVATE_GIFT" + test.getFrom_msg());
                        break;
                    }
                    case LiveConstant.CustomMsgType.MSG_CLOSE_VIDEO_LINE: {

                        CustomMsgCloseVideo customMsgCloseVideo = (CustomMsgCloseVideo) realCustomMsg;
                        EImOnCloseVideoLine event = new EImOnCloseVideoLine();
                        event.customMsgCloseVideo = customMsgCloseVideo;
                        BGEventManage.post(event);

                        break;
                    }
                    default:
                        break;
                }
            }
        } else if (timTextElem != null) {
            EventPrivateChatMessage event = new EventPrivateChatMessage();
            event.setMsg(timTextElem.getText());
            BGEventManage.post(event);
        }
    }

    //将消息设置为已读状态
    private void setMessageRead() {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, timMessage.getSender());
        TIMConversationExt conExt = new TIMConversationExt(con);
        conExt.setReadMessage(timMessage, null);
    }


    public TIMFaceElem getTimFaceElem() {
        return timFaceElem;
    }

    public void setTimFaceElem(TIMFaceElem timFaceElem) {
        this.timFaceElem = timFaceElem;
    }

    public TIMFileElem getTimFileElem() {
        return timFileElem;
    }

    public void setTimFileElem(TIMFileElem timFileElem) {
        this.timFileElem = timFileElem;
    }

    public TIMGroupSystemElem getTimGroupSystemElem() {
        return timGroupSystemElem;
    }

    public void setTimGroupSystemElem(TIMGroupSystemElem timGroupSystemElem) {
        this.timGroupSystemElem = timGroupSystemElem;
    }

    public TIMGroupTipsElem getTimGroupTipsElem() {
        return timGroupTipsElem;
    }

    public void setTimGroupTipsElem(TIMGroupTipsElem timGroupTipsElem) {
        this.timGroupTipsElem = timGroupTipsElem;
    }

    public TIMImageElem getTimImageElem() {
        return timImageElem;
    }

    public void setTimImageElem(TIMImageElem timImageElem) {
        this.timImageElem = timImageElem;
    }

    public TIMLocationElem getTimLocationElem() {
        return timLocationElem;
    }

    public void setTimLocationElem(TIMLocationElem timLocationElem) {
        this.timLocationElem = timLocationElem;
    }

    public TIMProfileSystemElem getTimProfileSystemElem() {
        return timProfileSystemElem;
    }

    public void setTimProfileSystemElem(TIMProfileSystemElem timProfileSystemElem) {
        this.timProfileSystemElem = timProfileSystemElem;
    }

    public TIMSNSSystemElem getTimSnsSystemElem() {
        return timSnsSystemElem;
    }

    public void setTimSnsSystemElem(TIMSNSSystemElem timSnsSystemElem) {
        this.timSnsSystemElem = timSnsSystemElem;
    }

    public TIMSoundElem getTimSoundElem() {
        return timSoundElem;
    }

    public void setTimSoundElem(TIMSoundElem timSoundElem) {
        this.timSoundElem = timSoundElem;
    }

    public TIMTextElem getTimTextElem() {
        return timTextElem;
    }

    public void setTimTextElem(TIMTextElem timTextElem) {
        this.timTextElem = timTextElem;
    }

    public TIMVideoElem getTimVideoElem() {
        return timVideoElem;
    }

    public void setTimVideoElem(TIMVideoElem timVideoElem) {
        this.timVideoElem = timVideoElem;
    }

    public <T extends CustomMsg> T parseToModel(Class<T> clazz) {
        T model = null;
        String json = null;
        try {
            byte[] data = null;
            if (timGroupSystemElem != null) {
                data = timGroupSystemElem.getUserData();
            }
            if (data == null) {
                data = timCustomElem.getData();
            }

            json = new String(data, LiveConstant.DEFAULT_CHARSET);
            model = JSON.parseObject(json, clazz);

            if (AppConfig.DEBUG && printLog) {
                LogUtils.i("parseToModel " + model.getType() + ":" + json);
            }
        } catch (Exception e) {
            if (AppConfig.DEBUG && printLog) {
                e.printStackTrace();
                LogUtils.e("(" + getConversationPeer() + ")parse msg error:" + e.toString() + ",json:" + json);
            }
        } finally {

        }
        return model;
    }
}
