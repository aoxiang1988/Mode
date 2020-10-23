package com.application.selfdesignpattern.builder;

public class ConcreateBuilder1 extends Builder{
    @Override
    public void buildPart() {
        getResult1();
    }

    @Override
    public Builder setProductName(String productName) {
        return null;
    }

    @Override
    public Builder setProductValue(int value) {
        return null;
    }

    private Product1 getResult1() {
        return new Product1();
    }
}
