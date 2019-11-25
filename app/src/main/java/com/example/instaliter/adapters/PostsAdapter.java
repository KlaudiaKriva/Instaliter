package com.example.instaliter.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instaliter.Post;
import com.example.instaliter.R;

import java.util.ArrayList;

public class PostsAdapter extends BaseAdapter {

    Context context;
    ArrayList<Post> arrayList;

    public PostsAdapter(Context context, ArrayList<Post> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return position;
        return arrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.one_contribution, null);

        System.out.println("convertview: "+ convertView);
        Post post = arrayList.get(position);
        System.out.println("arrayl je: " + arrayList.size());
        System.out.println(post.getId() + post.getPost_text());
        
//        ImageView profileImage = convertView.findViewById(R.id.profileImage);
//        profileImage.setImageURI(Uri.parse(post.getProfileImage()));

        TextView userName = convertView.findViewById(R.id.userName);
        userName.setText(String.valueOf(post.getId()));

//        TextView place = convertView.findViewById(R.id.place);
//        place.setText(post.getPlace());

        ImageView postImage = convertView.findViewById(R.id.postImage);
        System.out.println(postImage + "imageview");
        System.out.println("uri je " +post.getPostImage());
        String path = post.getPostImage();
        System.out.println("path je: "+ path);
        postImage.setImageURI(Uri.parse(path));

        //heart a bubble aku budu mat funkcionalitu, treba zahrnut tu pravdepodobne

        TextView peoples_likes = convertView.findViewById(R.id.peoples_likes);
        //peoples likes cez service

        TextView post_text = convertView.findViewById(R.id.post_text);
        post_text.setText(post.getPost_text());


        System.out.println("som v getview postsadapter " + post_text);
        return convertView;
    }
}
