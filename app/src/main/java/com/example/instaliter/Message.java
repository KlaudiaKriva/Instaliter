package com.example.instaliter;

public class Message {

    private int id;
    private int recieverID;
    private String recieverThumbnail;
    private String recieverName;
    private String messageText;
    private boolean belongsToCurrentUser;


    public Message(int id, int recieverID, String recieverThumbnail, String recieverName, String messageText, boolean belongsToCurrentUser) {
        this.id = id;
        this.recieverID = recieverID;
        this.recieverThumbnail = recieverThumbnail;
        this.recieverName = recieverName;
        this.messageText = messageText;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(int recieverID) {
        this.recieverID = recieverID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getRecieverThumbnail() {
        return recieverThumbnail;
    }

    public void setRecieverThumbnail(String recieverThumbnail) {
        this.recieverThumbnail = recieverThumbnail;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }
}
