package com.example.instaliter.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.MainActivity;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.ServerSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    Button button, button2;
    TextView textView;
    DatabaseHelper databaseHelper;
    String pathImage;
    File photoFile;

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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = getImage();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Uri imageUri = FileProvider.getUriForFile(this,getPackageName()+".provider", photoFile);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        System.out.println("spusta sa fotenie");
        startActivityForResult(intent2,100);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissions",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("vola sa onActivity result");
        if (requestCode == 100 ){
            System.out.println("image path "+pathImage);
            imageView.setImageURI(Uri.parse(pathImage));
        }else {
            System.out.println("request code is not 100");
        }


//         Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//         pathImage = getImageUri(getApplicationContext(),bitmap);
//         System.out.println("path image "+pathImage);
//         imageView.setImageBitmap(bitmap);

    }

    public void insertPost(View view){
        if(!(textView.getText().toString().equals(""))){
//            boolean result = databaseHelper.insertNewPost(RegisterActivity.userID,pathImage,textView.getText().toString());
            ArrayList<String> tags = new ArrayList<>();
//            tags.add("");

            String text = textView.getText().toString();
            String regexPattern = "(#\\w+)";

            Pattern p = Pattern.compile(regexPattern);
            Matcher m = p.matcher(text);
            while (m.find()) {
                String hashTag = m.group(1);
                String novyhashTag = "\"" + hashTag + "\"";
                tags.add(novyhashTag);
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(RegisterActivity.userID));
            params.put("imageDescription", textView.getText().toString());
            params.put("type", String.valueOf(1));
            params.put("tag", String.valueOf(tags));

            System.out.println("hashtagy su: " + String.valueOf(tags));


//            HashMap<String, String> paramsToSend = new HashMap<>();
//            paramsToSend.put("recfile", pathImage);
//            paramsToSend.put("details", String.valueOf(params));
            boolean result = ServerSingleton.getInstance().uploadNewPost(params, pathImage, this);
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

    public File getImage() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFile = "IMG_"+ time+ "_";
        File storage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile,".jpg",storage);
        pathImage = image.getAbsolutePath();
        return image;

    }
}

