package com.example.instaliter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instaliter.MainActivity;
import com.example.instaliter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_layout);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent1 = new Intent(ChatsActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(ChatsActivity.this, SearchActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_camera:
                        Intent intent3 = new Intent(ChatsActivity.this, CameraActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_messagges:
                        break;
                    case R.id.nav_profile:
                        Intent intent = new Intent(ChatsActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });
    }
}
