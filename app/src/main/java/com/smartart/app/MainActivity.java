package com.smartart.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;


public class MainActivity extends AppCompatActivity {

    private Context mainContext;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private final View.OnClickListener drawOnCanvasListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "Draw", Toast.LENGTH_SHORT).show();

            CanvasView myCanvasView;

            // No XML file; just one custom view created programmatically.
            myCanvasView = new CanvasView(mainContext);

            // Request the full available screen for layout.
            myCanvasView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
            setContentView(myCanvasView);
        }
    };

    private final View.OnClickListener photoOnCanvasListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "Photo", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainContext = this;

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.drawButton).setOnClickListener(drawOnCanvasListener);
        findViewById(R.id.photoButton).setOnClickListener(photoOnCanvasListener);
    }
}