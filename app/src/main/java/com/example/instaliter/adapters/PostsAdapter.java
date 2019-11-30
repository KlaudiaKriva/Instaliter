package com.example.instaliter.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instaliter.DatabaseHelper;
import com.example.instaliter.Post;
import com.example.instaliter.R;
import com.example.instaliter.RegisterActivity;

import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Post post = postArrayList.get(position);

        holder.userName.setText(RegisterActivity.userName);
        holder.time.setText(post.getDate());
        holder.post_text.setText(post.getPost_text());
        String path ="http://192.168.0.101:5005/"+post.getPostImage();
        System.out.println("path "+path);
        glide.load(path).into(holder.postImage);

        holder.heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("klikkkk");
                    buttonView.setBackground(context.getResources().getDrawable(R.drawable.icon_heart2));
                }
                else
                    buttonView.setBackground(context.getResources().getDrawable(R.drawable.heart_icon));
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
