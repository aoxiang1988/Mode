package com.application.selfdesignpattern.builder;

import com.application.Log;

public class Product {
    public Product(String name, int val) {
        Log.i(getClass().getName(), "I am product " + name + "\n");
        Log.i(getClass().getName(), "I am value " + val + "\n");
    }
}
