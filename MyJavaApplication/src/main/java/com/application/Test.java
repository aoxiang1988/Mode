package com.application;

public class Test {
    public static void main(String[] arg) {
        int num = 1;
        for (int a=17; a>=0; a--) {
            num = 2*(num+1);
        }
         System.out.println(num);
    }

    boolean getFlagStatus() {
        return true;
    }

    void setFlagStatus() {
        mFlag = true;
    }

    boolean getVinStatus() {
        return true;
    }

    void connectToNet() {

    }

    boolean mFlag = false;

    void requestNetInterface() {
        mFlag = getFlagStatus();
        if (mFlag) {
            connectToNet();
        } else {
            if (getVinStatus()) {
                setFlagStatus();
                try {
                    wait(30*1000);
                    connectToNet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return;
    }
}
