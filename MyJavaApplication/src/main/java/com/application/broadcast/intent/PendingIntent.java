package com.application.broadcast.intent;

import java.util.Date;

public class PendingIntent extends Intent {
    String mAction;
    private long mPendingTime = 0;

    public PendingIntent(String action, long pendingTime) {
        setAction(action);
        setPendingTime(pendingTime);
    }

    public void setPendingTime(long pendingTime) {
        mPendingTime = pendingTime;
    }

    public long getPendingTime() {
        return mPendingTime;
    }
}
