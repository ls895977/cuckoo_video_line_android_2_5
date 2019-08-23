package com.ailiaovideo.videoline.modle.custommsg;

/**
 * 私聊礼物消息
 */
public class InputListenerMsgText{


    /**
     * userAction : 14
     * actionParam : EIMAMSG_InputStatus_Ing
     */

    private int userAction;
    private String actionParam;

    public int getUserAction() {
        return userAction;
    }

    public void setUserAction(int userAction) {
        this.userAction = userAction;
    }

    public String getActionParam() {
        return actionParam;
    }

    public void setActionParam(String actionParam) {
        this.actionParam = actionParam;
    }
}
