package com.application.selfdesignpattern.builder;

import com.application.Log;

public class Product {
    public Product(String name, int val) {
        Log.d(getClass().getName(), "I am product " + name + "\n");
        Log.d(getClass().getName(), "I am value " + val + "\n");
    }
}
