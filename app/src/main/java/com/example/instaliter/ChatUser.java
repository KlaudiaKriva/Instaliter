package com.example.instaliter;

public class ChatUser {

    int id;
    String userName;
    String instaName;
    String thumbnailPath;

    public ChatUser(int id, String userName, String instaName, String thumbnailPath) {
        this.id = id;
        this.userName = userName;
        this.instaName = instaName;
        this.thumbnailPath = thumbnailPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setInstaName(String cTime) {
        this.instaName = cTime;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
