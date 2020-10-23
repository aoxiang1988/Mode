package com.application.selfdesignpattern.abstractfactory;

public class ConcreateFactory1 extends AbstractFactory{
    @Override
    AbstractProductA createProductA(int type) {
        if(type == DefaultValue.PRODUCT_A_1)
            return new ProductA1();
        if(type == DefaultValue.PRODUCT_A_2)
            return new ProductA2();
        return null;
    }

    @Override
    AbstractProductB createProductB(int type) {
        return null;
    }
}
