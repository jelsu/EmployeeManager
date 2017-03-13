package com.teaching.jelus.employeemanager;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class RequestTask implements Runnable {
    public static final String TAG = RequestTask.class.getSimpleName();
    private static final String FEDIA_EMAIL = "fedia123@gmail.com";

    @Override
    public void run() {
        URL url = null;
        try {
            url = buildUrl(FEDIA_EMAIL);
            String request = Requests.getRequest(url, "GET");
            Log.d(TAG, "run: \n" + request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private URL buildUrl(String content) throws MalformedURLException {
        final String BASE_URL = "http://192.168.1.2:8000/employee/";
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_URL);
        builder.append(content);
        return new URL(builder.toString());
    }
}
