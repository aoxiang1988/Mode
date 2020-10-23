package com.application.selfdesignpattern.builder;

public class ConcreateBuilder extends Builder {

    public static ConcreateBuilder Builder = new ConcreateBuilder();

    @Override
    public void buildPart() {
        getResult();
        getResult1();
    }

    private String mProductName;

    @Override
    public Builder setProductName(String productName) {
        mProductName = productName;
        return Builder;
    }

    private int mValue;

    @Override
    public Builder setProductValue(int value) {
        mValue = value;
        return Builder;
    }

    private Product getResult() {
        return new Product(mProductName, mValue);
    }

    private Product1 getResult1() {
        return new Product1();
    }
}
