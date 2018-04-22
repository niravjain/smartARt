package com.smartart.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasActivity extends AppCompatActivity {

    private static Context mainContext;
    private static String objectsDrawn;
    Map<String,Boolean> topics = new HashMap<>();

    private static void initializeTopics{

        topics.put("apple",false);
        topics.put("chair",false);
        topics.put("car",false);
        topics.put("fish",false);

    }


    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    public static final String FILE_NAME = "temp.jpg";

    private final View.OnClickListener finishListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
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

    private final View.OnClickListener exploreListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "Photo", Toast.LENGTH_SHORT).show();
            startCamera();
        }
    };

    public void startCamera() {
        if (PermissionUtils.requestPermission(
            this,
            CAMERA_PERMISSIONS_REQUEST,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            Intent uploadImage = new Intent(this, ImageUploadActivity.class);
            uploadImage.putExtra("PHOTO_URI", photoUri.toString());
            mainContext.startActivity(uploadImage);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

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
        findViewById(R.id.explore).setOnClickListener(exploreListener);
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

                objectsDrawn = lvl3.toString().replace("[","")
                        .replace("]","")
                        .replace("\"","")
                        .toLowerCase();
                Log.d("In post exec", objectsDrawn);
                String[] results = objectsDrawn.split(",");

                boolean found = false;
                String final_result = "";
                String keySet = "apple chair fish car";
                for(int idx = 0 ; idx < results.length ; idx++){
                    if(keySet.contains(results[idx])){
                        found = true;
                        final_result = results[idx];
                        break;
                    }
                }

                if(found){
                    Intent uploadImage = new Intent(mainContext, ObjRender.class);
                    uploadImage.putExtra("RESULT", final_result);
                    Log.d("Sending result", final_result);
                    Toast.makeText(mainContext, "Opening a 3D render of " + final_result,Toast.LENGTH_LONG).show();

                    mainContext.startActivity(uploadImage);

                } else {

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
