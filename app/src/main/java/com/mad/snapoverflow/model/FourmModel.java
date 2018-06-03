/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      Author: 11025400
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.mad.snapoverflow.model;


/* this model contains the getter and setters for the fourm fragement activity and also allows the link between firebase and the application */
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


