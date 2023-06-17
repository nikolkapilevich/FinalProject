package com.example.finalproject;

public class DataClass {

    private String imageURL , caption , userId;

    public DataClass(String imageURL, String caption , String userId) {
        this.imageURL = imageURL;
        this.caption = caption;
        this.userId = userId;
    }

    public DataClass () {

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
