package com.smartart.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class FinalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        int score = sp.getInt("score", 0);
        TextView fscore = findViewById(R.id.score);
        fscore.setText(score);
    }
}
