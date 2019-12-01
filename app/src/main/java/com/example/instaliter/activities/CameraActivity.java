package com.example.instaliter.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.instaliter.NetworkClient;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.ServerSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.instaliter.RegisterActivity.*;
import static com.example.instaliter.RegisterActivity.token;

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
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPost();
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

    public void insertPost(){
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
            params.put("id", String.valueOf(userID));
            params.put("imageDescription", textView.getText().toString());
            params.put("type", String.valueOf(1));
            params.put("tag", String.valueOf(tags));

            System.out.println("hashtagy su: " + String.valueOf(tags));


//            HashMap<String, String> paramsToSend = new HashMap<>();
//            paramsToSend.put("recfile", pathImage);
//            paramsToSend.put("details", String.valueOf(params));
//            uploadFile(pathImage);
            uploadToServer(pathImage);
            boolean result = true;
            if (result){
//                Toast.makeText(view.getContext(), "Post inserted successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CameraActivity.this, ProfileActivity.class);
                startActivity(intent);
                System.out.println(result + " result vracia");
            }
            else {
//                Toast.makeText(view.getContext(), "Post not inserted",Toast.LENGTH_LONG).show();
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

    public interface UploadAPIs {
        @Multipart
        @POST("/uploadImage")
        Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("details") RequestBody requestBody);
    }

    private void uploadToServer(String filePath) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        System.out.println("fileReqBody "+fileReqBody.toString());
        // Create MultipartBody.Part using file request-body,file name and part name

        MultipartBody.Part part = MultipartBody.Part.createFormData("recfile", file.getName(), fileReqBody);
        System.out.println("part "+part.toString());
        //Create request body with text description and text media type
        String descriptionString = "{\"id\":"+ userID+",\"imageDescription\":\""+textView.getText().toString()+"\", \"type\":0, \"tag\": [\"#newPost\"]}";
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionString);
        System.out.println("description "+description.toString());
        //
        Call call = uploadAPIs.uploadImage(part, description);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("upload succcessful");
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("ulpoad not successsful");
            }
        });
    }
}

