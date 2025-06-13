package com.application.handler;

public abstract class Handler implements IHandler {


    protected Handler() {
        WorkLooper.getInstance(this);//创建handler 消息loop线程
    }

    public void sendMessage(String msg) {
        sendMessage(msg, 0);
    }

    public void sendMessage(String msg, long delayTime) {
        WorkLooper.mMessageQueue.pushToMessageQueue(msg, delayTime);
    }
}
