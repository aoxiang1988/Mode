package com.application.selfdesignpattern.abstractfactory;

import com.application.Log;

public class ProductB2 extends AbstractProductB {

    @Override
    public void productBFunction() {
        Log.d(getClass().getName(), "ProductB2 productBFunction!!!\n");
    }
}
