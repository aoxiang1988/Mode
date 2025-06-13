package com.application.handler;

import sun.misc.Lock;
import java.util.Date;

public class MessageQueue {

    private static Handler mHandler;

    static MessageQueue getInstance(Handler handler) {
        if (WorkLooper.mMessageQueue == null) {
            WorkLooper.mMessageQueue = new MessageQueue();
            WorkLooper.mMessageQueue.initMessageQueue();
        }
        mHandler = handler;
        return WorkLooper.mMessageQueue;
    }

    public static class Message {
        Message front;
        Message next;
        long time;
        long storeTime;
        public String msgString;
    }

    private static Message mHandlerMsgQue = null;

    private void initMessageQueue() {
        mHandlerMsgQue = new Message();
        mHandlerMsgQue.front = null;
        mHandlerMsgQue.next = null;
        mHandlerMsgQue.time = -1;
        mHandlerMsgQue.storeTime = -1;
        mHandlerMsgQue.msgString = "default";
    }

    private final Lock mLock = new Lock();

    void pushToMessageQueue(String msg, long delayTime) {
        synchronized (mLock) {
            // 处理非法参数
            if (msg == null) {
                return; // 或抛出异常
            }

            long currentTimeMillis = System.currentTimeMillis();
            long scheduledTime = currentTimeMillis + delayTime;

            Message msgNode = new Message();
            msgNode.msgString = msg;
            msgNode.time = scheduledTime;
            msgNode.storeTime = currentTimeMillis;

            // 如果链表为空，初始化头节点
            if (mHandlerMsgQue == null) {
                mHandlerMsgQue = msgNode;
                return;
            }

            // 查找插入位置
            Message current = mHandlerMsgQue;
            while (current.next != null) {
                if (current.time <= msgNode.time && current.next.time >= msgNode.time) {
                    // 插入中间
                    msgNode.next = current.next;
                    msgNode.front = current;
                    current.next.front = msgNode;
                    current.next = msgNode;
                    return;
                }
                current = current.next;
            }

            // 插入尾部
            current.next = msgNode;
            msgNode.front = current;
        }
    }


    boolean isQueueEmpty() {
        return false;
    }

    void sendMsgToMainThread() {
        synchronized (mLock) {
            long currentTimeMillis = System.currentTimeMillis();
            Message current = mHandlerMsgQue.next;

            while (current != null && current.time != -1) {
                if (current.time <= currentTimeMillis) {
                    Message matchedMessage = current;
                    current = current.next;

                    // Remove matchedMessage from the linked list
                    if (matchedMessage.front != null) {
                        matchedMessage.front.next = current;
                    }
                    if (current != null) {
                        current.front = matchedMessage.front;
                    }

                    // Reset message fields and handle it
                    matchedMessage.next = null;
                    matchedMessage.front = null;
                    matchedMessage.time = 0;
                    mHandler.handleMessage(matchedMessage);
                } else {
                    current = current.next;
                }
            }
        }
    }
}
