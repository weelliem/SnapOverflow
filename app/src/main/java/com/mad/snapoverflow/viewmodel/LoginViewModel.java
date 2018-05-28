package com.mad.snapoverflow.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
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

public class LoginViewModel extends BaseObservable {

    private FirebaseAuth mAuth;
    private Context mContext;
    public String mEmailText;
    public String mPasswordText;
    private String mPasswordHint;
    private String mEmailHint;
    private EditText mEmailET;
    private EditText mPasswordET;
    //private ProgressBar mProgressBar;



    public LoginViewModel(UsersLoginModel User, Context context, String password, String email, EditText emailET, EditText passwordET) {
        mEmailHint = User.mEmailhint;
        mPasswordHint = User.mPasswordhint;
        mContext = context;
        mEmailText = email;
        mPasswordText = password;
        mEmailET = emailET;
        mPasswordET = passwordET;
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

    public View.OnClickListener onClickSignUp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpActivity();
            }
        };

    }

    public View.OnClickListener onClickLogin(){
      return new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseLoginAuth(userLoginEmail(),userLoginPassword());
        }
      };
    }

    public String userLoginEmail() {
        String email = getEmailText();

        if(email.isEmpty()){
            mEmailET.setError("Email is required");
            mEmailET.requestFocus();

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email != null){
            mEmailET.setError("Please enter a valid email");
            mEmailET.requestFocus();


        }

        return email;
    }

    public String userLoginPassword(){
        String password = getPasswordText();
        if (password.isEmpty()) {
            mPasswordET.setError("Password is required");
            mPasswordET.requestFocus();

        }

        if (password.length() < 6) {
            mPasswordET.setError("Minimum length of password should be 6 characters");
            mPasswordET.requestFocus();


        }

        return password;
    }



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

    private void launchMainActivity(){
        Intent intent = new Intent(mContext, MainFragmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    private void launchSignUpActivity() {
        mContext.startActivity(new Intent(mContext, SignupActivity.class));
    }

}


