package com.example.instaliter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instaliter.MainActivity;
import com.example.instaliter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println(" vykresluje sa druha screena");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        imageView = (ImageView) findViewById(R.id.cameraView);
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("spusta sa fotenie");
        startActivityForResult(intent2,0);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(CameraActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_camera:
                        break;
                    case R.id.nav_profile:
                        Intent intent3 = new Intent(CameraActivity.this, ProfileActivity.class);
                        startActivity(intent3);
                        break;
                }

                return false;
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
