package com.example.instaliter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaliter.MainActivity;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;
import org.json.JSONException;

import java.util.ArrayList;

public class ProfileOfOthersActivity extends AppCompatActivity {

    PostsAdapter postsAdapter;
    RecyclerView recyclerView;
    TextView profile_number_of_posts;
    TextView profile_username;
    ImageView imageView;
    ArrayList<Post> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_others);

        profile_number_of_posts = findViewById(R.id.profile_number_of_posts);
        profile_username = findViewById(R.id.profile_username);
        imageView = findViewById(R.id.profile_picture);
        recyclerView = findViewById(R.id.myPosts);

        try {
            getUserInfo();
            getUserPosts();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        postsAdapter = new PostsAdapter(this, arrayList);
        recyclerView.setAdapter(postsAdapter);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent1 = new Intent(ProfileOfOthersActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_camera:
                        Intent intent3 = new Intent(ProfileOfOthersActivity.this, CameraActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_search:
                        Intent intent = new Intent(ProfileOfOthersActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        Intent intent4 = new Intent(ProfileOfOthersActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }

    public void getUserInfo() throws JSONException {


    }

    public void getUserPosts() throws JSONException{

    }

    public void followUser(View view){
        //serversingleton
    }
}
