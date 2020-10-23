package com.application.selfdesignpattern.observer;

import com.application.Log;

public class SubServer {

    private static ObserverManager mObserverManager = null;
    private static SubServer mSubServer = null;

    public static SubServer getSubServer() {
        if(mSubServer == null) {
            mSubServer = new SubServer();
            mObserverManager = ObserverManager.getObserverManager();
        }
        return mSubServer;
    }

    public void serverNotifyObserver() {
        Log.d(getClass().getName(), "SubServer serverNotifyObserver!!!\n");
        mObserverManager.notifyObserver();
    }
}
