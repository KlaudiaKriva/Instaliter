package com.example.instaliter;

public class Follower {
    int id;
    String name;
    String instaName;

    public Follower(int id, String name, String instaName) {
        this.id = id;
        this.name = name;
        this.instaName = instaName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstaName() {
        return instaName;
    }

    public void setInstaName(String instaName) {
        this.instaName = instaName;
    }
}
