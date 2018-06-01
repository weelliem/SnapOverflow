package com.mad.snapoverflow.model;

public class FourmModel {

    /*private String username;*/
    private String title;
    private String imageUrl;
    private String content;
    private String key;
    private String systemtime;

    public FourmModel(String title, String imageUrl, String content, String systemtime) {
       // this.username = username;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.systemtime = systemtime;
    }

/*    public String getUsername() {
        return username;
    }*/

    public String gettitle() {
        return title;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public String getSystemtime() {
        return systemtime;
    }

    public void setSystemtime(String systemtime) {
        this.systemtime = systemtime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

  /*  public void setUsername(String username) {
        this.username = username;
    }*/

    public void settitle(String title) {
        this.title = title;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public FourmModel(){

    }
}


