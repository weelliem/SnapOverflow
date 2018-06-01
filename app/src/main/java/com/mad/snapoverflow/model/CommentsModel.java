package com.mad.snapoverflow.model;

public class CommentsModel {

    private String text;

    public CommentsModel(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CommentsModel() {


    }

}
