package com.example.instaliter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.volley.toolbox.Volley;
import com.example.instaliter.ChatUser;
import com.example.instaliter.MainActivity;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.adapters.PostsAdapter;
import com.example.instaliter.adapters.UserChatsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

public class ChatsActivity extends AppCompatActivity {
    DarkModeActivity modSharedPrefs;

    RecyclerView recyclerView;
    UserChatsAdapter userChatsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new DarkModeActivity(this);
        if (modSharedPrefs.loadDarkModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_layout);
        getChatUsers((int)userID);

        recyclerView = findViewById(R.id.list_posts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userChatsAdapter = new UserChatsAdapter(this,arrayList);
        recyclerView.setAdapter(userChatsAdapter);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
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

    Map<String, String> responseMapPosts;
    String idU;
    String name="";
    String instaName="";
    String thumbnailPath="";
    ArrayList<ChatUser> arrayList = new ArrayList<>();

    public void getChatUsers(int id)
    {
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getChatUsers";


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

//                                arrayList.clear();
                                for (int i = 0; i< response1.length(); i++){
                                    response = response1.getJSONObject(i);
                                    System.out.println("response spravny uz "+response);

                                    idU = response.getString("id");

                                    name = response.getString("name");

                                    instaName = response.getString("instaName");

                                    responseMapPosts.put("id", String.valueOf(idU));
                                    responseMapPosts.put("name", String.valueOf(name));
                                    responseMapPosts.put("instaName", instaName);


                                    ChatUser chatUser = new ChatUser( Integer.parseInt(idU), name, instaName, thumbnailPath);

                                    arrayList.add(chatUser);
                                    userChatsAdapter.notifyDataSetChanged();

                                    if (!responseMapPosts.isEmpty()){
                                        Toast.makeText(getBaseContext(), "User posts loaded",Toast.LENGTH_LONG).show();
                                        System.out.println("profile activity som "+ RegisterActivity.userID);
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
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> paramas = new HashMap<String, String>();
                    paramas.put("id", String.valueOf(idU));
                    paramas.put("name", name);
                    paramas.put("instaname", instaName);
                    paramas.put("thumbnailPath", thumbnailPath);
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
