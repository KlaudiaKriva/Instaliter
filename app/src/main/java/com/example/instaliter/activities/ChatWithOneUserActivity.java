package com.example.instaliter.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.instaliter.ChatUser;
import com.example.instaliter.Message;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;
import com.example.instaliter.SocketClient;
import com.example.instaliter.adapters.MessageAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.instaliter.RegisterActivity.registerurl;
import static com.example.instaliter.RegisterActivity.token;
import static com.example.instaliter.RegisterActivity.userID;

public class ChatWithOneUserActivity extends AppCompatActivity {

    EditText userMessageText;
    ImageButton sendButton;
    ListView messages_view;
    private MessageAdapter messageAdapter;

    private int recieverID;
    private String recieverName;
    private String recieverThumbnail;

    private Socket mSocket = SocketClient.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_chat_layout);

        recieverID = getIntent().getIntExtra("receiverID", 0);
        recieverThumbnail = getIntent().getStringExtra("receiverThumbnail");
        recieverName = getIntent().getStringExtra("receiveName");

        System.out.println("dostali sme tieto hodnoty " + recieverID + " " + recieverThumbnail + " " + recieverName);
        mSocket.on("message", onNewMessage);

        messageAdapter = new MessageAdapter(this);

        loadHistory();


        userMessageText = findViewById(R.id.userMessageText);
        sendButton = findViewById(R.id.sendButton);
        messages_view = (ListView) findViewById(R.id.messages_view);

        messages_view.setAdapter(messageAdapter);

        ImageButton clickButton = (ImageButton) findViewById(R.id.sendButton);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                attemptSend();
            }
        });
    }

    private void attemptSend()
    {
        try {
            String message = userMessageText.getText().toString().trim();

            String mess = "NOTHING";
            JSONObject json = new JSONObject();
            json.put("id", userID); //EZ
            json.put("recieverID", recieverID); //EZ
            json.put("messageText", message);


            mess = json.toString();
            Log.d("MESSAGE:", mess);
            mSocket.emit("sendMessage", json);

            Message userMess = new Message((int)RegisterActivity.userID, recieverID, recieverThumbnail, recieverName, message, true);
            messageAdapter.add(userMess);
            // scroll the ListView to the last added element
            messages_view.setSelection(messages_view.getCount() - 1);
            userMessageText.setText("");
        }
        catch (Exception e) {
            Log.d("EXC", e.toString());
        }
    }

    Map<String, String> responseMapChats;
    String senderID;
    String recieverIDD="";
    String messageText="";

    ArrayList<Message> arrayList = new ArrayList<>();

    private void loadHistory()
    {
        if(!(token.equals("")))
        {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userID));
            params.put("recieverID", String.valueOf(recieverID));

            RequestQueue queue = Volley.newRequestQueue(this);

            String url = registerurl + "getChatHistoryById";


            responseMapChats = new HashMap<>();
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            System.out.println("co vrati server "+ response1);
                            JSONObject response = null;
                            try {

//                                arrayList.clear();
                                for (int i = 0; i< response1.length(); i++){
                                    response = response1.getJSONObject(i);
                                    System.out.println("response spravny uz "+response);

                                    senderID = response.getString("senderID");

                                    recieverIDD = response.getString("recieverID");

                                    messageText = response.getString("messageText");

                                    responseMapChats.put("senderID", String.valueOf(senderID));
                                    responseMapChats.put("recieverID", recieverIDD);
                                    responseMapChats.put("messageText", messageText);

                                    Message message;
                                    if(Long.parseLong(recieverIDD) == userID)
                                    {
                                        message = new Message(Integer.parseInt(senderID), Integer.parseInt(recieverIDD), recieverThumbnail, recieverName, messageText, false);
                                    }
                                    else
                                        {
                                            message = new Message(Integer.parseInt(senderID), Integer.parseInt(recieverIDD), recieverThumbnail, recieverName, messageText, true);
                                        }


                                    arrayList.add(message);
                                    messageAdapter.add(message);
                                    // scroll the ListView to the last added element
                                    messages_view.setSelection(messages_view.getCount() - 1);
                                    /*
                                    userChatsAdapter.notifyDataSetChanged();
                                    */

                                    if (!responseMapChats.isEmpty()){
                                        Toast.makeText(getBaseContext(), "User posts loaded",Toast.LENGTH_LONG).show();
                                        System.out.println("profile activity som "+ RegisterActivity.userID);
                                    }
                                    else {
                                        Toast.makeText(getBaseContext(), "User posts not loaded",Toast.LENGTH_LONG).show();
                                    }
                                }
                                System.out.println("tu vypisujem size arau: " + arrayList.size());

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
                protected Map<String, String> getParams() {
                    Map<String, String> paramas = new HashMap<String, String>();
                    paramas.put("senderID", senderID);
                    paramas.put("recieverID", recieverIDD);
                    paramas.put("messageText", messageText);

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


    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            ChatWithOneUserActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String messageText = "";
                    try
                    {
                        messageText = data.getString("messageText");
                    } catch (JSONException e) {
                        Log.d("ERROR IN RESPONSE", e.toString());
                    }
                    Message userMess = new Message((int)RegisterActivity.userID, recieverID, recieverThumbnail, recieverName, messageText, false);
                    messageAdapter.add(userMess);
                    // scroll the ListView to the last added element
                    messages_view.setSelection(messages_view.getCount() - 1);
                    userMessageText.setText("");
                }
            });
        }
    };
}
