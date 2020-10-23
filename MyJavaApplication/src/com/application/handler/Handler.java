package com.application.handler;

public abstract class Handler implements IHandler {

    private MessageQueue mMessageQueue;

    protected Handler() {
        mMessageQueue = MessageQueue.getInstance(this);
    }

    public void sendMessage(String msg) {
        sendMessage(msg, 0);
    }

    public void sendMessage(String msg, long delayTime) {
        mMessageQueue.pushToMessageQueue(msg, delayTime);
    }
}
