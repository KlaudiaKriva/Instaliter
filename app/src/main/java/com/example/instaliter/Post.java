package com.example.instaliter;

public class Post {
    String idI;
    int userName;
    String postImage;
    String thumbnailPath;
    String post_text;
    String date;


    public Post(int userID, String idI, String postImage, String thumbnailPath, String post_text, String date) {
        this.userName = userID;
        this.idI = idI;
        this.postImage = postImage;
        this.thumbnailPath = thumbnailPath;
        this.post_text = post_text;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    public String getIdI() {
        return idI;
    }

    public void setIdI(String idI) {
        this.idI = idI;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
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


}
