package com.idealbank.module_main.Netty.bean;

/**
 * Created by zw on 2018/9/11.
 */


import java.io.Serializable;

public class Message<T> implements Serializable {

    private static final long serialVersionUID = -5756901646411393269L;

    int id;

    //用戶id
    MsgType type;//信息類型

    T responseMessage;//信息對象

    byte[] attachment;//文件

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public T getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(T responseMessage) {
        this.responseMessage = responseMessage;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public Message() {

    }

    public Message(MsgType type) {
        this.type = type;
    }
}