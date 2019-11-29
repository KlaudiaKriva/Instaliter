package com.example.instaliter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.MainActivity;
import com.example.instaliter.R;
import com.example.instaliter.User;
import com.example.instaliter.adapters.UsersAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ArrayList<User> arrayList = new ArrayList<>();
    UsersAdapter adapter;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.topPanel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Search");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.users);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadAllUsers();
        adapter = new UsersAdapter(arrayList);
        recyclerView.setAdapter(adapter);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent1 = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_camera:
                        Intent intent3 = new Intent(SearchActivity.this, CameraActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_search:
                        break;
                    case R.id.nav_profile:
                        Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void loadAllUsers() throws NullPointerException{
        try {
            arrayList = databaseHelper.selectAllUsers();
            System.out.println("naplnuje sa arraylist");
            adapter.notifyDataSetChanged();
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

//    private void setUpRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.myPosts);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        adapter = new UsersAdapter(arrayList);
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//    }
}
