package com.example.instaliter.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.profileimage;
import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

//public class PostsAdapter extends BaseAdapter {
//
//    Context context;
//    ArrayList<Post> arrayList;
//    CheckBox heart;
//
//    public PostsAdapter(Context context, ArrayList<Post> arrayList) {
//        this.context = context;
//        this.arrayList = arrayList;
//    }
//
//    @Override
//    public int getCount() {
//        return this.arrayList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return arrayList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
////        return position;
//        return arrayList.get(position).getId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = li.inflate(R.layout.one_contribution, null);
//
//        System.out.println("convertview: "+ convertView);
//        Post post = arrayList.get(position);
//        System.out.println("arrayl je: " + arrayList.size());
//        System.out.println(post.getId() + post.getPost_text());
//
//
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//        TextView userName = convertView.findViewById(R.id.userName);
//        userName.setText(databaseHelper.selectUserNameFromPost(post.getId()));//tu treba ine
//
//        ImageView postImage = convertView.findViewById(R.id.postImage);
//        System.out.println(postImage + "imageview");
//        System.out.println("uri je " +post.getPostImage());
//        String path = post.getPostImage();
//        System.out.println("path je: "+ path);
//        postImage.setImageURI(Uri.parse(path));
//
//        heart = convertView.findViewById(R.id.heart);
//        heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    System.out.println("klikkkk");
//                    buttonView.setBackground(context.getResources().getDrawable(R.drawable.icon_heart2));
//                }
//                else
//                    buttonView.setBackground(context.getResources().getDrawable(R.drawable.heart_icon));
//            }
//
//        });
//
//        TextView post_text = convertView.findViewById(R.id.post_text);
//        post_text.setText(post.getPost_text());
//
//
//         System.out.println("som v getview postsadapter " + post_text);
//        return convertView;
//    }
//}

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{

    Context context;
    ArrayList<Post> postArrayList = new ArrayList<>();
    RequestManager glide;

    public PostsAdapter(Context context, ArrayList<Post> arrayList){
        this.context = context;
        this.postArrayList = arrayList;
        glide = Glide.with(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_contribution, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(postArrayList.get(position).getIdI());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Post post = postArrayList.get(position);

        holder.userName.setText(RegisterActivity.userName);
        holder.time.setText(post.getDate());
        holder.post_text.setText(post.getPost_text());
        String pathPath = registerurl +post.getPostImage();
        System.out.println("path "+pathPath);
        glide.load(pathPath).into(holder.postImage);

        //pozor profileimage je staticka z registeractivity a profileImage je tu holder.profileImage
        if(profileimage != null){
            glide.load(profileimage).into(holder.profileImage);
        }

        final int[] active = new int[1];

        //request na ziskanie ci je lajknuty obrazok, ak sa vrati active 1- je ak active 0- nie je
        //na zaklade toho sa nastavi ci je checked


        RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(userID));
        params.put("idI", String.valueOf(getItemId(position)));
        String url = registerurl + "checkUserLike";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.POST, url,
                new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response1) {
                        System.out.println("co vrati server "+ response1);
                        JSONObject response = null;
                        try {
                            for (int i = 0; i< response1.length(); i++){
                                response = response1.getJSONObject(i);
                                System.out.println("response spravy uz "+response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // tento response je [{object}]
                        System.out.println("response checkUserLike "+response);
                        try {
                            active[0] = response.getInt("active");

                            if (active[0] == 1){
                                holder.heart.setChecked(true);
                            }
                            else {
                                holder.heart.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println(error.getMessage());
            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers= new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(jsObjRequest);

        holder.heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RequestQueue queue = Volley.newRequestQueue(context);
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(userID));
                params.put("idI", String.valueOf(getItemId(position)));

                if (isChecked) {
                    post.setLiked(true);
                    System.out.println("klikkkk");
                    buttonView.setBackground(context.getResources().getDrawable(R.drawable.icon_heart2));
                    System.out.println(getItemId(position) + " som zvedava ake id to vybralo");


                    String url = registerurl + "setUserLike";
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                            new JSONObject(params),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            System.out.println(error.getMessage());
                        }

                    }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers= new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            headers.put("Authorization", "Bearer " + token);
                            return headers;
                        }
                    };
                    queue.add(jsObjRequest);
                }
                else {
                    post.setLiked(false);
                    System.out.println(getItemId(position) + " som zvedava ake id to vybralo v dislike");
                    buttonView.setBackground(context.getResources().getDrawable(R.drawable.heart_icon));
                    String url = registerurl + "setUserDislike";
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                            new JSONObject(params),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            System.out.println(error.getMessage());
                        }

                    }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers= new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            headers.put("Authorization", "Bearer " + token);
                            return headers;
                        }
                    };
                    queue.add(jsObjRequest);

                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName, time, number, post_text;
        ImageView profileImage, postImage;
        CheckBox heart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            postImage = itemView.findViewById(R.id.postImage);

            userName = itemView.findViewById(R.id.userName);
            time = itemView.findViewById(R.id.time);
            number = itemView.findViewById(R.id.number_ofLikes);
            post_text = itemView.findViewById(R.id.post_text);

            heart = itemView.findViewById(R.id.heart);
        }
    }
}
