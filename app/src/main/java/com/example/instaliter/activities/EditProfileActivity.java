package com.example.instaliter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.example.instaliter.LoginActivity;
import com.example.instaliter.MainActivity;
import com.example.instaliter.MyVolley;
import com.example.instaliter.NetworkClient;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

public class EditProfileActivity extends AppCompatActivity {

    Button button, button2, button3;
    ImageView profilePic;
    EditText changeDesc;
    Switch switchDarkMode;
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
        setContentView(R.layout.edit_profile_layout);

        button = findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profilePic = findViewById(R.id.profile_picture);
        changeDesc = findViewById(R.id.change_desc);

        button3 = findViewById(R.id.btn_done);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDescription();
            }
        });

        button2 = findViewById(R.id.btn_changePic);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePorfilePic();
            }
        });

        modSharedPrefs = new DarkModeActivity(this);

        MyVolley.getRequestQueue(this);
        switchDarkMode = findViewById(R.id.simpleSwitch);
        if(modSharedPrefs.loadDarkModeState()){
            switchDarkMode.setChecked(true);
        }
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    modSharedPrefs.setDarkModeState(true);
                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(i);
                }else{
                    modSharedPrefs.setDarkModeState(false);
                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    //este to neuploaduje fotku na server
    public void changePorfilePic(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        Intent intent2 = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        System.out.println("spusta sa vyber obrazku");
        startActivityForResult(intent2,100);
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
        if (requestCode == 100) {
            System.out.println("result code "+resultCode);
            try {
                if (data == null) {
                    Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                    startActivity(intent);
                } else {
                    Uri imageUri = data.getData();
                    String imagePath = getRealPathFromURI(imageUri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    profilePic.setImageBitmap(bitmap);

                    System.out.println("image path from bitmap "+imagePath);
                    uploadToServer(imagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                onBackPressed();
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    String imageDesc = "My new profile picture";
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
        String descriptionString = "{\"id\":"+ userID+",\"imageDescription\":\""+imageDesc+"\", \"type\":1, \"tag\":[]}";
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionString);
        System.out.println("description "+description.toString());
        //
        Call call = uploadAPIs.uploadImage(part, description);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                System.out.println("upload succcessful");
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("ulpoad not successsful");
            }
        });
    }

    Map<String, String> responseMap;
    String isItOk="";

    public void editUserDescription(){
        if(!(changeDesc.getText().toString().equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(RegisterActivity.userID));
            params.put("profileDescription", changeDesc.getText().toString());
            Map<String, String> result = new HashMap<>();

            String url = registerurl + "setProfileDescription";

            responseMap = new HashMap<>();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("dostal som sa aspon tu");
                            //zakomentovane veci lebo na onresponse sa nikdy nedostane :D pise chybu: nemoze sparsovat string na jsonobject
                            //on response sa nevykona, who knows why :D
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                    System.out.println(error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers= new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            MyVolley.addToQueueObject(jsObjRequest);
            Toast.makeText(getBaseContext(), "Description changed",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
            startActivity(intent);
        }
    }

}
