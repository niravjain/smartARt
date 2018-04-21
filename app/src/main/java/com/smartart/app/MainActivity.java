package com.smartart.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;



public class MainActivity extends AppCompatActivity {

    private Context mainContext;
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    public static final String FILE_NAME = "temp.jpg";

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

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
        }
    }

    private final View.OnClickListener photoOnCanvasListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Toast.makeText(mainContext, "Photo", Toast.LENGTH_SHORT).show();
            startCamera();
        }
    };

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

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

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