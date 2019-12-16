package com.example.instaliter.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.ChatUser;
import com.example.instaliter.R;
import com.example.instaliter.activities.ChatWithOneUserActivity;

import java.util.ArrayList;

public class UserChatsAdapter extends RecyclerView.Adapter<UserChatsAdapter.ChatsViewHolder>{

    Context context;
    ArrayList<ChatUser> chatArrayList = new ArrayList<>();
    RequestManager glide;

    public UserChatsAdapter(Context context, ArrayList<ChatUser> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
        glide = Glide.with(context);
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageview_message;
        TextView user_mess;
        TextView text_mess;
        RelativeLayout lay_ofOneUser;

        public ChatsViewHolder(View itemView) {
            super(itemView);
            imageview_message = itemView.findViewById(R.id.userPhotoMessage);
            user_mess = itemView.findViewById(R.id.user_mess);
            text_mess = itemView.findViewById(R.id.text_mess);
            lay_ofOneUser = itemView.findViewById(R.id.lay_ofOneUser);
        }
    }


    @Override
    public UserChatsAdapter.ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        glide = Glide.with(context);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_message, parent, false);
        return new UserChatsAdapter.ChatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatsAdapter.ChatsViewHolder holder, final int position) {
        final ChatUser chatUser = chatArrayList.get(position);
        holder.user_mess.setText(chatUser.getUserName());
        holder.text_mess.setText(chatUser.getInstaName());

        holder.lay_ofOneUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatWithOneUserActivity.class);
                intent.putExtra("receiverID", chatUser.getId());
                intent.putExtra("receiverThumbnail", chatUser.getThumbnailPath());
                intent.putExtra("receiveName", chatUser.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return chatArrayList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }
}
