package com.example.instaliter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.LoginActivity;
import com.example.instaliter.MainActivity;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

public class ProfileActivity extends AppCompatActivity {

    PostsAdapter postsAdapter;

    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    Button button;

    TextView profile_number_of_posts;
    TextView profile_username;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        profile_number_of_posts = findViewById(R.id.profile_number_of_posts);
        profile_username = findViewById(R.id.profile_username);
        imageView = findViewById(R.id.profile_picture);
        recyclerView = findViewById(R.id.myPosts);
        button = findViewById(R.id.editProfile);

        try {
            getUserInfo();
            getUserPosts();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        RegisterActivity.userName = profile_username.getText().toString();
//        profile_username.setText(String.valueOf(RegisterActivity.userID));
//
//        profile_username.setText(databaseHelper.selectUserNameFromID((int)RegisterActivity.userID));
//        listView = findViewById(R.id.myPosts);
//        databaseHelper = new DatabaseHelper(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//
//        loadDataInListview();
        postsAdapter = new PostsAdapter(this,arrayList);
        recyclerView.setAdapter(postsAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

//        loadDataInListview();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent1 = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_camera:
                        Intent intent3 = new Intent(ProfileActivity.this, CameraActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_search:
                        Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        break;
                }

                return false;
            }
        });
    }

    Map<String, String> responseMap;
    String username="";
    String instaname="";
    String email= "";
    String profileDescription="";
    String idI="";
    String imagePath="";
    String imageDate= "";
    String thumbnailPath="";
    String description;


    public void getUserInfo() throws JSONException {
        System.out.println("tahaju sa data zo servera o userovi");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));

            RequestQueue queue = Volley.newRequestQueue(this);
            String registerurl = "http://192.168.0.101:5005/userInfo";

            responseMap = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    registerurl, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            try {
                                for (int i = 0; i< response1.length(); i++){
                                    response = response1.getJSONObject(i);
                                    System.out.println("response spravy uz "+response);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // tento response je [{object}]
                            System.out.println("response userInfo "+response);
                            try {
                                //tu si mala velke pismena Name, InstaName- taky response nepride, pridu male pismenka
                                username = response.getString("name");
                                instaname = response.getString("instaName");
                                email = response.getString("email");
                                profileDescription = response.getString("profileDescription");
//                                idI = Integer.parseInt(response.getString("idI"));
                                //toto je zakomentovane, lebo je tam exception, int nemoze byt null, zaroven som zmenila idI hroe na "" a nie na int=0
                                idI = response.getString("idI");
                                imagePath = response.getString("imagePath");
                                imageDate = response.getString("imageDate");
                                thumbnailPath = response.getString("thumbnailPath");
                                description= response.getString("description");

                                responseMap.put("id", String.valueOf(userID));
                                responseMap.put("name", username);
                                responseMap.put("instaName", instaname);
                                responseMap.put("email", email);
                                responseMap.put("profileDescription", profileDescription);
                                responseMap.put("idI", String.valueOf(idI));
                                responseMap.put("imagePath", imagePath);
                                responseMap.put("imageDate", imageDate);
                                responseMap.put("thumbnailPath", thumbnailPath);
                                responseMap.put("description", description);

                                if (!responseMap.isEmpty()){
                                    Toast.makeText(getBaseContext(), "User authentificed",Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                    RegisterActivity.userName = username;
                                    profile_username.setText(username);
                                    System.out.println("profile activity som "+ RegisterActivity.userID);
                                }
                                else {
                                    Toast.makeText(getBaseContext(), "User not authentificed",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramas = new HashMap<String, String>();
                    paramas.put("id", String.valueOf(userID));
                    paramas.put("name", username);
                    paramas.put("instaName", instaname);
                    paramas.put("email", email);
                    paramas.put("profileDescription", profileDescription);
                    paramas.put("idI", String.valueOf(idI));
                    paramas.put("imagePath", imagePath);
                    paramas.put("imageDate", imageDate);
                    paramas.put("description", description);
                    return paramas;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers= new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };



            queue.add(jsonArrayRequest);
        } else {
            System.out.println("token je prazdny "+token);
        }
    }

    Map<String, String> responseMapPosts;
    String idI_posts="";
    String path_posts="";
    String thumbnailPath_posts="";
    String description_posts="";
    String date_posts;
    ArrayList<Post> arrayList = new ArrayList<>();



    public void getUserPosts() throws JSONException {
        System.out.println("tahaju sa posty usera zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));

            RequestQueue queue = Volley.newRequestQueue(this);
            String registerurl = "http://192.168.0.101:5005/getUserImages";

            responseMapPosts = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    registerurl, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            try {
                                for (int i = 0; i< response1.length(); i++){
                                    response = response1.getJSONObject(i);
                                    System.out.println("response spravny uz "+response);
//                                idI = Integer.parseInt(response.getString("idI"));
                                    //toto je zakomentovane, lebo je tam exception, int nemoze byt null, zaroven som zmenila idI hroe na "" a nie na int=0
                                    idI_posts = response.getString("idI");
                                    path_posts = response.getString("path");
                                    path_posts =path_posts.substring(0,6)+"/"+path_posts.substring(7);
                                    System.out.println("image cesta "+path_posts);
                                    thumbnailPath_posts = response.getString("thumbnailPath");
                                    thumbnailPath_posts =thumbnailPath_posts.substring(0,10)+"/"+thumbnailPath_posts.substring(11);
                                    System.out.println("image cesta "+thumbnailPath_posts);
                                    description_posts= response.getString("description");
                                    date_posts = response.getString("imageDate");

                                    responseMapPosts.put("id", String.valueOf(userID));
                                    responseMapPosts.put("idI", String.valueOf(idI_posts));
                                    responseMapPosts.put("imagePath", path_posts);
                                    responseMapPosts.put("thumbnailPath", thumbnailPath_posts);
                                    responseMapPosts.put("description", description_posts);
                                    responseMapPosts.put("imageDate", date_posts);

                                    Post post = new Post( (int) RegisterActivity.userID, idI_posts , path_posts, thumbnailPath_posts, description_posts,date_posts);
                                    arrayList.add(post);
                                    postsAdapter.notifyDataSetChanged();

                                    if (!responseMapPosts.isEmpty()){
                                        Toast.makeText(getBaseContext(), "User posts loaded",Toast.LENGTH_LONG).show();
                                        System.out.println("profile activity som "+ RegisterActivity.userID);
                                    }
                                    else {
                                        Toast.makeText(getBaseContext(), "User posts not loaded",Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramas = new HashMap<String, String>();
                    paramas.put("id", String.valueOf(userID));
                    paramas.put("idI", String.valueOf(idI_posts));
                    paramas.put("imagePath", path_posts);
                    paramas.put("thumbnailPath", thumbnailPath_posts);
                    paramas.put("description", description_posts);
                    paramas.put("imageDate", date_posts);
                    return paramas;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers= new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            queue.add(jsonArrayRequest);

        } else {
            System.out.println("token je prazdny "+token);
        }
    }







//    public void loadDataInListview() throws NullPointerException {
////        try {
////            arrayList = databaseHelper.selectMyPosts();
////            postsAdapter = new PostsAdapter(this, arrayList);
////
////            listView.setAdapter(postsAdapter);
////            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    System.out.println("a");
////                }
////            });
////
////            profile_number_of_posts.setText(String.valueOf(arrayList.size()));
////            postsAdapter.notifyDataSetChanged();
////        } catch (NullPointerException e) {
////            System.out.println(e.getMessage());
//        try {
//            arrayList = databaseHelper.selectMyPosts();
//            System.out.println("naplnuje sa arr list na profile");
//            profile_number_of_posts.setText(String.valueOf(arrayList.size()));
//            postsAdapter.notifyDataSetChanged();
//        } catch (NullPointerException e){
//            System.out.println(e.getMessage());
//        }
//
//
////        }
//    }

//    public void likePost(final View view){
//        int position = listView.getPositionForView(view);
//        Post post = (Post) postsAdapter.getItem(position);
//
//        CheckBox heart = findViewById(R.id.heart);
//        }


//        heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("klikkkk");
//                view.setBackground(getResources().getDrawable(R.drawable.icon_heart2));
//            }
//        });





    public void logout(View view){
        RegisterActivity.userID = 0;
        RegisterActivity.userName = "";
        Intent intent_lg = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent_lg);
    }
}
