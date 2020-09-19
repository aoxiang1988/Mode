package com.application.broadcast.intent;

import java.util.Date;

public class PendingIntent extends Intent {
    String mAction;
    private long mPendingTime = 0;

    public PendingIntent() {

    }

    public PendingIntent(String action) {
        setAction(action);
    }

    public PendingIntent(String action, long pendingTime) {
        setAction(action);
        setPendingTime(pendingTime);
    }

    public void setPendingTime(long pendingTime) {
        Date currentTime = new Date();
        this.mPendingTime = currentTime.getTime() + pendingTime;
    }

    public boolean isNeedSendIntent() {
        Date currentTime = new Date();
        return currentTime.getTime() == mPendingTime;
    }
}
