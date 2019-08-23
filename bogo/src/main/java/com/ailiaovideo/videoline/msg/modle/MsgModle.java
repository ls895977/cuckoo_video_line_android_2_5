package com.ailiaovideo.videoline.msg.modle;

/**
 * 消息模型显示类
 * Created by jiahengfei on 2018/1/29 0029.
 */

public class MsgModle {
    //模型类id
    private long id;

    //消息读取类型^^tip系统级消息^^left接收消息^^right发送消息
    private Type type;

    private Msg msg;

    public enum Type{
        Tip,
        Left,
        Right
    }

    @Override
    public String toString() {
        return "MsgModle{" +
                "id=" + id +
                ", type=" + type +
                ", msg=" + msg +
                '}';
    }

    public MsgModle(long id, Type type, Msg msg) {
        this.id = id;
        this.type = type;
        this.msg = msg;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return id;
    }
    public void setType(Type type) {
        this.type = type;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public Msg getMsg() {
        return msg;
    }

    public MsgModle() {
        super();
    }
}
