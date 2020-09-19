package com.application.broadcast.intent;

public class Intent {

    String mAction;

    public Intent() {

    }
    public Intent(String action) {
        setAction(action);
    }

    void setAction(String action) {
        mAction = action;
    }

    public String getAction() {
        return mAction;
    }
}
