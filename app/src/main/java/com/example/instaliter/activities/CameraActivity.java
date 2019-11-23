package com.example.instaliter.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.instaliter.MainActivity;
import com.example.instaliter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println(" vykresluje sa druha screena");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        imageView = findViewById(R.id.cameraView);
        button = findViewById(R.id.back_button);

        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("spusta sa fotenie");
        startActivityForResult(intent2,0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }
}

