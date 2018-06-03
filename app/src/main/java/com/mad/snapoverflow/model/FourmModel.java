package com.mad.snapoverflow.model;

public class FourmModel {


    private String mTitle;
    private String mImageUrl;
    private String mContent;
    private String mKey;
    private String mSystemTime;

    public FourmModel(String title, String imageUrl, String content, String systemtime) {
       // this.username = username;
        this.mTitle = title;
        this.mImageUrl = imageUrl;
        this.mContent = content;
        this.mSystemTime = systemtime;
    }

/*    public String getUsername() {
        return username;
    }*/

    public String gettitle() {
        return mTitle;
    }

    public String getimageUrl() {
        return mImageUrl;
    }

    public String getSystemTime() {
        return mSystemTime;
    }

    public void setSystemTime(String systemTime) {
        this.mSystemTime = systemTime;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

  /*  public void setUsername(String username) {
        this.username = username;
    }*/

    public void settitle(String title) {
        this.mTitle = title;
    }

    public void setimageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }


    public FourmModel(){

    }
}


