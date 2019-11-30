package com.example.instaliter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.instaliter.activities.CameraActivity;
import com.example.instaliter.activities.ProfileActivity;
import com.example.instaliter.activities.SearchActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PostsAdapter postsAdapter;
    ArrayList<Post> arrayList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("vykresluje sa prva screena + " + RegisterActivity.userID + " a zaroven  name: "+ RegisterActivity.userName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.list_posts);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadAllPosts();
        postsAdapter = new PostsAdapter(this,arrayList);
        recyclerView.setAdapter(postsAdapter);



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
                    case R.id.nav_search:
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
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
//        try {
//            arrayList = databaseHelper.selectAllPosts();
//            postsAdapter = new PostsAdapter(this, arrayList);
//
//            recyclerView.setAdapter(postsAdapter);
//            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    System.out.println("uz");
//                }
//            });
//
//
//            postsAdapter.notifyDataSetChanged();
//        }
//        catch (NullPointerException e){
//            System.out.println(e.getMessage());

        try {
//            arrayList = databaseHelper.selectAllPosts();
            System.out.println("naplnuje sa arraylist");
            postsAdapter.notifyDataSetChanged();
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }


        }



    }



