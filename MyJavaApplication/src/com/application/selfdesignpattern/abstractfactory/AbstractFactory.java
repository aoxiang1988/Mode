package com.application.selfdesignpattern.abstractfactory;

public abstract class AbstractFactory {
    abstract AbstractProductA createProductA(int type);
    abstract AbstractProductB createProductB(int type);
}
