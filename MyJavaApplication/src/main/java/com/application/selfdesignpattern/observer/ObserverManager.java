package com.application.selfdesignpattern.observer;


import java.util.ArrayList;

public class ObserverManager implements SubjectInterface{

    private static ObserverManager mObserverManager = null;
    private static ArrayList<ObserverInterface> mObserverList = null;

    public static ObserverManager getObserverManager() {
        if(mObserverManager == null) {
            mObserverManager = new ObserverManager();
        }

        if(mObserverList == null)
            mObserverList = new ArrayList<ObserverInterface>();

        return mObserverManager;
    }

    @Override
    public void add(ObserverInterface observer) {
        mObserverList.add(observer);
    }

    @Override
    public void notifyObserver() {
        for(ObserverInterface observer : mObserverList) {
            observer.updateFunction();
        }
    }

    @Override
    public void remove(ObserverInterface observer) {
        if(mObserverList.contains(observer))
            mObserverList.remove(observer);
    }
}
