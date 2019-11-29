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

import com.example.instaliter.activities.CameraActivity;
import com.example.instaliter.activities.ProfileActivity;

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

    public void getLoginUser(View view){
        ServerSingleton serverSingleton = ServerSingleton.getInstance();
        if(!(login_email.getText().toString().equals("") && (login_email.getText().toString().equals("")))){
            HashMap<String, String> params = new HashMap<>();
            params.put("email", login_email.getText().toString());
            params.put("password", login_pass.getText().toString());
            Map<String, String> result = new HashMap<>();

            result = serverSingleton.loginUser(params, LoginActivity.this);

//            boolean result = serverSingleton.loginUser(params, LoginActivity.this);

            boolean isEmpty = result.isEmpty();
//            LoggedUser loggedUser = LoggedUser.getInstance();
//            System.out.println("result log user je: "+ loggedUser.getId() + " sdv " + loggedUser.getToken());
            if(!isEmpty){
                Toast.makeText(view.getContext(), "Login inserted successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                System.out.println("loginactivity som "+ RegisterActivity.userID);
                startActivity(intent);
                System.out.println(result + " result vracia");
            } else {
                Toast.makeText(view.getContext(), "Login not inserted",Toast.LENGTH_LONG).show();
            }
        }
    }
}
