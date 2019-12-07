package com.example.instaliter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.instaliter.activities.DarkModeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button button;
    EditText register_name;
    EditText register_username;
    EditText register_email;
    EditText register_password;
    public static long userID;
    public static String userName;
    public static String token;
    public static String registerurl = "http://192.168.1.123:5005/";
    public static String profileimage;
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
        setContentView(R.layout.register_layout);
        register_name = findViewById(R.id.register_name);
        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);

        button = findViewById(R.id.back_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void insertNewUser(View v){
        if(!(register_name.getText().toString().equals("") || register_username.getText().toString().equals("")
        || register_email.getText().toString().equals("") || register_password.getText().toString().equals(""))) {

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("name", register_name.getText().toString());
            params.put("instaName", register_username.getText().toString());
            params.put("email", register_email.getText().toString());
            params.put("password", register_password.getText().toString());

            ServerSingleton serverSingleton = ServerSingleton.getInstance();
            boolean response = serverSingleton.registerUser(params, RegisterActivity.this);
            if(response){
                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
            else {
                Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter all values", Toast.LENGTH_LONG).show();
        }
    }
}
