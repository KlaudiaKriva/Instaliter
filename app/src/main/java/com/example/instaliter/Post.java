package com.example.instaliter;

public class Post {
    int id;
    int userName;
    String datum;
    String postImage;
    String post_text;
    String user;


    public Post(int id, int userID, String datum, String postImage, String post_text) {
        this.id = id;
        this.userName = userID;
        this.datum = datum;
        this.postImage = postImage;
        this.post_text = post_text;
    }

    public void setId(int id) {
        this.id = id;
    }




    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }


    public String getPostImage() {
        return postImage;
    }

    public String getPost_text() {
        return post_text;
    }

    public int getId() {
        return id;
    }
}
