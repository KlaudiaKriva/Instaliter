package com.example.instaliter.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.MainActivity;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    Button button, button2;
    TextView textView;
    DatabaseHelper databaseHelper;
    String pathImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println(" vykresluje sa druha screena");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        imageView = findViewById(R.id.cameraView);
        button = findViewById(R.id.back_button);
        textView = findViewById(R.id.addTitle);
        button2 = findViewById(R.id.btn_share);

        databaseHelper = new DatabaseHelper(this);

        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("spusta sa fotenie");
        startActivityForResult(intent2,0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        Uri path = getImageUri(getApplicationContext(),bitmap);
        pathImage = path.getPath();
        System.out.println("path image "+pathImage);
        imageView.setImageBitmap(bitmap);
    }

    public void insertNewPost(View view){
        if(!(textView.getText().toString().equals(""))){
            boolean result = databaseHelper.insertNewPost(RegisterActivity.userID,pathImage,textView.getText().toString());
            if (result){
                Toast.makeText(view.getContext(), "Post inserted successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CameraActivity.this, ProfileActivity.class);
                startActivity(intent);
                System.out.println(result + " result vracia");
            }
            else {
                Toast.makeText(view.getContext(), "Post not inserted",Toast.LENGTH_LONG).show();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
}

