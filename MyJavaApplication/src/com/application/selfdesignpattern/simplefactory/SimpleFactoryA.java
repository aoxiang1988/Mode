package com.application.selfdesignpattern.simplefactory;

import com.application.Log;

public class SimpleFactoryA extends SimpleFactoryBase {
    @Override
    public void function() {
        super.function();
        //TODO self function
        Log.d(getClass().getName(), "SimpleFactoryA function!!!\n");
    }
}
