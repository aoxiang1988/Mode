package com.application.selfdesignpattern.builder;

public class ConcreateBuilder1 extends Builder{
    @Override
    public void buildPart() {
        getResult1();
    }
    private Product1 getResult1() {
        return new Product1();
    }
}
