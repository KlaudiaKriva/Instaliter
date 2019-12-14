package com.example.instaliter;

public class LoggedUser {

    private static int id;
    private static String email;
    private static String token;
    private static LoggedUser instance = new LoggedUser();
    private LoggedUser(){}
    public static LoggedUser getInstance() {
        return instance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public  int getId() {
        return id;
    }

    public  String getToken() {
        return token;
    }
}
