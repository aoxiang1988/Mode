package com.application;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPConnectionUtils {

    private static final String TAG = "HTTPConnectionUtils";
    private String mURL = "";

    private static HTTPConnectionUtils mUtils = null;

    public static HTTPConnectionUtils getUtils() {
        if (mUtils == null) {
            mUtils = new HTTPConnectionUtils();
        }
        Log.i(TAG, "context success");
        return mUtils;
    }

    public void setUrl(String url) {
        mURL = url;
    }

    public String httpConnect() {
        String result = null;
        HttpsURLConnection mHttpConnection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            URL mUrl = new URL(mURL);
            mHttpConnection = (HttpsURLConnection) mUrl.openConnection();
            mHttpConnection.setRequestMethod("POST");
            mHttpConnection.setConnectTimeout(5000);
            mHttpConnection.setReadTimeout(60000);
            mHttpConnection.connect();

            if (mHttpConnection.getResponseCode() == 200) {
                inputStream = mHttpConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            } else {
                int errorCode = mHttpConnection.getResponseCode();
                Log.w(TAG, "Connect response code : " + errorCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHttpConnection.disconnect();
        }
        return result;
    }
}
