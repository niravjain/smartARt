package com.smartart.app;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutodrawAPI extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... urls) {
        String response;
        try {
            response = hitAPI(urls[0]);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }

        return response;

    }

    public String hitAPI(String jsonStr) {

        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient

            String urlAdress = "http://inputtools.google.com/request?ime=handwriting&app=autodraw&dbg=1&cs=1&oe=UTF-8";
            URL url = new URL(urlAdress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());

            os.writeBytes(jsonStr);

            os.flush();
            os.close();

            try {
                InputStream ip = new BufferedInputStream(conn.getInputStream());
                result = convertInputStreamToString(ip);
                Log.d("myLog", "RESULT: " + result);
            } finally {
                conn.disconnect();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}