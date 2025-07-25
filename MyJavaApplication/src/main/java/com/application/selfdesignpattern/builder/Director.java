package com.application.selfdesignpattern.builder;

public class Director {
    public static void conCreate() {
        /*构建的这种复杂对象里需要其他多重操作组合而成，将其构建过程和其表示进行分离*/
        //一个复杂对象
        Builder mBuilder = ConcreateBuilder.Builder
                .setProductName("product1")
                .setProductValue(10);//构建第一种该复杂对象，由产品和产品1的功能构成
        mBuilder.buildPart();

        mBuilder = new ConcreateBuilder1();//构建第二种该复杂对象，仅由产品1的功能构成
        mBuilder.buildPart();
    }
}
