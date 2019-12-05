package com.example.instaliter.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.instaliter.Comment;
import com.example.instaliter.R;
import com.example.instaliter.adapters.CommentAdapter;
import com.example.instaliter.adapters.PostsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    int idI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);

        recyclerView = findViewById(R.id.list_comments);
        idI = getIntent().getIntExtra("idI", 0);

        getUserComments(idI);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this,commentArray);
        recyclerView.setAdapter(commentAdapter);



    }

    Map<String, String> responseMapComments;
    ArrayList<Comment> commentArray = new ArrayList<>();

    String usernameComment;
    String commentText;
    String commentTime;

    public void getUserComments(int idImage) {
        System.out.println("tahaju sa comenty obrazka zo servera");
        if(!(token.equals(""))){
            HashMap<String, String> params = new HashMap<>();
            params.put("idI", String.valueOf(idImage));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getImageComments";
            responseMapComments = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
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
                                    usernameComment = response.getString("name");
                                    commentTime = response.getString("cTime");
                                    commentText= response.getString("commentText");


                                    responseMapComments.put("name", usernameComment);
                                    responseMapComments.put("cTime", commentTime);
                                    responseMapComments.put("commentText", commentText);


                                    Comment comment = new Comment(usernameComment, idI, commentTime, commentText);

                                    commentArray.add(comment);
                                    commentAdapter.notifyDataSetChanged();

                                }

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
                    paramas.put("name", usernameComment);
                    paramas.put("cTime", commentTime);
                    paramas.put("commentText", commentText);
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

            queue.add(jsonArrayRequest);

        } else {
            System.out.println("token je prazdny "+token);
        }
    }
}
