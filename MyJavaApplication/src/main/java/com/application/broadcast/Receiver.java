package com.application.broadcast;

import com.application.Log;
import com.application.broadcast.intent.Intent;
import com.application.broadcast.intent.IntentFilter;
import com.application.broadcast.intent.PendingIntent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver {

    private static final String TAG = "Receiver";
    private static IntentFilter mCurrentFilter;
    private static final int MAX_RECEIVERS = 100;
    private static BroadCastReceiver[] mCurrentReceiver = new BroadCastReceiver[MAX_RECEIVERS];

    public static synchronized void registerReceiver(BroadCastReceiver receiver, IntentFilter filter) {
        if (receiver == null || filter == null) {
            throw new IllegalArgumentException("Receiver and filter cannot be null.");
        }

        if (mCurrentReceiver != null && mCurrentReceiver.equals(receiver)) {
            Log.w(TAG, "Warning: This receiver is already registered.");
            return;
        }

        // 初始化 mCurrentReceiver 数组
        if (mCurrentReceiver == null) {
            mCurrentReceiver = new BroadCastReceiver[MAX_RECEIVERS];
        }

        mCurrentFilter = filter;

        for (int i = 0; i < MAX_RECEIVERS; i++) {
            if (mCurrentReceiver[i] == null) {
                mCurrentReceiver[i] = receiver;
                break; // 找到空位后插入并退出循环
            }
        }
    }


    /**
     * 安全地注销广播接收器。
     * 注意：确保在适当生命周期中调用此方法，避免重复注销或空指针异常。
     */
    public static void unregisterReceiver() {
         //if (isRegistered && someReceiver != null) {
         //    context.unregisterReceiver(someReceiver);
         //    isRegistered = false;
         //}
    }


    public static void sendBroadCastReceiver(Intent intent) {
        if (intent == null) {
            // 可选：添加日志
            // Log.e("Broadcast", "Received null intent");
            return;
        }

        String action = intent.getAction();
        if (action == null) {
            // 可选：添加日志
            // Log.w("Broadcast", "Intent action is null");
            return;
        }

        for (int i = 0; i < 100; i++) {
            BroadCastReceiver receiver = mCurrentReceiver[i];
            IntentFilter filter = mCurrentFilter;

            if (receiver == null || filter == null) {
                // 可选：添加日志
                // Log.e("Broadcast", "Receiver or Filter is null");
                return;
            }

            Intent filteredIntent = filter.getCurrentIntent(action);
            receiver.onReceiver(filteredIntent);
        }
    }

    public static void sendBroadCastReceiver(final PendingIntent intent) {
        if (intent == null) {
            // Log.e("Broadcast", "Received null intent");
            return;
        }

        final String action = intent.getAction();
        if (action == null) {
            // Log.w("Broadcast", "Intent action is null");
            return;
        }

        long delayMillis = intent.getPendingTime();
        if (delayMillis < 0) {
            // Log.e("Broadcast", "Invalid pending time: " + delayMillis);
            return;
        }

        // 使用线程池代替每次都 new Thread
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "BroadcastSenderThread");
            t.setDaemon(true); // 设置为守护线程
            return t;
        });

        executor.submit(() -> {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                // Log.e("Broadcast", "Thread interrupted during sleep", e);
                return;
            }

            BroadCastReceiver[] receivers = mCurrentReceiver; // 假设是 volatile 或 synchronized 获取
            IntentFilter filter = mCurrentFilter; // 同上

            if (receivers == null || filter == null) {
                // Log.e("Broadcast", "Receivers or Filter is null");
                return;
            }

            Intent filteredIntent = filter.getCurrentIntent(action);
            if (filteredIntent == null) {
                // Log.e("Broadcast", "Filtered intent is null for action: " + action);
                return;
            }

            for (int i = 0; i < receivers.length; i++) {
                BroadCastReceiver receiver = receivers[i];
                if (receiver == null) {
                    // Log.e("Broadcast", "Receiver at index " + i + " is null");
                    continue;
                }

                try {
                    receiver.onReceiver(filteredIntent);
                } catch (Exception e) {
                    // Log.e("Broadcast", "Error delivering broadcast to receiver at index " + i, e);
                }
            }
        });

        executor.shutdown();
    }

}
