package com.example.instaliter;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServerSingleton {

    private static ServerSingleton instance = new ServerSingleton();

    private ServerSingleton(){}

    public static ServerSingleton getInstance(){
        return instance;
    }

    public boolean registerUser(HashMap<String, String> params, final Context context){
        final boolean[] responseFromMethod = {false};
        RequestQueue queue = Volley.newRequestQueue(context);
        String registerurl = "http://192.168.2.240:5005/register";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, registerurl,
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
        });
        queue.add(jsObjRequest);
        return responseFromMethod[0];
    }
}
