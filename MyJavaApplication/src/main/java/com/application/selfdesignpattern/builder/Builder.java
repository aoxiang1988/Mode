package com.application.selfdesignpattern.builder;

public abstract class Builder {
    public abstract void buildPart();
    public abstract Builder setProductName(String productName);
    public abstract Builder setProductValue(int value);
}
