package com.application.selfdesignpattern.builder;

public class ConcreateBuilder extends Builder {
    @Override
    public void buildPart() {
        getResult();
        getResult1();
    }
    private Product getResult() {
        return new Product();
    }

    private Product1 getResult1() {
        return new Product1();
    }
}
