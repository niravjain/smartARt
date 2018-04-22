package com.smartart.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Context mainContext;
    private static final String TAG = MainActivity.class.getSimpleName();

    static String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        int score = sp.getInt("score", 0);

        setContentView(R.layout.activity_main);
        mainContext = this;

        TextView highScore = findViewById(R.id.highscore);
        highScore.setText("High Score: "+score);

        Button drawButton = findViewById(R.id.drawButton);
        drawButton.setOnClickListener(drawOnCanvasListener);

        drawButton.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Log.d(TAG, "Started Main Activity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private final View.OnClickListener drawOnCanvasListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent drawCanvas = new Intent(mainContext, CanvasActivity.class);
            Log.d(TAG, "Starting Canvas Activity");
            drawCanvas.putExtra("STAGE", 0);
            mainContext.startActivity(drawCanvas);
        }
    };
}