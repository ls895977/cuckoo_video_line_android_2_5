package com.ailiaovideo.videoline.modle.custommsg;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.ailiaovideo.videoline.ICustomMsg;
import com.ailiaovideo.videoline.LiveConstant;
import com.ailiaovideo.videoline.manage.SaveData;
import com.ailiaovideo.videoline.modle.UserModel;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;

public class CustomMsg implements ICustomMsg
{
    private int type;
    private UserModel user;
    private UserModel sender;
    private String deviceType;

    /**
     * 用于群组消息中指定某个用户接收消息，如果不为0而且有值，则只有指定的用户才能处理此消息
     */
    private String user_id;

    public CustomMsg()
    {
        type = LiveConstant.CustomMsgType.MSG_NONE;
        deviceType = "Android";
        sender = SaveData.getInstance().getUserInfo();
        sender.setUser_sign("");
        sender.setToken("");
        if (sender == null)
        {
            LogUtils.i("sender is null--------------------------------------");
        }
    }

    /**
     * 是否是其他用户的消息
     *
     * @return
     */
    public boolean isOtherUserMsg()
    {
        return !TextUtils.isEmpty(user_id) && !"0".equals(user_id) && !user_id.equals(SaveData.getInstance().getId());
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public TIMMessage parseToTIMMessage(TIMMessage msg)
    {
        //TIMMessage msg = null;

        try
        {
            String json = JSON.toJSONString(this);
            byte[] bytes = json.getBytes(LiveConstant.DEFAULT_CHARSET);

            TIMCustomElem elemCustom = new TIMCustomElem();
            elemCustom.setData(bytes);

            //msg = new TIMMessage();
            msg.addElement(elemCustom);
            LogUtils.i("send json:" + json);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return msg;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

    public UserModel getSender()
    {
        return sender;
    }

    public void setSender(UserModel sender)
    {
        this.sender = sender;
    }

    /**
     * 返回用于会话列表中展示的内容
     *
     * @return
     */
    public String getConversationDesc()
    {
        return "";
    }

}
