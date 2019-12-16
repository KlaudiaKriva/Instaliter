package com.example.instaliter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.instaliter.MyVolley;
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

public class LikesActivity extends AppCompatActivity {

    int idImage;
    UsersAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<User> arrayList = new ArrayList<>();
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_activity);
        idImage = getIntent().getIntExtra("idI", 0);
        MyVolley.getRequestQueue(this);
        recyclerView = findViewById(R.id.followers);
        button = findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsersAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
        getUsersLiked();

    }

    String id_users = "";
    String name_users = "";
    String instaName_users = "";

    private void getUsersLiked(){
        HashMap<String, String> params2 = new HashMap<>();
        params2.put("idI", String.valueOf(idImage));
        System.out.println("idimageu jee: "+ idImage);
        String url2 = registerurl + "getImageLikes";
        JsonArrayRequest jsObjRequest2 = new JsonArrayRequest(Request.Method.POST, url2,
                new JSONObject(params2),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response1) {
                        System.out.println("co vrati server "+ response1);
                        JSONObject response = null;
                        try {
                            for (int i = 0; i< response1.length(); i++){
                                response = response1.getJSONObject(i);
                                System.out.println("response spravy checkuserlikes uz "+response);
                                id_users = response.getString("id");
                                name_users = response.getString("name");
                                instaName_users = response.getString("instaname");
                                User user = new User(id_users, name_users, instaName_users, "", "", "");

                                updateUserInfo(user);
                                arrayList.add(user);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError){
                    System.out.println(error.getMessage());
                }
                System.out.println(error);
                System.out.println(error.getMessage());
            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("idI", String.valueOf(idImage));
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers= new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        MyVolley.addToQueueArray(jsObjRequest2);
    }

    //dokoncit

    public void updateUserInfo(User user){
        HashMap<String, String> params2 = new HashMap<>();
        params2.put("idI", String.valueOf(idImage));
        System.out.println("idimageu jee: "+ idImage);
        String url2 = registerurl + "getImageLikes";
        JsonArrayRequest jsObjRequest2 = new JsonArrayRequest(Request.Method.POST, url2,
                new JSONObject(params2),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response1) {
                        System.out.println("co vrati server "+ response1);
                        JSONObject response = null;
                        try {
                            for (int i = 0; i< response1.length(); i++){
                                response = response1.getJSONObject(i);
                                System.out.println("response spravy checkuserlikes uz "+response);
                                id_users = response.getString("id");
                                name_users = response.getString("name");
                                instaName_users = response.getString("instaname");
                                User user = new User(id_users, name_users, instaName_users, "", "", "");

                                updateUserInfo(user);
                                arrayList.add(user);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError){
                    System.out.println(error.getMessage());
                }
                System.out.println(error);
                System.out.println(error.getMessage());
            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("idI", String.valueOf(idImage));
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers= new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        MyVolley.addToQueueArray(jsObjRequest2);

    }


}
