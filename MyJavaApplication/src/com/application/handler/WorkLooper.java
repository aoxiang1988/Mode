package com.application.handler;

public class WorkLooper {

    private static WorkLooper mWorkLooper;
    private Thread mLooperThread = null;
    private MessageQueue mMessageQueue;

    public static WorkLooper getInstance() {
        if (mWorkLooper == null) {
            mWorkLooper = new WorkLooper();
        }
        if (mWorkLooper.mLooperThread == null || !mWorkLooper.mLooperThread.isAlive()) {
            mWorkLooper.initLooper();
        }
        return mWorkLooper;
    }

    private Runnable mMainRunner = new Runnable() {
        @Override
        public void run() {
            while (true) {
                mMessageQueue.sendMsgToMainThread();
            }
        }
    };

    private void initLooper() {
        mMessageQueue = MessageQueue.getInstance(null);
        mLooperThread = new Thread(mMainRunner);
        mLooperThread.start();
    }

}
