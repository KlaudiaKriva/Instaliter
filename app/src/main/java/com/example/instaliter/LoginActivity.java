package com.example.instaliter;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.instaliter.activities.CameraActivity;
import com.example.instaliter.activities.ProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    Button button, button2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    EditText login_email, login_pass;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        button = findViewById(R.id.btn_signUp);
        button2 = findViewById(R.id.btn_login);
        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);

        databaseHelper = new DatabaseHelper(this);


        handler.postDelayed(runnable,4000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    Map<String, String> responseMap;
    String token = "";
    String id = "";
    String username="";
//    String instaname="";

    public void getLoginUser(View view){
        if(!(login_email.getText().toString().equals("") && (login_pass.getText().toString().equals("")))){
            HashMap<String, String> params = new HashMap<>();
            params.put("email", login_email.getText().toString());
            params.put("password", login_pass.getText().toString());
            Map<String, String> result = new HashMap<>();

            RequestQueue queue = Volley.newRequestQueue(this);
            String registerurl = "http://192.168.0.102:5005/login";

            responseMap = new HashMap<>();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, registerurl,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("response "+response);
                                token = response.getString("token");
                                id = String.valueOf(response.getInt("id"));
                                username = response.getString("name");
                                System.out.println("vypisujem id a token" + token + " id je: " + id);
                                responseMap.put("id", id);
                                responseMap.put("token", token);
                                if (!responseMap.isEmpty()){
                                    Toast.makeText(getBaseContext(), "Login inserted successfully",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    RegisterActivity.userID = Long.parseLong(id);
                                    RegisterActivity.userName = username;
                                    RegisterActivity.token = token;
                                    System.out.println("loginactivity som "+ RegisterActivity.userID + "meno moje je "+ RegisterActivity.userName + "a moj token je "+ RegisterActivity.token);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getBaseContext(), "Login not inserted",Toast.LENGTH_LONG).show();
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
                    paramas.put("id", id);
                    paramas.put("token", token);
                    responseMap = paramas;
                    return paramas;
                }
            };

            queue.add(jsObjRequest);
        }
    }
}
