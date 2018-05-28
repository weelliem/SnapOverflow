package com.mad.snapoverflow.view.Activities;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;
    private LoginViewModel mViewModel;
    private String mEditTextEmail;
    private String mEditTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mEditTextEmail = mBinding.textEmail.getText().toString();
        mEditTextPassword = mBinding.textPassword.getText().toString();
        mViewModel = new LoginViewModel(new UsersLoginModel("Email","Password"), this, mEditTextPassword, mEditTextEmail,mBinding.textEmail, mBinding.textPassword);
        mBinding.setLoginViewModel(mViewModel);

    }



}
