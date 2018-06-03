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


package com.mad.snapoverflow.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.model.UsersLoginModel;
import com.mad.snapoverflow.view.Activities.SignupActivity;
import com.mad.snapoverflow.view.Activities.MainFragmentActivity;

import static android.support.constraint.Constraints.TAG;

/* this view model handles the logic for the login activity */
public class LoginViewModel extends BaseObservable {

    private FirebaseAuth mAuth;
    private Context mContext;
    public String mEmailText;
    public String mPasswordText;
    private String mPasswordHint;
    private String mEmailHint;
    private EditText mEmailET;
    private EditText mPasswordET;
    private Activity mActivity;



    //private ProgressBar mProgressBar;


/* the constuctor for the login activity */
    public LoginViewModel(UsersLoginModel User, Context context, String password, String email, EditText emailET, EditText passwordET, Activity activity) {
        mEmailHint = User.emailHint;
        mPasswordHint = User.passwordHint;
        mContext = context;
        mEmailText = email;
        mPasswordText = password;
        mEmailET = emailET;
        mPasswordET = passwordET;
        mActivity = activity;
    }

    public String getEmailText() {
        return mEmailText;
    }

    public void setEmailText(String emailText) {
        mEmailText = emailText;
        notifyPropertyChanged(R.id.textEmail);
    }

    public String getPasswordText() {
        return mPasswordText;
    }

    public void setPasswordText(String passwordText) {
        mPasswordText = passwordText;
        notifyPropertyChanged(R.id.textPassword);
    }

    public String getPasswordHint() {
        return mPasswordHint;
    }

    public void setPasswordHint(String passwordHint) {
        mPasswordHint = passwordHint;
    }

    public String getEmailHint() {
        return mEmailHint;
    }

    public void setEmailHint(String emailHint) {
        mEmailHint = emailHint;
    }

    /* launches a new activity to the sign up */
    public View.OnClickListener onClickSignUp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpActivity();
            }
        };

    }

    /* checks firebase autentication and see if the credentials is autheroised to login */
    public View.OnClickListener onClickLogin(){
      return new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseLoginAuth(userLoginEmail(),userLoginPassword());
        }
      };
    }

    /* checks the email inputs are within acceptable rangers for the application */
    public String userLoginEmail() {
        String email = getEmailText();

        if(email.isEmpty()){
            mEmailET.setError(mContext.getResources().getString(R.string.email_error));
            mEmailET.requestFocus();

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email != null){
            mEmailET.setError(mContext.getResources().getString(R.string.email_valid));
            mEmailET.requestFocus();


        }

        return email;
    }

    /* checks the password inputs of the user to see if they are within acceptable rangers */
    public String userLoginPassword(){
        String password = getPasswordText();
        if (password.isEmpty()) {
            mPasswordET.setError(mContext.getResources().getString(R.string.password_error));
            mPasswordET.requestFocus();

        }

        if (password.length() < 6) {
            mPasswordET.setError(mContext.getResources().getString(R.string.min_string));
            mPasswordET.requestFocus();


        }

        return password;
    }




/* the firebse authentication grabs the user imputs and comapres it to the login information stored on firebase */
    public void firebaseLoginAuth(final String email, final String password) {
        mAuth = FirebaseAuth.getInstance();
        if (!email.isEmpty() && !password.isEmpty()) {
           // mProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   // mProgressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {

                        launchMainActivity();
                    } else {
                        Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onComplete: " + email + password);
                    }
                }
            });
        }
    }

    /* launches a new activity */
    private void launchMainActivity(){
        Intent intent = new Intent(mContext, MainFragmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /* launches a new activity */
    private void launchSignUpActivity() {
        mContext.startActivity(new Intent(mContext, SignupActivity.class));
    }

}


