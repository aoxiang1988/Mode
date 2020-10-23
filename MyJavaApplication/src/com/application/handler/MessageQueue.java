package com.application.handler;

import sun.misc.Lock;
import java.util.Date;

public class MessageQueue {

    private static MessageQueue mMessageQueue = null;
    private static Handler mHandler;

    static MessageQueue getInstance(Handler handler) {
        if (mMessageQueue == null) {
            mMessageQueue = new MessageQueue();
            mMessageQueue.initMessageQueue();
        }
        mHandler = handler;
        return mMessageQueue;
    }

    public static class Message {
        Message front;
        Message next;
        long time;
        long storeTime;
        public String msgString;
    }

    private Message mHandlerMsgQue = null;

    private void initMessageQueue() {
        mHandlerMsgQue = new Message();
        mHandlerMsgQue.front = null;
        mHandlerMsgQue.next = null;
        mHandlerMsgQue.time = -1;
        mHandlerMsgQue.storeTime = -1;
        mHandlerMsgQue.msgString = null;
    }

    private final Lock mLock = new Lock();

    void pushToMessageQueue(String msg, long delayTime) {
        synchronized (mLock) {
            Date currentTime = new Date();
            Message msgNode = new Message();
            msgNode.next = null;
            msgNode.front = null;
            msgNode.msgString = msg;
            msgNode.time = currentTime.getTime() + delayTime;
            msgNode.storeTime = currentTime.getTime();

            Message tempNode = mHandlerMsgQue;
            boolean flag = false;
            while (tempNode.next != null) {
                if (tempNode.time <= msgNode.time && tempNode.next.time >= msgNode.time) {
                    msgNode.next = tempNode.next;
                    msgNode.front = tempNode;
                    msgNode.next.front = msgNode;
                    msgNode.front.next = msgNode;
                    flag = true;
                    break;
                }
                tempNode = tempNode.next;
            }
            if (!flag) {
                tempNode.next = msgNode;
                msgNode.front = tempNode;
            }
        }
    }

    boolean isQueueEmpty() {
        return false;
    }

    void sendMsgToMainThread() throws NullPointerException {
        synchronized (mLock) {
            Date currentTime = new Date();
            Message tempNode = mHandlerMsgQue.next;
            Message resultNode;
            while (tempNode != null && tempNode.time != -1) {
                if (tempNode.time <= currentTime.getTime()) {
                    resultNode = tempNode;
                    tempNode = tempNode.next;
                    if (tempNode != null) {
                        tempNode.front = resultNode.front;
                    }
                    resultNode.front.next = tempNode;
                    resultNode.next = null;
                    resultNode.front = null;
                    resultNode.time = 0;
                    mHandler.handleMessage(resultNode);
                }
                if (tempNode != null) {
                    tempNode = tempNode.next;
                }
            }
        }
    }
}
