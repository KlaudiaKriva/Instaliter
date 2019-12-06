package com.example.instaliter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.instaliter.activities.CameraActivity;
import com.example.instaliter.activities.DarkModeActivity;
import com.example.instaliter.activities.ProfileActivity;
import com.example.instaliter.activities.SearchActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

public class MainActivity extends AppCompatActivity {

    PostsAdapter postsAdapter;
    ArrayList<Post> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView textView;
    DarkModeActivity modSharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new DarkModeActivity(this);
        if (modSharedPrefs.loadDarkModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        System.out.println("vykresluje sa prva screena + " + RegisterActivity.userID + " a zaroven  name: "+ RegisterActivity.userName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.no_posts);
        recyclerView = findViewById(R.id.list_posts);

        try {
            getUserPosts();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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

        try {
            getUserPosts();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Map<String, String> responseMapPosts;
    String idUsera;
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
            params.put("id", String.valueOf(userID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getFeed";

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
                                arrayList.clear();
                                for (int i = 0; i< response1.length(); i++){
                                    response = response1.getJSONObject(i);
                                    System.out.println("response spravny uz "+response);
                                    //toto je zakomentovane, lebo je tam exception, int nemoze byt null, zaroven som zmenila idI hroe na "" a nie na int=0

                                    idUsera = response.getString("id");
                                    int idcko = Integer.parseInt(idUsera);
                                    idI_posts = response.getString("idI");
                                    path_posts = response.getString("path");
                                    path_posts =path_posts.substring(0,6)+"/"+path_posts.substring(7);
                                    System.out.println("image cesta "+path_posts);
                                    thumbnailPath_posts = response.getString("thumbnailpath");
                                    thumbnailPath_posts =thumbnailPath_posts.substring(0,10)+"/"+thumbnailPath_posts.substring(11);
                                    System.out.println("image cesta "+thumbnailPath_posts);
                                    description_posts= response.getString("description");
                                    date_posts = response.getString("ctime");
                                    type_posts = response.getInt("type");

                                    responseMapPosts.put("id", String.valueOf(idUsera));
                                    responseMapPosts.put("idI", String.valueOf(idI_posts));
                                    responseMapPosts.put("imagePath", path_posts);
                                    responseMapPosts.put("thumbnailPath", thumbnailPath_posts);
                                    responseMapPosts.put("description", description_posts);
                                    responseMapPosts.put("imageDate", date_posts);
                                    responseMapPosts.put("type", String.valueOf(type_posts));

                                    Post post = new Post(idcko, idI_posts , path_posts, thumbnailPath_posts, description_posts,date_posts, type_posts,false);

                                    //tu osetrit ci je alebo nie je followerom nakoniec, ci je 0 alebo 1- pri type to nefunguje- to len oznacuje ci to je alebo nie je profilova fotka
                                    arrayList.add(post);


                                    postsAdapter.notifyDataSetChanged();

                                    if (!responseMapPosts.isEmpty()){
                                        Toast.makeText(getBaseContext(), "User posts loaded",Toast.LENGTH_LONG).show();
                                        System.out.println("profile activity som "+ RegisterActivity.userID + "main activity");
                                    }
                                    else {
                                        Toast.makeText(getBaseContext(), "User posts not loaded",Toast.LENGTH_LONG).show();
                                    }
                                }
                                System.out.println("tu vypisujem size arau: " + arrayList.size());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof ServerError){
                        textView.setVisibility(View.VISIBLE);
                    }
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramas = new HashMap<String, String>();
                    paramas.put("id", String.valueOf(idUsera));
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

}



