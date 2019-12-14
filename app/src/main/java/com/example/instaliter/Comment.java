package com.example.instaliter;

public class Comment {
    String name;
    int idI;
    String cTime;
    String commenttext;
    int idOfUser;

    public Comment(String name, int idI, int idOfUser, String cTime, String commenttext) {
        this.name = name;
        this.idI = idI;
        this.idOfUser = idOfUser;
        this.cTime = cTime;
        this.commenttext = commenttext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdI() {
        return idI;
    }

    public void setIdI(int idI) {
        this.idI = idI;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }

    public int getIdOfUser() {
        return idOfUser;
    }

    public void setIdOfUser(int idOfUser) {
        this.idOfUser = idOfUser;
    }
}
