package com.example.instaliter;

public class Post {
    int id;
    String userName;
    String profileImage;
    String place;
    String postImage;
    String post_text;

    public Post(int id, String userName, String profileImage, String place, String postImage, String post_text) {
        this.id = id;
        this.userName = userName;
        this.profileImage = profileImage;
        this.place = place;
        this.postImage = postImage;
        this.post_text = post_text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getPlace() {
        return place;
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
