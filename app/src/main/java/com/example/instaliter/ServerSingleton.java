package com.example.instaliter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.registerurl;


public class ServerSingleton {

    private static ServerSingleton instance = new ServerSingleton();

    private ServerSingleton(){}
    private final String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    public static ServerSingleton getInstance(){
        return instance;
    }

    public boolean registerUser(HashMap<String, String> params, final Context context){
        final boolean[] responseFromMethod = {false};
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = registerurl + "register";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.get("message"));
                            responseFromMethod[0] =true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseFromMethod[0]=false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println(error.getMessage());
                responseFromMethod[0]=false;
            }
        }
        );
        queue.add(jsObjRequest);
        return responseFromMethod[0];
    }



}


