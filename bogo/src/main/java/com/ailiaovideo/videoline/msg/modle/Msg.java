package com.ailiaovideo.videoline.msg.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * 消息数据库模型类
 * Created by jiahengfei on 2018/1/16 0016.
 */
@Entity
public class Msg{
    @Id(autoincrement = true)
    private Long id;

    //自身账号
    private String myAccount;

    //对方账号
    private String account;

    //信息对象,如果是一条文本类型,就是文本,如果是其他附件类型,就是地址
    private String msg;

    //信息类型--0文本类型,1图片类型,2语音类型,3视频类型,4附件类型
    private int type;

    //状态--0发出消息(right_chat),1接受消息(left)
    private int status;

    //发送时间
    private Date date;
    //未使用字段
    private String s;

    //信息的发送状态---0发送成功,1发送失败
    private int i;

    public Msg() {
        super();
    }

    @Override
    public String toString() {
        return "Msg{" +
                "id=" + id +
                ", myAccount='" + myAccount + '\'' +
                ", account='" + account + '\'' +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", date=" + date +
                ", s='" + s + '\'' +
                ", i=" + i +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMyAccount(String myAccount) {
        this.myAccount = myAccount;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setS(String s) {
        this.s = s;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Long getId() {

        return id;
    }

    public String getMyAccount() {
        return myAccount;
    }

    public String getAccount() {
        return account;
    }

    public String getMsg() {
        return msg;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public String getS() {
        return s;
    }

    public int getI() {
        return i;
    }

    public Msg(String msg, int type) {
        this.msg = msg;
        this.type = type;
    }

    @Generated(hash = 808619449)
    public Msg(Long id, String myAccount, String account, String msg, int type, int status, Date date,
            String s, int i) {
        this.id = id;
        this.myAccount = myAccount;
        this.account = account;
        this.msg = msg;
        this.type = type;
        this.status = status;
        this.date = date;
        this.s = s;
        this.i = i;
    }
}
