package com.example.instaliter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.instaliter.NetworkClient;
import com.example.instaliter.R;

import java.io.File;
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
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.instaliter.RegisterActivity.*;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    Button button, button2;
    TextView textView;
    String pathImage;
    File photoFile;
    DarkModeActivity modSharedPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new DarkModeActivity(this);
        if (modSharedPrefs.loadDarkModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        System.out.println(" vykresluje sa druha screena");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        imageView = findViewById(R.id.cameraView);
        button = findViewById(R.id.back_button);
        textView = findViewById(R.id.addTitle);
        button2 = findViewById(R.id.btn_share);

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


    }

    public void insertPost(){
        if(!(textView.getText().toString().equals(""))){
            ArrayList<String> tags = new ArrayList<>();

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

            uploadToServer(pathImage);
            boolean result = true;
            if (result){
                Intent intent = new Intent(CameraActivity.this, ProfileActivity.class);
                startActivity(intent);
                System.out.println(result + " result vracia");
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

