package com.example.instaliter.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.Comment;
import com.example.instaliter.LoginActivity;
import com.example.instaliter.MainActivity;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.activities.CommentsActivity;
import com.example.instaliter.activities.ProfileActivity;

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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{


    Context context;
    ArrayList<Post> postArrayList = new ArrayList<>();
    RequestManager glide;

    CommentAdapter commentAdapter;
    RecyclerView recyclerViewComments;
    RecyclerView.LayoutManager layoutManager1;



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


        final HashMap<String, String> params111 = new HashMap<>();
        params111.put("id", String.valueOf(post.getUserName()));

        RequestQueue queue111 = Volley.newRequestQueue(context);
        String url111 = registerurl + "userInfo";
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                url111, new JSONObject(params111),
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
                        System.out.println("response userInfo "+response);
                        try {

                            String instaname = response.getString("instaName");

                            String path = response.getString("imagePath");
                            System.out.println("co je totototototot za fotkuuuuuuuuuuuu "+path);
//                            if (path )
//                            holder.profileImage
                            holder.userName.setText(instaname);
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers= new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue111.add(jsonArrayRequest);

//        holder.userName.setText(RegisterActivity.userName); //tu sa nastavuje usrname postovv na prvej screene
        holder.time.setText(post.getDate());
        holder.post_text.setText(post.getPost_text());

        String pathPath = registerurl +post.getPostImage();
        System.out.println("path "+pathPath);

        glide.load(pathPath).into(holder.postImage);

        //pozor profileimage je staticka z registeractivity a profileImage je tu holder.profileImage

        if(profileimage != null){
            System.out.println("ta ja neviem totot co ej  "+profileimage);
            glide.load(profileimage).into(holder.profileImage);
            glide.load(profileimage).into(holder.commentUser);
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
                if (error instanceof ServerError){
                    holder.heart.setChecked(false);
                    holder.number.setText("0");
                    // nastavit 0 likes
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
                            },
                            new Response.ErrorListener() {
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
                    String numFromTV = holder.number.getText().toString();
                    System.out.println(" cislo likes "+numFromTV);
                    int number = Integer.parseInt(numFromTV);
                    System.out.println("uz cislo "+number);
                    int newNum = number + 1;
                    System.out.println("cislo plus 1 "+newNum);
                    String numString = String.valueOf(newNum);
                    System.out.println("string nove cislo "+numString);
                    holder.number.setText(numString);
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
                    String numFromTV = holder.number.getText().toString();
                    System.out.println(" cislo likes "+numFromTV);
                    int number = Integer.parseInt(numFromTV);
                    System.out.println("uz cislo "+number);
                    int newNum = number - 1;
                    System.out.println("cislo minus 1 "+newNum);
                    String numString = String.valueOf(newNum);
                    System.out.println("string nove cislo "+numString);
                    holder.number.setText(numString);
//                    queue.add(jsObjRequest);
                    queue.add(jsObjRequest);

                }
            }

        });


        //tu urobit array naplnenie

//
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.count == 0) {
                    holder.show_comments.setVisibility(View.VISIBLE);
                    holder.show_commentsLeyout.setVisibility(View.VISIBLE);
                    holder.linearLayoutComment.setVisibility(View.VISIBLE);
                    holder.count = 1;
                }
                else {
                    holder.show_comments.setVisibility(View.GONE);
                    holder.show_commentsLeyout.setVisibility(View.GONE);
                    holder.linearLayoutComment.setVisibility(View.GONE);
                    holder.count = 0;
                }
            }
        });

        layoutManager1 = new LinearLayoutManager(holder.postImage.getContext());
        holder.show_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("klikkkkkkkkkshfkajfbldkwbv");
                Intent intent = new Intent(context.getApplicationContext(), CommentsActivity.class);
                intent.putExtra("idI", getItemImageID(position));
                context.startActivity(intent);
            }
        });




        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("vypisujem addcoment: "+ holder.addComment.getText().toString());
                if(!(holder.addComment.getText().toString().equals(""))){
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(userID));
                    params.put("idI", String.valueOf(getItemId(position)));
                    params.put("commentText", holder.addComment.getText().toString());
                    Map<String, String> result = new HashMap<>();

                    RequestQueue queue = Volley.newRequestQueue(context);

                    String url = registerurl + "setImageComment";

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                            new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    holder.addComment.setText("");
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

                    queue.add(jsObjRequest);
                }

            }
        });


        final Map<String, String> responseMapComments;
        final ArrayList<Comment> commentArray = new ArrayList<>();

        final String[] usernameComment = new String[1];
        final String[] commentText = new String[1];
        final String[] commentTime = new String[1];


        //tu cislo komentarov

            System.out.println("tahaju sa comenty obrazka zo servera");
            if(!(token.equals(""))){
                HashMap<String, String> params222 = new HashMap<>();
                params222.put("idI", String.valueOf(getItemImageID(position)));

                RequestQueue queue222 = Volley.newRequestQueue(context);

                String url222 = registerurl + "getImageComments";
                responseMapComments = new HashMap<>();
                final JsonArrayRequest jsonArrayRequest222 = new JsonArrayRequest(
                        Request.Method.POST,
                        url222, new JSONObject(params),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response1) {
                                System.out.println("co vrati server "+ response1);
                                JSONObject response = null;
                                try {

                                    for (int i = 0; i< response1.length(); i++){
                                        response = response1.getJSONObject(i);
                                        System.out.println("response spravny uz "+response);
                                        //toto je zakomentovane, lebo je tam exception, int nemoze byt null, zaroven som zmenila idI hroe na "" a nie na int=0
                                        usernameComment[0] = response.getString("name");
                                        commentTime[0] = response.getString("cTime");
                                        commentText[0] = response.getString("commentText");



                                        responseMapComments.put("name", usernameComment[0]);
                                        responseMapComments.put("cTime", commentTime[0]);
                                        responseMapComments.put("commentText", commentText[0]);


                                    }

                                    String mytext = "Show comments ("+response1.length()+")";
                                    holder.show_comments.setText(mytext);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof ServerError){
                            System.out.println("ziadne komenty nema obrazok");
                        }
                        System.out.println(error);
                        System.out.println(error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> paramas = new HashMap<String, String>();
                        paramas.put("name", usernameComment[0]);
                        paramas.put("cTime", commentTime[0]);
                        paramas.put("commentText", commentText[0]);
                        return paramas;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers= new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };

                queue222.add(jsonArrayRequest222);

            } else {
                System.out.println("token je prazdny "+token);
            }


    }


    public int getItemImageID(int position){
        return Integer.parseInt(postArrayList.get(position).getIdI());
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }


//    CommentAdapter commentAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName, time, number, post_text;
        ImageView profileImage, postImage, commentUser;
        CheckBox heart;
        Button comment, btn_share, show_comments;
        EditText addComment;
        LinearLayout linearLayoutComment;
        RecyclerView all_comments;
        RelativeLayout show_commentsLeyout;
        int count;
        RecyclerView recyclerViewComments;
        ArrayList<Comment> commentArrayList;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            commentArrayList = new ArrayList<>();
            profileImage = itemView.findViewById(R.id.profileImage);
            postImage = itemView.findViewById(R.id.postImage);

            userName = itemView.findViewById(R.id.userName);
            time = itemView.findViewById(R.id.time);
            number = itemView.findViewById(R.id.number_ofLikes);
            post_text = itemView.findViewById(R.id.post_text);
            count = 0;
            heart = itemView.findViewById(R.id.heart);
            comment = itemView.findViewById(R.id.bubble);
            commentUser = itemView.findViewById(R.id.profileImageAU);
            addComment = itemView.findViewById(R.id.addComment);
            linearLayoutComment = itemView.findViewById(R.id.comment_lay);
            btn_share = itemView.findViewById(R.id.btn_shareComment);
            show_comments = itemView.findViewById(R.id.show_comments);
            all_comments = itemView.findViewById(R.id.all_comments);
            show_commentsLeyout = itemView.findViewById(R.id.show_commentsLeyout);
            recyclerViewComments = itemView.findViewById(R.id.all_comments);
        }
    }
}
