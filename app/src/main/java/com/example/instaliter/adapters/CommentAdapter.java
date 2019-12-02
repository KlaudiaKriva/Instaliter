package com.example.instaliter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.Comment;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    Context context;
    ArrayList<Comment> commentArrayList = new ArrayList<>();
    RequestManager glide;

    public CommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
        glide = Glide.with(context);
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_comment, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.MyViewHolder holder, int position) {
//        final Comment comment = commentArrayList.get(position);

        RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<>();
        params.put("idI", String.valueOf(getItemId(position)));
        String url = registerurl + "getImageComments";
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
                            String userName = response.getString("name");
                            holder.username.setText(userName);
                            String time = response.getString("cTime");
                            holder.time_comment.setText(time);
                            String text = response.getString("commentText");
                            holder.text_comment.setText(text);

                            int idProfilePhoto = response.getInt("idI");

                            Comment comment1 = new Comment(userName, idProfilePhoto, time, text);
                            commentArrayList.add(comment1);

                            System.out.println("id profilovky je: "+ idProfilePhoto);
                            loadProfileImage(idProfilePhoto, holder);

//                            commentArrayList.add()
//                            glide.load(tu cesta).into(holder.profileImage);
                            //show comments visible  show_comments a onclick all_comments visible

                            //
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError){
                }
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

    @Override
    public long getItemId(int position) {
        return Long.parseLong(String.valueOf(commentArrayList.get(position).getIdI()));
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }


    public void loadProfileImage(int idProfilePhoto, final CommentAdapter.MyViewHolder holder){

        RequestQueue queue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<>();
        params.put("idI", String.valueOf(idProfilePhoto));
        String url = registerurl + "getImage";
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
                            String imagePath = response.getString("path");
                            glide.load(imagePath).into(holder.profileImage);
//                            int idProfilePhoto = response.getInt("idI");

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError){
                }
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
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, text_comment, time_comment;
        ImageView profileImage;
//        LinearLayout all_comments;
        int count;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            time_comment = itemView.findViewById(R.id.time_comment);
            text_comment = itemView.findViewById(R.id.text_comment);
//            all_comments = itemView.findViewById(R.id.all_comments);
        }
    }
}
