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


package com.mad.snapoverflow.view.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivityLoginBinding;
import com.mad.snapoverflow.model.UsersLoginModel;
import com.mad.snapoverflow.viewmodel.LoginViewModel;

import static android.support.constraint.Constraints.TAG;

/* this class is manages the authentication and log in with the application*/
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;
    private LoginViewModel mViewModel;
    private String mEditTextEmail;
    private String mEditTextPassword;
    private static final String EMAILHINT = "Email";
    private static final String PASSWORDHINT = "Password";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_CODE = 2991;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login); //bindings to the activity
        mEditTextEmail = mBinding.textEmail.getText().toString();
        mEditTextPassword = mBinding.textPassword.getText().toString();
        mViewModel = new LoginViewModel(new UsersLoginModel(EMAILHINT, PASSWORDHINT), this, mEditTextPassword, mEditTextEmail, mBinding.textEmail, mBinding.textPassword, this );
        mBinding.setLoginViewModel(mViewModel);

        //permissions for the application
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this,FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }
            else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this,permissions,LOCATION_REQUEST_CODE);
        }

    }


}
