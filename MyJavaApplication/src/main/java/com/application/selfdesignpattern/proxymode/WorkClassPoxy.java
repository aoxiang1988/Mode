package com.application.selfdesignpattern.proxymode;

public class WorkClassPoxy implements BaseFunctionInterface {

    private WorkClass mWorkClass;

    public WorkClassPoxy() {
        mWorkClass = new WorkClass();
    }

    @Override
    public void doSomeThing() {
        mWorkClass.doSomeThing();
    }
}
