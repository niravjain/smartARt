package com.smartart.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Context mainContext;
    private static final String TAG = MainActivity.class.getSimpleName();

    static String MY_PREFS_NAME = "MyPrefsFile";

//    Context context = getActivity();
//    SharedPreferences sharedPref = context.getSharedPreferences(
//            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("Score", 0);
//        editor.commit();

        editor.putString("name", "Elena");
        editor.putInt("idName", 12);
        editor.apply();

        setContentView(R.layout.activity_main);
        mainContext = this;

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
            String restoredText = prefs.getString("text", null);
            if (restoredText != null) {
                String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                int idName = prefs.getInt("idName", 0); //0 is the default value.
                Log.d("name",name);
                Log.d("idName",idName);
            }

            Toast.makeText(mainContext, "Draw", Toast.LENGTH_SHORT).show();
            Intent drawCanvas = new Intent(mainContext, CanvasActivity.class);
            Log.d(TAG, "Starting Canvas Activity");
            mainContext.startActivity(drawCanvas);
        }
    };
}