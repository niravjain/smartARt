package com.smartart.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

public class CanvasActivity extends AppCompatActivity {

    private Context mainContext;
    private static String objectsDrawn;

    private final View.OnClickListener finishListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "finish", Toast.LENGTH_SHORT).show();

            List<Float> xPts = CanvasView.getXPts();
            List<Float> yPts = CanvasView.getYPts();
            float height = CanvasView.getScreenHeight();
            float width = CanvasView.getScreenWidth();

            String jsonData = generateJSON(width, height, xPts, yPts);

            AutodrawAPI2 autoDraw = new AutodrawAPI2();
            autoDraw.execute(jsonData);
            CanvasView.clearPts();

        }
    };

    private final View.OnClickListener clearListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "clear", Toast.LENGTH_SHORT).show();
            CanvasView.clearPts();
            CanvasView.clearView();
        }
    };

    private String generateJSON(float width, float height, List<Float> xPts, List<Float> yPts) {

        List<Integer> x = new ArrayList<>();
        for(float f : xPts) {
            x.add((int)f);
        }

        List<Integer> y = new ArrayList<>();
        for(float f : yPts) {
            y.add((int)f);
        }

        //String json = "{ \"input_type\": 0, \"requests\": [   {     \"language\": \"autodraw\",     \"writing_guide\": {       \"width\": "+width+",       \"height\": "+height+"     },     \"ink\": [       [         "+xPts+",         "+yPts+"       ]     ]   } ]}";
        String json = "{ \"input_type\": 0, \"requests\": [   {     \"language\": \"autodraw\",     \"writing_guide\": {       \"width\": "+width+",       \"height\": "+height+"     },     \"ink\": [       [         "+x+",         "+y+"       ]     ]   } ]}";
        Log.d("json",json);

        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        mainContext = this;

        findViewById(R.id.finish).setOnClickListener(finishListener);
        findViewById(R.id.clear).setOnClickListener(clearListener);
    }

    private static class AutodrawAPI2 extends AsyncTask<String, Void, String> {

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
                    Log.d("result", result);
                } finally {
                    conn.disconnect();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;

        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        protected void onPostExecute(String response_result) {

            try {

                JSONArray jsonArray = new JSONArray(response_result);
                JSONArray lvl1 = (JSONArray)jsonArray.get(1);
                JSONArray lvl2 = (JSONArray)lvl1.get(0);
                JSONArray lvl3 = (JSONArray)lvl2.get(1);

                objectsDrawn = lvl3.toString();
                Log.d("json from inner", objectsDrawn);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
