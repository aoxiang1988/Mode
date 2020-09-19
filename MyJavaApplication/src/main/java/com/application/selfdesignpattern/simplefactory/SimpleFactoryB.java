package com.application.selfdesignpattern.simplefactory;

import com.application.Log;

public class SimpleFactoryB extends SimpleFactoryBase {
    @Override
    public void function() {
        super.function();
        //TODO self function
        Log.d(getClass().getName(), "SimpleFactoryB function!!!\n");
    }
}
