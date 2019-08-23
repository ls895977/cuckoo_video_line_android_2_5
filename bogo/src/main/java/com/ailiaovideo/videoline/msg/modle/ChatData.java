package com.ailiaovideo.videoline.msg.modle;

import java.io.Serializable;

/**
 * 聊天对象资料模型类
 * Created by jiahengfei on 2018/1/18 0018.
 */

public class ChatData implements Serializable{
    /**
     * 聊天对象id
     */
    private Long id;

    /**
     * 对方账号
     */
    private String account;

    /**
     * 对方名称
     */
    private String name;

    /**
     * 对方头像资源
     */
    private String bitmap;

    /**
     * 最后一条消息
     */
    private Msg lastMsg;

    /**
     * 聊天对象新消息条数
     */
    private int position;

    /**
     * 年龄
     */
    private int sex;

    /**
     * 等级
     */
    private int level;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ChatData(Long id, String account, String name, String bitmap, Msg lastMsg, int position, int sex, int level) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.bitmap = bitmap;
        this.lastMsg = lastMsg;
        this.position = position;
        this.sex = sex;
        this.level = level;
    }

    /**
     * 构造方法
     * @param id
     * @param account
     * @param name
     * @param bitmap
     * @param lastMsg
     * @param position
     */
    public ChatData(Long id, String account, String name, String bitmap, Msg lastMsg, int position) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.bitmap = bitmap;
        this.lastMsg = lastMsg;
        this.position = position;
    }

    /**
     * 构造方法
     * @param account
     * @param name
     * @param bitmap
     * @param lastMsg
     * @param position
     */
    public ChatData(String account, String name, String bitmap, Msg lastMsg, int position) {
        this.account = account;
        this.name = name;
        this.bitmap = bitmap;
        this.lastMsg = lastMsg;
        this.position = position;
    }

    public ChatData() {
        super();
    }


    public Long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getBitmap() {
        return bitmap;
    }

    public Msg getLastMsg() {
        return lastMsg;
    }

    public int getPosition() {
        return position;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMsg(Msg lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", bitmap='" + bitmap + '\'' +
                ", lastMsg=" + lastMsg +
                ", position=" + position +
                '}';
    }
}
