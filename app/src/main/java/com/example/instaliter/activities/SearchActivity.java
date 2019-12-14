package com.example.instaliter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.example.instaliter.MainActivity;
import com.example.instaliter.MyVolley;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.User;
import com.example.instaliter.adapters.UsersAdapter;
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

public class SearchActivity extends AppCompatActivity{

    UsersAdapter adapter;
    RecyclerView recyclerView;
    EditText findUser;
    DarkModeActivity modSharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new DarkModeActivity(this);
        if (modSharedPrefs.loadDarkModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        recyclerView = findViewById(R.id.users);
        findUser = findViewById(R.id.findUser);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.topPanel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Search");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        glide = Glide.with(getApplicationContext());

        try {
            getAllUsers();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyVolley.getRequestQueue(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UsersAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent1 = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_search:
                        break;
                    case R.id.nav_camera:
                        Intent intent3 = new Intent(SearchActivity.this, CameraActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_messagges:
                        Intent intent4 = new Intent(SearchActivity.this, ChatsActivity.class);
                        startActivity(intent4);
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        try {
            getAllUsers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onKeyUp(keyCode, event);
    }

    Map<String, String> responseMapUsers;
    String id_users="";
    String name_users="";
    String thumbnailPath_users="";
    String instaName_users="";
    String idI_users = "";
    String imagePath_users="";
    ArrayList<User> arrayList = new ArrayList<>();
    RequestManager glide;

    public void getAllUsers() throws JSONException {
        System.out.println("tahaju sa vsetci useri");
        if(!(findUser.getText().toString().equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("word", findUser.getText().toString());

//            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "searchUser";

            responseMapUsers = new HashMap<>();
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
                                    id_users = response.getString("id");
                                    name_users = response.getString("name");
                                    instaName_users = response.getString("instaName");
                                    idI_users = response.getString("idI");
                                    imagePath_users = response.getString("imagePath");
//                                    if(!(imagePath_users == null)){
//                                        imagePath_users = imagePath_users.substring(0,6)+"//"+imagePath_users.substring(7);
//                                    }

                                    thumbnailPath_users = response.getString("thumbnailPath");
//                                    thumbnailPath_users =thumbnailPath_users.substring(0,10)+"/"+thumbnailPath_users.substring(11);

                                    responseMapUsers.put("id", id_users);
                                    responseMapUsers.put("name", name_users);
                                    responseMapUsers.put("instaName", instaName_users);
                                    responseMapUsers.put("idI", idI_users);
                                    responseMapUsers.put("imagePath", imagePath_users);
                                    responseMapUsers.put("thumbnailPath", thumbnailPath_users);

                                    User user = new User(id_users,name_users,instaName_users,idI_users,imagePath_users,thumbnailPath_users);
                                    arrayList.add(user);
                                    adapter.notifyDataSetChanged();

                                    if (!responseMapUsers.isEmpty()){
                                        Toast.makeText(getBaseContext(), "Users finding loaded",Toast.LENGTH_LONG).show();
                                        System.out.println("profile activity som "+ RegisterActivity.userID);
                                    }
                                    else {
                                        Toast.makeText(getBaseContext(), "User findings not loaded",Toast.LENGTH_LONG).show();
                                    }


                                }
                                System.out.println("tu vypisujem size arau, teda kolko userov sa naslo: " + arrayList.size());

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
                    paramas.put("id", id_users);
                    paramas.put("idI", idI_users);
                    paramas.put("imagePath", imagePath_users);
                    paramas.put("thumbnailPath", thumbnailPath_users);
                    paramas.put("instaName", instaName_users);
                    paramas.put("name", name_users);
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

            MyVolley.addToQueueArray(jsonArrayRequest);

        } else {
            System.out.println("token je prazdny "+token);
        }
    }


}
