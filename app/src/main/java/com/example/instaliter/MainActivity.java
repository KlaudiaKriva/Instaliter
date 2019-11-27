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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.instaliter.activities.CameraActivity;
import com.example.instaliter.activities.ProfileActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PostsAdapter postsAdapter;
    ArrayList<Post> arrayList;
    DatabaseHelper databaseHelper;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("vykresluje sa prva screena");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        arrayList = new ArrayList<>();
        listView = findViewById(R.id.list_posts);

        loadAllPosts();

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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

    public void loadAllPosts() throws NullPointerException{
        try {
            arrayList = databaseHelper.selectAllPosts();
            postsAdapter = new PostsAdapter(this, arrayList);

            listView.setAdapter(postsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("uz");
                }
            });


            postsAdapter.notifyDataSetChanged();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }


}
