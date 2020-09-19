package com.application.broadcast;

import com.application.Log;
import com.application.broadcast.intent.Intent;
import com.application.broadcast.intent.IntentFilter;
import com.application.broadcast.intent.PendingIntent;

public class Receiver {

    private static String TAG = "Receiver";
    private static IntentFilter mCurrentFilter;
    private static BroadCastReceiver mCurrentReceiver;

    public static void registerReceiver(BroadCastReceiver receiver, IntentFilter filter) {
        mCurrentFilter = filter;
        mCurrentReceiver = receiver;
    }
    public static void unregisterReceiver() {

    }

    public static void sendBroadCastReceiver(Intent intent) {
        String mCurrentAction = intent.getAction();
        mCurrentReceiver.onReceiver(mCurrentFilter.getCurrentIntent(mCurrentAction));
    }

    public static void sendBroadCastReceiver(PendingIntent intent) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasSendIntent = false;
                while (!hasSendIntent) {
                    if (intent.isNeedSendIntent()) {
                        mCurrentReceiver.onReceiver(intent);
                        hasSendIntent = true;
                    } else {
                        Log.d(TAG, "need wait!!!");
                    }
                }
            }
        });
        thread.start();
    }
}
