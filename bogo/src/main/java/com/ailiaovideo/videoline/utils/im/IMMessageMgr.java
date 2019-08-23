package com.ailiaovideo.videoline.utils.im;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ailiaovideo.videoline.helper.ContentUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupSystemElem;
import com.tencent.imsdk.TIMGroupSystemElemType;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.FormatFlagsConversionMismatchException;
import java.util.List;


/**
 * Created by jac on 2017/11/4.
 * Copyright © 2013-2017 Tencent Cloud. All Rights Reserved.
 */

public class IMMessageMgr implements TIMMessageListener {

    private static final String TAG = IMMessageMgr.class.getSimpleName();

    private Context             mContext;
    private Handler             mHandler;
    private static boolean      mConnectSuccess = false;
    private boolean             mLoginSuccess = false;

    private String              mSelfUserID;
    private String              mSelfUserSig;
    private int                 mAppID;
    private boolean             mInGroup;
    private String              mGroupID;
    private String              mGroupName;
    private TIMUserConfig       mTIMSdkConfig;
    private IMMessageConnCallback  mIMConnListener;
    private IMMessageLoginCallback mIMLoginListener;
    private IMMessageCallback      mMessageListener;

    public IMMessageMgr(final Context context) {
        this.mContext = context.getApplicationContext();
        this.mHandler = new Handler(this.mContext.getMainLooper());
        this.mMessageListener = new IMMessageCallback(null);
    }

    public void setIMMessageListener(IMMessageListener listener){
        this.mMessageListener.setListener(listener);
    }

    public void initialize(final String userID,
                           final String userSig,
                           final int appID, final Callback callback){

        if (userID == null || userSig == null) {
            mMessageListener.onDebugLog("参数错误，请检查 UserID， userSig 是否为空！");
            callback.onError(-1, "参数错误");
            return;
        }

        final long initializeStartTS = System.currentTimeMillis(); //监控下 IM 初始化时间

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                mIMConnListener = new IMMessageConnCallback(initializeStartTS, callback);

                mTIMSdkConfig = new TIMUserConfig();
                mTIMSdkConfig.setConnectionListener(mIMConnListener);
                //初始化SDK
                TIMSdkConfig config = new TIMSdkConfig(ContentUtils.TxContent.SDK_APPID);
                config.enableLogPrint(true).setLogLevel(TIMLogLevel.INFO);

                TIMManager.getInstance().addMessageListener(IMMessageMgr.this);
                if( TIMManager.getInstance().init(mContext, config) ){
                    updateLoginInfo(userID, userSig, appID);
                    login(new Callback() {
                        @Override
                        public void onError(int code, String errInfo) {
                            print("login failed: %s(%d)", errInfo, code);
                            mLoginSuccess = false;
                            callback.onError(code, "IM登录失败");
                        }

                        @Override
                        public void onSuccess(Object... args) {
                            print("login success");
                            mLoginSuccess = true;
                            callback.onSuccess();
                        }
                    });
                }
                else {
                    print("init failed");
                    callback.onError(-1, "IM初始化失败");
                }
            }
        });
    }

    public void unInitialize(){
        mContext = null;
        mHandler = null;
        if (mTIMSdkConfig != null) {
            mTIMSdkConfig.setConnectionListener(null);
            mTIMSdkConfig = null;
        }
        if (mIMConnListener != null) {
            mIMConnListener.clean();
            mIMConnListener  = null;
        }
        if (mIMLoginListener != null) {
            mIMLoginListener.clean();
            mIMLoginListener = null;
        }
        if (mMessageListener != null) {
            mMessageListener.setListener(null);
            mMessageListener = null;
        }
        TIMManager.getInstance().removeMessageListener(IMMessageMgr.this);
        logout(null);
    }

    @Deprecated
    public void createGroup(final String groupId, final String groupName, final Callback callback){
        if (!mConnectSuccess || !mLoginSuccess){
            mMessageListener.onDebugLog("[createGroup] IM 没有初始化");
            callback.onError(-1, "IM 没有初始化");
            return;
        }

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                TIMGroupManager.CreateGroupParam param = new TIMGroupManager.CreateGroupParam("AVChatRoom", groupName);
                param.setGroupId(groupId);

                TIMGroupManager.getInstance().createGroup(param, new TIMValueCallBack<String>() {
                    @Override
                    public void onError(int i, String s) {
                        print("创建群 \"%s(%s)\" 失败: %s(%d)", groupName, groupId, s, i);
                        callback.onError(i, s);
                    }

                    @Override
                    public void onSuccess(String s) {
                        print("创建群 \"%s(%s)\" 成功", groupName, groupId);
                        updateGroup(groupId, groupName, false);
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    public void jionGroup(final String groupId, final Callback callback){
        if (!mConnectSuccess || !mLoginSuccess){
            mMessageListener.onDebugLog("[jionGroup] IM 没有初始化");
            callback.onError(-1, "IM 没有初始化");
            return;
        }

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                TIMGroupManager.getInstance().applyJoinGroup(groupId, "who care?", new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        print("加入群 {%s} 失败:%s(%d)", groupId, s, i);
                        if (i == 10010) {
                            s = "房间已解散";
                        }
                        callback.onError(i, s);
                    }

                    @Override
                    public void onSuccess() {
                        print("加入群 {%s} 成功", groupId);
                        updateGroup(groupId, null, true);
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    public void quitGroup(final String groupId, final Callback callback){
        if (!mConnectSuccess || !mLoginSuccess){
            mMessageListener.onDebugLog("[quitGroup] IM 没有初始化");
            callback.onError(-1, "IM 没有初始化");
            return;
        }

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        if (i == 10010) {
                            print("群 {%s} 已经解散了", groupId);
                            onSuccess();
                        }
                        else{
                            print("退出群 {%s} 失败： %s(%d)", groupId, s, i);
                            callback.onError(i, s);
                        }
                    }

                    @Override
                    public void onSuccess() {
                        print("退出群 {%s} 成功", groupId);
                        updateGroup(groupId, null, false);
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    public void destroyGroup(final String groupId, final Callback callback){
        if (!mConnectSuccess || !mLoginSuccess){
            mMessageListener.onDebugLog("IM 没有初始化");
            callback.onError(-1, "IM 没有初始化");
            return;
        }

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                TIMGroupManager.getInstance().deleteGroup(groupId, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        print("解散群 {%s} 失败：%s(%d)", groupId, s, i);
                        callback.onError(i, s);
                    }

                    @Override
                    public void onSuccess() {
                        print("解散群 {%s} 成功", groupId);
                        updateGroup(groupId, null, false);
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    public void sendGroupMessage(@NonNull String userName, @NonNull String headPic, @NonNull String text, Callback callback){
       sendGroupMessage(userName, headPic, text, mGroupID, callback);
    }

    public void sendGroupMessage(final @NonNull String userName, final @NonNull String headPic, final @NonNull String text, final String groupId, final Callback callback){
        if (!mConnectSuccess || !mLoginSuccess){
            mMessageListener.onDebugLog("[sendGroupMessage] IM 没有初始化");
            if (callback != null)
                callback.onError(-1, "IM 没有初始化");
            return;
        }

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);

                TIMMessage message = new TIMMessage();

                try {
                    UserInfo userInfo = new UserInfo();
                    userInfo.nickName = userName;
                    userInfo.headPic = headPic;

                    TextHeadMsg txtHeadMsg = new TextHeadMsg();
                    txtHeadMsg.cmd = "CustomTextMsg";
                    txtHeadMsg.data = userInfo;

                    String strCmdMsg = JSON.toJSONString(txtHeadMsg);

                    TIMCustomElem customElem = new TIMCustomElem();
                    customElem.setData(strCmdMsg.getBytes("UTF-8"));

                    TIMTextElem textElem = new TIMTextElem();
                    textElem.setText(text);

                    message.addElement(customElem);
                    message.addElement(textElem);
                }
                catch (Exception e) {
                    print("[sendGroupMessage] 发送群{%s}消息失败，组包异常", groupId);
                    if (callback != null) {
                        callback.onError(-1, "发送群消息失败");
                    }
                    return;
                }

                conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
                    @Override
                    public void onError(int i, String s) {
                        print("[sendGroupMessage] 发送群{%s}消息失败: %s(%d)", groupId, s, i);

                        if (callback != null)
                            callback.onError(i, s);
                    }

                    @Override
                    public void onSuccess(TIMMessage timMessage) {
                        print("[sendGroupMessage] 发送群消息成功");

                        if (callback != null)
                            callback.onSuccess();
                    }
                });
            }
        });
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {

        for (TIMMessage message : list) {

            for (int i = 0; i < message.getElementCount(); i++) {
                TIMElem element = message.getElement(i);

                print("onNewMessage type = %s", element.getType());

                switch (element.getType()){

                    case GroupSystem:{
                        TIMGroupSystemElemType systemElemType = ((TIMGroupSystemElem) element).getSubtype();

                        switch (systemElemType){

                            case TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE:{
                                print("onNewMessage subType = %s", systemElemType);
                                if (mMessageListener != null)
                                    mMessageListener.onGroupDestroyed();
                                return true;
                            }

                            case TIM_GROUP_SYSTEM_CUSTOM_INFO:{

                                byte[] userData = ((TIMGroupSystemElem) element).getUserData();
                                if (userData == null || userData.length == 0){
                                    print("userData == null");
                                    return true;
                                }

                                String data = new String(userData);
                                print("onNewMessage subType = %s content = %s", systemElemType, data);
                                CommandMsg msg = JSON.parseObject(data, CommandMsg.class);
                                if (msg.cmd.equals("notifyPusherChange")) {
                                    mMessageListener.onMemberChanged();
                                }
                                return true;
                            }
                        }

                        break;
                    }//case GroupSystem

                    case Custom: {
                        byte[] userData = ((TIMCustomElem) element).getData();
                        if (userData == null || userData.length == 0){
                            print("userData == null");
                            return true;
                        }

                        String data = new String(userData);
                        print("onNewMessage subType = Custom content = %s", data);
                        TextHeadMsg txtHeadMsg = JSON.parseObject(data, TextHeadMsg.class);
                        if (txtHeadMsg.cmd.equalsIgnoreCase("CustomTextMsg")) {
                            ++i;
                            UserInfo userInfo = txtHeadMsg.data;
                            if (userInfo != null && i < message.getElementCount()) {
                                TIMElem nextElement = message.getElement(i);
                                TIMTextElem textElem = (TIMTextElem) nextElement;
                                String text = textElem.getText();
                                if (text != null){
                                    mMessageListener.onGroupMessage(message.getSender(), userInfo.nickName, userInfo.headPic, text);
                                }
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void login(final Callback cb){
        if (mSelfUserID == null || mSelfUserSig == null ){
            cb.onError(-1, "没有 UserId");
            return;
        }

        Log.i(TAG, "start login: userId = " + this.mSelfUserID);
        
        final long loginStartTS = System.currentTimeMillis();

        mIMLoginListener = new IMMessageLoginCallback(loginStartTS, cb);

        TIMManager.getInstance().login(this.mSelfUserID, this.mSelfUserSig, mIMLoginListener);
    }

    private void logout(final Callback callback){
        if (!mConnectSuccess || !mLoginSuccess){
            return;
        }

        TIMManager.getInstance().logout(null);
    }

    private void updateLoginInfo(String userId, String userSig, int appId){
        this.mSelfUserID  = userId;
        this.mSelfUserSig = userSig;
        this.mAppID       = appId;
    }

    private void updateGroup(String groupId, String groupName, boolean mInGroup){
        this.mGroupID   = groupId;
        this.mGroupName = groupName;
        this.mInGroup   = mInGroup;
    }

    private void print(String format, Object ...args){
        String s = null;
        try {
            s = String.format(format, args);
            Log.e(TAG, s);
            if (mMessageListener != null) {
                mMessageListener.onDebugLog(s);
            }
        } catch (FormatFlagsConversionMismatchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 辅助类 IM Connect Listener
     */
    private final class IMMessageConnCallback implements TIMConnListener {
        private long            initializeStartTS = 0;
        private Callback        callback;
        
        public IMMessageConnCallback(long ts, Callback cb) {
            initializeStartTS = ts;
            callback = cb;
        }

        public void clean() {
            initializeStartTS = 0;
            callback = null;
        }
        
        @Override
        public void onConnected() {
            print("connect success，initialize() time cost %.2f secs", (System.currentTimeMillis() - initializeStartTS) / 1000.0);
            mMessageListener.onConnected();

            mConnectSuccess = true;
        }

        @Override
        public void onDisconnected(int i, String s) {
            print("disconnect: %s(%d)", s, i);
            if (mLoginSuccess) {
                mMessageListener.onDisconnected();
            }
            else {
                callback.onError(i, s);
            }
            mConnectSuccess = false;
        }

        @Override
        public void onWifiNeedAuth(String s) {
            print("onWifiNeedAuth(): %s", s);
            if (mLoginSuccess){
                mMessageListener.onDisconnected();
            }
            else {
                callback.onError(-1, s);
            }
            mConnectSuccess = false;
        }
    }

    /**
     * 辅助类 IM Login Listener
     */
    private final class IMMessageLoginCallback implements TIMCallBack {
        private long      loginStartTS ;
        private Callback  callback;
        
        public IMMessageLoginCallback(long ts, Callback cb) {
            loginStartTS = ts;
            callback = cb;
        }

        public void clean() {
            loginStartTS = 0;
            callback = null;
        }

        @Override
        public void onError(int i, String s) {
            if (callback != null) {
                callback.onError(i, s);
            }
        }

        @Override
        public void onSuccess() {
            print("login success, time cost %.2f secs", (System.currentTimeMillis()- loginStartTS) / 1000.0);
            if (callback != null) {
                callback.onSuccess();
            }
        }
    };
    
    /**
     * 辅助类 IM Message Listener
     */
    private final class IMMessageCallback implements IMMessageListener {

        private IMMessageListener listener;

        public IMMessageCallback(IMMessageListener listener) {
            this.listener = listener;
        }

        public void setListener(IMMessageListener listener) {
            this.listener = listener;
        }

        @Override
        public void onConnected() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.onConnected();
                }
            });
        }

        @Override
        public void onDisconnected() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.onDisconnected();
                }
            });
        }
        @Override
        public void onMemberChanged() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.onMemberChanged();
                }
            });
        }

        @Override
        public void onGroupDestroyed() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.onGroupDestroyed();
                }
            });
        }

        @Override
        public void onDebugLog(final String line) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.onDebugLog("[IM] "+line);
                }
            });
        }

        @Override
        public void onGroupMessage(final String senderId, final String userName, final String headPic, final String message) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.onGroupMessage(senderId, userName, headPic, message);
                }
            });
        }
    }


    public interface Callback{
        void onError(int code, String errInfo);
        void onSuccess(Object... args);
    }


    public interface IMMessageListener {
        void onConnected();
        void onDisconnected();
        void onMemberChanged();
        void onGroupDestroyed();
        void onGroupMessage(String senderId, String userName, String headPic, String message);
        void onDebugLog(String line);
    }


    private static final class CommandMsg{
        String cmd;
        String data;
    }

    private static final class UserInfo {
        String nickName;
        String headPic;
    }

    private static final class TextHeadMsg {
        String cmd;
        UserInfo data;
    }
}
