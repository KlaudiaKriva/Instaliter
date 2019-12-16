package com.example.instaliter.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.R;
import com.example.instaliter.User;
import com.example.instaliter.activities.ChatWithOneUserActivity;
import com.example.instaliter.activities.ProfileOfOthersActivity;

import java.util.ArrayList;

import static com.example.instaliter.RegisterActivity.registerurl;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private Context context;
    private ArrayList<User> fullArrayList;
    RequestManager glide;

    public UsersAdapter(Context context, ArrayList<User> fullArrayList) {
        this.context = context;
        this.fullArrayList = fullArrayList;
        glide = Glide.with(context);

    }
    public class UsersViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profileImage;
        TextView tv_instaName;
        TextView tv_userName;
        RelativeLayout rl_User;
        Button btn_user;

        public UsersViewHolder(View itemView) {
            super(itemView);
            iv_profileImage = itemView.findViewById(R.id.userPhotoMessage);
            tv_instaName = itemView.findViewById(R.id.textview);
            tv_userName = itemView.findViewById(R.id.textview2);
            rl_User = itemView.findViewById(R.id.lay_ofOneUser);
            btn_user = itemView.findViewById(R.id.send_mess);
        }
    }


    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        glide = Glide.with(context);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user, parent, false);
        return new UsersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, final int position) {
        final User currentUser = fullArrayList.get(position);

        if(!(currentUser.getProfileImage() == null || currentUser.getProfileImage().equals("null") || currentUser.getProfileImage().equals(""))) {
            System.out.println("vypis mi currentuser pfoileimage: " + currentUser.getProfileImage());
            glide.load(registerurl + currentUser.getProfileImage()).into(holder.iv_profileImage);
//            holder.iv_profileImage.setImageResource(Integer.parseInt(currentUser.getProfileImage()));
        }
        else{
            glide.load(R.drawable.profile_pic).into(holder.iv_profileImage);
        }
//        holder.iv_profileImage.setImageResource(Integer.parseInt(currentUser.getProfileImage()));
        holder.tv_instaName.setText(currentUser.getInstaName());
        holder.tv_userName.setText(currentUser.getUserName());

        holder.rl_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileOfOthersActivity.otherUserID = (int) getItemId(position);
                context.startActivity(new Intent(context, ProfileOfOthersActivity.class));
            }
        });

        holder.btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatWithOneUserActivity.class));
            }
        });




    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(fullArrayList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return fullArrayList.size();
    }

}
