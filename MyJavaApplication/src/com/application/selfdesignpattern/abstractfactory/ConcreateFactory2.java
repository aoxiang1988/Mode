package com.application.selfdesignpattern.abstractfactory;

public class ConcreateFactory2 extends AbstractFactory {
    @Override
    AbstractProductA createProductA(int type) {
        return null;
    }

    @Override
    AbstractProductB createProductB(int type) {
        if(type == DefaultValue.PRODUCT_B_1)
            return new ProductB1();
        if(type == DefaultValue.PRODUCT_B_2)
            return new ProductB2();
        return null;
    }
}
