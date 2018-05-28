package com.mad.snapoverflow.model;

public class FourmModel {

    /*private String username;*/
    private String title;
    private String imageUrl;

    public FourmModel(String title, String imageUrl) {
       // this.username = username;
        this.title = title;
        this.imageUrl = imageUrl;
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


