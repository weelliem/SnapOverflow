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

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivitySignupBinding;
import com.mad.snapoverflow.model.UsersSignupModel;
import com.mad.snapoverflow.viewmodel.SignUpViewModel;

/* this class handles the view the layout of the registration activity */
public class SignupActivity extends AppCompatActivity {

    public String editTextEmail, editTextPassword,editTextUni, editTextDate, editTextAoi, editTextUsername;
    public ProgressBar progressSign;
    private ActivitySignupBinding mBinding;
    private SignUpViewModel mViewModel;


    /* on create lifecycle which handls the binding  */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_signup);

        editTextEmail = mBinding.textEmailReg.getText().toString();
        editTextPassword = mBinding.textPasswordReg.getText().toString();
        progressSign = mBinding.progressSign;
        editTextAoi = mBinding.textAoI.getText().toString();
        editTextDate = mBinding.textDate.getText().toString();
        editTextUsername = mBinding.textUsername.getText().toString();
        editTextUni = mBinding.textUniversity.getText().toString();

        mViewModel = new SignUpViewModel(this, editTextEmail, editTextPassword,
                editTextAoi, editTextDate, editTextUni, editTextUsername,progressSign, mBinding.textEmailReg, mBinding.textPasswordReg);
        mBinding.setSignupVM(mViewModel);






    }



}
