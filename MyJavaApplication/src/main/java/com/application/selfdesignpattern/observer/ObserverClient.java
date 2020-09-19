package com.application.selfdesignpattern.observer;

import com.application.Log;

public class ObserverClient implements ObserverInterface{

    private static ObserverManager mObserverManager = null;
    private static ObserverClient mObserverClient = null;

    public static ObserverClient getObserverClient() {
        if(mObserverClient == null) {
            mObserverClient = new ObserverClient();
            mObserverManager = ObserverManager.getObserverManager();
            mObserverManager.add(mObserverClient);
        }
        return mObserverClient;
    }

    public void StopObserverClient() {
        if(mObserverClient != null) {
            mObserverManager.remove(mObserverClient);
            mObserverClient = null;
        }
    }

    @Override
    public void updateFunction() {
        //TODO something
        Log.d(getClass().getName(), "ObserverClient updateFunction!!!\n");
    }
}
