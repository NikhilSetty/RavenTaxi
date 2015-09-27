package com.diemen.olaoff.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static final String TAG = "HttpUtils";




    public static boolean isConnectedToInternet() {
        try {
            //Connect
            HttpURLConnection urlConnection = (HttpURLConnection) ((new URL("http://www.google.com").openConnection()));
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.connect();
            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            String result = sb.toString();
            if (result != null) {
                return true;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


}
