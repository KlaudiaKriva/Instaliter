package com.example.instaliter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.MainActivity;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.instaliter.RegisterActivity.profileimage;
import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileOfOthersActivity extends AppCompatActivity {

    public  static int otherUserID;

    PostsAdapter postsAdapter;
    RecyclerView recyclerView;
    TextView profile_number_of_posts;
    TextView profile_username, profile_desc;
    ImageView profile_picture;
    ArrayList<Post> arrayList = new ArrayList<>();
    Button button, unfollow;
    DarkModeActivity modSharedPrefs;
    TextView profile_user_followers, profile_user_following;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new DarkModeActivity(this);
        if (modSharedPrefs.loadDarkModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_others);
        System.out.println("moje id je " + otherUserID);

        profile_number_of_posts = findViewById(R.id.profile_number_of_posts);
        profile_username = findViewById(R.id.profile_username);
        profile_picture = findViewById(R.id.profile_picture);
        recyclerView = findViewById(R.id.myPosts);
        profile_desc = findViewById(R.id.profile_description);
        button = findViewById(R.id.followUser);
        unfollow = findViewById(R.id.unfollowUser);
        profile_user_followers = findViewById(R.id.profile_number_followers);
        profile_user_following = findViewById(R.id.profile_number_following);

        glide = Glide.with(getApplicationContext());

        try {
            getUserInfo();
            getUserPosts();
            getUsersFollowers();
            getUserFollowing();
            isUserFollowed();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        postsAdapter = new PostsAdapter(this, arrayList);
        recyclerView.setAdapter(postsAdapter);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
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


    RequestManager glide;
    public void getUserInfo() throws JSONException {
        System.out.println("tahaju sa data zo servera o userovi");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(otherUserID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "userInfo";

            responseMap = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
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
//                                    RegisterActivity.userName = username;
                                    profile_desc.setText(profileDescription);
                                    profile_username.setText(username);
                                    if(profileDescription.equals("null")){
                                        profile_desc.setText(R.string.about_me);
                                    } else {
                                        profile_desc.setText(profileDescription);
                                    }

                                    System.out.println("tu by som chcela nastavit imagepath: "+ registerurl + imagePath);

                                    if((registerurl+imagePath).equals(registerurl+"null")){
                                        profile_picture.setImageDrawable(getResources().getDrawable(R.drawable.profile_pic));
                                    }else {
                                        String profileimageOfOther = registerurl+imagePath;
                                        glide.load(profileimageOfOther).into(profile_picture);
                                    }

                                    System.out.println("profile activity som "+ RegisterActivity.userID + "profile druheho");
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
    int type_posts =0;



    public void getUserPosts() throws JSONException {
        System.out.println("tahaju sa posty usera zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(otherUserID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getUserImages";


            responseMapPosts = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            try {

                                for (int i = 0; i< response1.length(); i++){
                                    response = response1.getJSONObject(i);
                                    System.out.println("response spravny uz "+response);
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
                                    type_posts = response.getInt("type");

                                    responseMapPosts.put("id", String.valueOf(userID));
                                    responseMapPosts.put("idI", String.valueOf(idI_posts));
                                    responseMapPosts.put("imagePath", path_posts);
                                    responseMapPosts.put("thumbnailPath", thumbnailPath_posts);
                                    responseMapPosts.put("description", description_posts);
                                    responseMapPosts.put("imageDate", date_posts);
                                    responseMapPosts.put("type", String.valueOf(type_posts));

                                    Post post = new Post(otherUserID, idI_posts , path_posts, thumbnailPath_posts, description_posts,date_posts, type_posts,false);
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
                                System.out.println("tu vypisujem size arau: " + arrayList.size());
                                profile_number_of_posts.setText(String.valueOf(arrayList.size()));

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
                    paramas.put("type", String.valueOf(type_posts));
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

    public void followUser(){
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));
            params.put("idF", String.valueOf(otherUserID));
            Map<String, String> result = new HashMap<>();

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "setUserFollow";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            getUsersFollowers();
                            isUserFollowed();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers= new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            queue.add(jsObjRequest);
        }
    }

    public void unfollowUser(){
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));
            params.put("idF", String.valueOf(otherUserID));
            Map<String, String> result = new HashMap<>();

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "setUserUnFollow";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            getUsersFollowers();
                            isUserFollowed();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers= new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            queue.add(jsObjRequest);
        }
    }

    public void getUsersFollowers() {
        System.out.println("tahaju sa posty usera zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(otherUserID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getUsersFollowers";
            responseMapPosts = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            profile_user_followers.setText(String.valueOf(response1.length()));

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                    profile_user_followers.setText(String.valueOf("0"));
                }
            }) {
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

    public void getUserFollowing(){
        System.out.println("tahaju sa posty usera zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(otherUserID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getUserFollowers";
            responseMapPosts = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            profile_user_following.setText(String.valueOf(response1.length()));


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
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


    public void openFollowers(View view){
        Intent intent = new Intent(ProfileOfOthersActivity.this, FollowersActivity.class);
        intent.putExtra("idU", otherUserID);
        view.getContext().startActivity(intent);
    }

    public void openFollowing(View view){
        Intent intent = new Intent(ProfileOfOthersActivity.this, FollowingActivity.class);
        intent.putExtra("idU", otherUserID);
        view.getContext().startActivity(intent);
    }

    Map<String, String> responseMapIsFollowed;
    boolean isFollowed = false;

    public void isUserFollowed(){
        System.out.println("checkuje sa follow usera zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));
            params.put("idF", String.valueOf(otherUserID));
            Map<String, String> result = new HashMap<>();

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "checkIfUserFollowed";

            responseMapIsFollowed = new HashMap<>();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                isFollowed = response.getBoolean("followed");
                                System.out.println("is folowed je: "+ isFollowed);
                                System.out.println("folovuje toho cloveka ? "+isFollowed);

                                if(isFollowed){
                                    unfollow.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.GONE);
                                    unfollow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            unfollowUser();
                                            button.setVisibility(View.VISIBLE);
                                            unfollow.setVisibility(View.GONE);
                                        }
                                    });
                                }else{
                                    button.setVisibility(View.VISIBLE);
                                    unfollow.setVisibility(View.GONE);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            followUser();
                                            Toast.makeText(getBaseContext(), "Follow",Toast.LENGTH_LONG).show();
                                            button.setVisibility(View.GONE);
                                            unfollow.setVisibility(View.VISIBLE);
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            responseMapIsFollowed.put("followed", String.valueOf(isFollowed));
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
                    paramas.put("followed", String.valueOf(isFollowed));
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
            queue.add(jsObjRequest);
        } else {
            System.out.println("token je prazdny "+token);
        }
    }
}
