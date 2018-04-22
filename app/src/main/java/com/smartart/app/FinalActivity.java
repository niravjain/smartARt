package com.smartart.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class FinalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        SharedPreferences sp = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        int currScore = sp.getInt("score", 0);
        int highScore = sp.getInt("highScore", 0);

        if(highScore<currScore) {
            SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt("highScore", currScore);
            editor.apply();
        }

        TextView fscore = findViewById(R.id.score);
        fscore.setText(""+currScore);
    }

}
