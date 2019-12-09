package com.example.instaliter;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MyVolley {

    private static MyVolley instance = new MyVolley();
    private static RequestQueue queue;

    private MyVolley(){}

    public static RequestQueue getRequestQueue(Context context){
        queue = Volley.newRequestQueue(context);
        return queue;
    }

    public static void addToQueueArray(JsonArrayRequest jsonArrayRequest){
        queue.add(jsonArrayRequest);
    }

    public static void addToQueueObject(JsonObjectRequest jsonObjectRequest){
        queue.add(jsonObjectRequest);
    }
}
