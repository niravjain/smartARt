package com.smartart.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

public class CanvasActivity extends AppCompatActivity {

    private Context mainContext;

    
    
    private final View.OnClickListener finishListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "finish", Toast.LENGTH_SHORT).show();

            List<Float> xPts = CanvasView.getXPts();
            List<Float> yPts = CanvasView.getYPts();
            float height = CanvasView.getScreenHeight();
            float width = CanvasView.getScreenWidth();

            String jsonData = generateJSON(width, height, xPts, yPts);

            AutodrawAPI autoDraw = new AutodrawAPI();
            autoDraw.execute(jsonData);

        }
    };

    //private String generateJSON(float width, float height, List<Float> xPts, List<Float> yPts) {
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
    }

}
