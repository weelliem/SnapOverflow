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

public class SignupActivity extends AppCompatActivity {

    public String editTextEmail, editTextPassword,editTextUni, editTextDate, editTextAoi, editTextUsername;
    public ProgressBar progressSign;
    private ActivitySignupBinding mBinding;
    private SignUpViewModel mViewModel;



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
