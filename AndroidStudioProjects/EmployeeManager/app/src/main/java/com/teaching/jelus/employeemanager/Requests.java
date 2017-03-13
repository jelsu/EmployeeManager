package com.teaching.jelus.employeemanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requests {
    public static String getRequest(URL url, String method) throws IOException {
        HttpURLConnection urlConnection;
        BufferedReader reader;
        InputStream inputStream;
        StringBuffer buffer;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.connect();
        inputStream = urlConnection.getInputStream();
        buffer = new StringBuffer();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }
}
