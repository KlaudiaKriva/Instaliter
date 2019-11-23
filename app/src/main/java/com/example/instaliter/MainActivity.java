package com.example.instaliter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.instaliter.activities.CameraActivity;
import com.example.instaliter.activities.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("vykresluje sa prva screena");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        break;
                    case R.id.nav_camera:
                            Intent intent2 = new Intent(MainActivity.this, CameraActivity.class);
                            startActivity(intent2);
                            break;
                    case R.id.nav_profile:
                            Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent3);
                            break;
                }

                return false;
            }
        });
    }


}
