package com.application.handler;

import com.application.Log;

public class WorkLooper {

    public static MessageQueue mMessageQueue;
    private static WorkLooper mWorkLooper;
    private Thread mLooperThread = null;
    private static String TAG = "WorkLooper";

    public static void getInstance(Handler handler) {
        if (mWorkLooper == null) {
            mWorkLooper = new WorkLooper();
        }
        if (mWorkLooper.mLooperThread == null || !mWorkLooper.mLooperThread.isAlive()) {
            Log.i(TAG, "initLooper handler");
            mWorkLooper.initLooper(handler);
        }
    }

    private final Runnable mMainRunner = new Runnable() {
        @Override
        public void run() {
            while (true) {
                mMessageQueue.sendMsgToMainThread();
            }
        }
    };

    private void initLooper(Handler handler) {
        mMessageQueue = MessageQueue.getInstance(handler);
        mLooperThread = new Thread(mMainRunner);
        mLooperThread.start();
    }

}
