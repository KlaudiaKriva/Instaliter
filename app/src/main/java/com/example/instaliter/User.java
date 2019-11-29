package com.example.instaliter;

public class User {
    int id;
    String instaName;
    String userName;
    String profileImage;

    public User(int id, String instaName, String userName, String profileImage) {
        this.id = id;
        this.instaName = instaName;
        this.userName = userName;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstaName() {
        return instaName;
    }

    public void setInstaName(String instaName) {
        this.instaName = instaName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
