package com.example.instaliter.activities;

import android.os.Bundle;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.Follower;
import com.example.instaliter.R;
import com.example.instaliter.User;
import com.example.instaliter.adapters.UsersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

public class FollowersActivity extends AppCompatActivity {
    UsersAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<User> arrayList = new ArrayList<>();
    DarkModeActivity modSharedPrefs;
    int idU;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new DarkModeActivity(this);
        if (modSharedPrefs.loadDarkModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_activity);
        recyclerView = findViewById(R.id.followers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsersAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);

        idU = getIntent().getIntExtra("idU", 0);

        try {
            getFollowers();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("vypisujem velkost arrayu na followers acitivite::  " + arrayList.size());
    }

    String id_users = "";
    String name_users = "";
    String instaName_users = "";

    public void getFollowers() throws JSONException {
        System.out.println("tahaju sa posty usera zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(idU));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getUsersFollowers";

            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            try {
                                for (int i = 0; i< response1.length(); i++) {
                                    response = response1.getJSONObject(i);
                                    id_users = response.getString("id");
                                    name_users = response.getString("name");
                                    instaName_users = response.getString("instaname");

//                                    Follower follower = new Follower(Integer.parseInt(id_users), name_users, instaName_users);

                                    User user = new User(id_users, name_users, instaName_users, "", "", "");

                                    arrayList.add(user);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            catch (JSONException e){
                                System.out.println(e.getMessage());
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
