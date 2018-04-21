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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClients;

public class AutodrawAPI extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... urls) {
        String text;
        try {
            text = getText1();
        } catch (Exception e) {
            this.exception = e;
            return null;
        }

        return text;

    }

    public String getText1() {
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

            String jsonStr = "{\"input_type\":0,\"requests\":[{\"language\":\"autodraw\",\"writing_guide\":{\"width\":350,\"height\":350},\"ink\":[[[0,146,103,97,92,87,85,98,124,151,187,202,213,220,247,258,265,271,273,274,271,268,264,261,257,253,250,247,245,242,240,238,236,233,227,213,187,185,181,178,177,175,174,173,175,177,179,180,181,182,183,184,183,177,170,167,164,161,159,157,156,154,151,149,147],[0,91,94,106,119,132,145,192,224,248,275,283,290,292,286,281,276,271,268,258,99,90,83,79,74,70,67,64,61,58,57,55,54,53,52,51,52,53,55,56,57,58,59,60,90,95,99,103,105,106,107,108,109,108,105,104,103,101,100,99,98,97,96,95,94],[10,173,362,378,396,415,433,469,479,495,514,531,550,568,612,630,649,666,685,704,819,829,847,864,882,900,919,929,945,964,982,1000,1018,1035,1062,1134,1314,1351,1377,1396,1414,1432,1467,1486,1611,1629,1647,1666,1683,1702,1728,1765,1863,1917,1936,1945,1963,1981,1998,2016,2034,2061,2152,2233,2324]]]}]}";

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());

            os.writeBytes(jsonStr);

            os.flush();
            os.close();

            Log.i("STATUS: ", String.valueOf(conn.getResponseCode()));
            Log.i("MSG: " , conn.getResponseMessage());

            try {
                InputStream ip = new BufferedInputStream(conn.getInputStream());
                result = convertInputStreamToString(ip);
                Log.d("myLog", "result: " + result);
            } finally {
                conn.disconnect();
            }

//
//            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
//            // ObjectMapper mapper = new ObjectMapper();
//            // json = mapper.writeValueAsString(person);
//
//            // 5. set json to StringEntity
//            StringEntity se = new StringEntity(json);
//
//            // 6. set httpPost Entity
//            httpPost.setEntity(se);
//
//            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            // 8. Execute POST request to the given URL
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if (inputStream != null) {
//                result = convertInputStreamToString(inputStream);
//                Log.d("myLog", "result: " + result);
//            } else {
//                result = "Did not work!";
//            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;

    }
    public String getText2() {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            //HttpURLConnection httpclient = new HttpURLConnection();
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            String url = "http://inputtools.google.com/request?ime=handwriting&app=autodraw&dbg=1&cs=1&oe=UTF-8";

            HttpPost httpPost = new HttpPost(url);
//
//            String json = "";
//
//            // 3. build jsonObject
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("name", person.getName());
//            jsonObject.accumulate("country", person.getCountry());
//            jsonObject.accumulate("twitter", person.getTwitter());
//
//            // 4. convert JSONObject to JSON to String
//            json = jsonObject.toString();
            String json = "{\"input_type\":0,\"requests\":[{\"language\":\"autodraw\",\"writing_guide\":{\"width\":350,\"height\":350},\"ink\":[[[0,146,103,97,92,87,85,98,124,151,187,202,213,220,247,258,265,271,273,274,271,268,264,261,257,253,250,247,245,242,240,238,236,233,227,213,187,185,181,178,177,175,174,173,175,177,179,180,181,182,183,184,183,177,170,167,164,161,159,157,156,154,151,149,147],[0,91,94,106,119,132,145,192,224,248,275,283,290,292,286,281,276,271,268,258,99,90,83,79,74,70,67,64,61,58,57,55,54,53,52,51,52,53,55,56,57,58,59,60,90,95,99,103,105,106,107,108,109,108,105,104,103,101,100,99,98,97,96,95,94],[10,173,362,378,396,415,433,469,479,495,514,531,550,568,612,630,649,666,685,704,819,829,847,864,882,900,919,929,945,964,982,1000,1018,1035,1062,1134,1314,1351,1377,1396,1414,1432,1467,1486,1611,1629,1647,1666,1683,1702,1728,1765,1863,1917,1936,1945,1963,1981,1998,2016,2034,2061,2152,2233,2324]]]}]}";

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.d("myLog", "result: " + result);
            } else {
                result = "Did not work!";
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