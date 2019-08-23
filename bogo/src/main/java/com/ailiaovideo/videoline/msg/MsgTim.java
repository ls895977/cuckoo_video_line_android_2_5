package com.ailiaovideo.videoline.msg;

import android.util.Log;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.List;

import static io.agora.rtc.internal.AudioRoutingController.TAG;

/**
 * 腾讯云消息处理工具类
 * Created by jiahengfe on 2018/1/23 0023.
 */

public class MsgTim {
    public static MsgTim instaner;
    private String identifier;
    private MsgTim(String identifier){
        this.identifier = identifier;
    }

    /**
     * 获取消息操作单例
     * @param identifier 目标对象名
     * @return MsgTim
     */
    public static MsgTim getInstaner(String identifier){
        if (instaner == null){
            return new MsgTim(identifier);
        }else{
            instaner.setIdentifier(identifier);
            return instaner;
        }
    }

    /**
     * 获取单聊会话
     * @return TIMConversation
     */
    public TIMConversation getConversation(){
        //获取单聊会话
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                identifier);
        return conversation;
    }

    /**
     * 获取群组会话
     * @return TIMConversation
     */
    public TIMConversation getConversationGroup(){
        //获取群组会话
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,      //会话类型：群组
                identifier);
        return conversation;
    }

    /**
     * 发送单聊文本信息
     * @param text 文本信息
     * @param timValueCallBack 回调接口
     */
    public void sendMsg(String text,TIMValueCallBack timValueCallBack){
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(text);
        //将elem添加到消息
        if(msg.addElement(elem) != 0) {
            return;
        }
        //发送消息
        getConversation().sendMessage(msg,timValueCallBack);
    }

    /**
     * 发送单聊图片信息
     * @param path 图片路径
     * @param timValueCallBack 回调接口
     */
    public void sendImgMsg(String path,TIMValueCallBack timValueCallBack){
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加图片
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);
        //将elem添加到消息
        if(msg.addElement(elem) != 0) {
            return;
        }
        //发送消息
        getConversation().sendMessage(msg,timValueCallBack);
    }

    /**
     * 发送群聊文本信息
     * @param text 文本信息
     * @param timValueCallBack 回调接口
     */
    public void sendMsgGroup(String text,TIMValueCallBack timValueCallBack){
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(text);
        //将elem添加到消息
        if(msg.addElement(elem) != 0) {
            return;
        }
        //发送消息
        getConversationGroup().sendMessage(msg,timValueCallBack);
    }

    /**
     * 发送群聊图片信息
     * @param paths 图片路径
     * @param timValueCallBack 回调接口
     */
    public void sendMsgGroup(List<String> paths, TIMValueCallBack timValueCallBack){
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加图片
        TIMImageElem elem = new TIMImageElem();
        for (String path:paths) {
            elem.setPath(path);
        }
        Log.d(TAG, "sendMsgGroup: "+elem.getImageList());
        //将elem添加到消息
        if(msg.addElement(elem) != 0) {
            return;
        }
        //发送消息
        getConversationGroup().sendMessage(msg,timValueCallBack);
    }



    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
