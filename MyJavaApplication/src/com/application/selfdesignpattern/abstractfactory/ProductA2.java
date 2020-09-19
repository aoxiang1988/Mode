package com.application.selfdesignpattern.abstractfactory;

import com.application.Log;

public class ProductA2 extends AbstractProductA {
    @Override
    public void productAFunction() {
        Log.d(getClass().getName(), "ProductA2 productAFunction!!!\n");
    }
}
