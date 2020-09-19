package com.application.testcode;

public class TestCodeClass {

    /*IP check*/
    private static final String IPV4 = "IPv4";
    private static final String IPV6 = "IPv6";
    private static final String NEITHER = "Neither";

    public static String validIPAddress(String IP) {
        String mResult = NEITHER;
        if(IP.contains(".")) { // IPV4
            String[] ipCode = IP.split(".");
            for(String key : ipCode) {
                if(key.startsWith("0") && key.length() > 1) {
                    mResult = NEITHER;
                    break;
                } else {
                    char[] t = key.toCharArray();
                }
            }
        } else if(IP.contains(":")) { // IPV6

        }
        return mResult;
    }
}
