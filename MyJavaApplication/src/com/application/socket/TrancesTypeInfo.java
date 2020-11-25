package com.application.socket;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class TrancesTypeInfo {

    public static final String FILE_TYPE = "file";
    public static final String STRING_TYPE = "string";

    Object init(String type, byte[] bytes, int offset, int length, String charsetName) {
        if (STRING_TYPE.equals(type)) {
            try {
                return new String(bytes, offset, length, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
