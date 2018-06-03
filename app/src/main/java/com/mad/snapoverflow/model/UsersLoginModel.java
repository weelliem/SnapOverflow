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

/*view model contains all the logic between view and the models this specific one is responsible for the uploading of images */
public class UsersLoginModel {
    private String mEmail;
    private String mPassword;
    public String emailHint;
    public String passwordHint;

    public UsersLoginModel() {

    }

    /* obtains data from the view and places it within the constructor */
    public UsersLoginModel(String emailhint, String passwordhint) {
        this.emailHint = emailhint;
        passwordHint = passwordhint;
    }
}
