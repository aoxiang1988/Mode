package com.application.selfdesignpattern.abstractfactory;

import com.application.Log;

public class ProductB1 extends AbstractProductB {

    @Override
    public void productBFunction(){
        Log.d(getClass().getName(), "ProductB1 productBFunction!!!\n");
    }
}
