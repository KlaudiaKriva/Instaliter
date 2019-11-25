package com.example.instaliter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    Button button;
    DatabaseHelper databaseHelper;
    EditText register_name;
    EditText register_username;
    EditText register_email;
    EditText register_password;
    public static long userID;
    public static String userName = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        register_name = findViewById(R.id.register_name);
        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);

        databaseHelper = new DatabaseHelper(this);

//        button = findViewById(R.id.btn_signUp2);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                insertNewUser();
//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    public void insertNewUser(View v){
        if(!(register_name.getText().toString().equals("") && register_username.getText().toString().equals("")
        && register_email.getText().toString().equals("") && register_password.getText().toString().equals(""))){
            long result = databaseHelper.insertNewUser(register_name.getText().toString(), register_username.getText().toString(),
                    register_email.getText().toString(), register_password.getText().toString());
            if (result != -1){
                Toast.makeText(v.getContext(), "User inserted successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                System.out.println(result + " result vracia");
                //nastavit staticuser ako result
                userID = result;
                userName = register_username.getText().toString();

            }
            else {
                Toast.makeText(v.getContext(), "User not inserted",Toast.LENGTH_LONG).show();
            }


        }



    }
}
