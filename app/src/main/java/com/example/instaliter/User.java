package com.example.instaliter;

public class User {
    String id;
    String userName;
    String instaName;
    String idI;
    String profileImage;
    String thumbnailPath;

    public User(String id, String userName, String instaName, String idI, String profileImage, String thumbnailPath) {
        this.id = id;
        this.userName = userName;
        this.instaName = instaName;
        this.idI = idI;
        this.profileImage = profileImage;
        this.thumbnailPath = thumbnailPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInstaName() {
        return instaName;
    }

    public void setInstaName(String instaName) {
        this.instaName = instaName;
    }

    public String getIdI() {
        return idI;
    }

    public void setIdI(String idI) {
        this.idI = idI;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
