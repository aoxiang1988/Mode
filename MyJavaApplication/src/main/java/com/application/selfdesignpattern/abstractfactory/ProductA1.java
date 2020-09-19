package com.application.selfdesignpattern.abstractfactory;

import com.application.Log;

public class ProductA1 extends AbstractProductA {

    @Override
    public void productAFunction() {
        Log.d(getClass().getName(), "ProductA1 productAFunction!!!\n");
    }
}
