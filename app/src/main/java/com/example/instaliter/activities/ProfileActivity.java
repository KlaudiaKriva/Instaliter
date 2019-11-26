package com.example.instaliter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.LoginActivity;
import com.example.instaliter.MainActivity;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView profile_username;
    PostsAdapter postsAdapter;
    ArrayList<Post> arrayList;
    DatabaseHelper databaseHelper;
    ListView listView;
    TextView profile_number_of_posts;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        profile_number_of_posts = findViewById(R.id.profile_number_of_posts);

        databaseHelper = new DatabaseHelper(this);
        arrayList = new ArrayList<>();
        profile_username = findViewById(R.id.profile_username);
        profile_username.setText(databaseHelper.selectUserNameFromID((int)RegisterActivity.userID));
        listView = findViewById(R.id.myPosts);
        imageView = findViewById(R.id.profile_picture);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });


        loadDataInListview();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_camera:
                        Intent intent3 = new Intent(ProfileActivity.this, CameraActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_profile:
                        break;
                }

                return false;
            }
        });
    }


    public void loadDataInListview() throws NullPointerException{
        try {
            arrayList = databaseHelper.selectMyPosts();
            postsAdapter = new PostsAdapter(this, arrayList);

            listView.setAdapter(postsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("a");
                }
            });

            profile_number_of_posts.setText(String.valueOf(arrayList.size()));
            postsAdapter.notifyDataSetChanged();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    public void likePost(final View view){
        int position = listView.getPositionForView(view);
        Post post = (Post) postsAdapter.getItem(position);

        CheckBox heart = findViewById(R.id.heart);


//        heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("klikkkk");
//                view.setBackground(getResources().getDrawable(R.drawable.icon_heart2));
//            }
//        });




    }

    public void logout(View view){
        Intent intent_lg = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent_lg);
    }
}
