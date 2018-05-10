package com.mad.snapoverflow;

import android.content.Intent;
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

public class signupActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword,editTextUni, editTextDate, editTextAoi, editTextUsername;

    ProgressBar progressSign;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        editTextEmail = findViewById(R.id.textEmailReg);
        editTextPassword = findViewById(R.id.textPasswordReg);
        progressSign = findViewById(R.id.progressSign);
        editTextAoi = findViewById(R.id.textAoI);
        editTextDate = findViewById(R.id.textDate);
        editTextUsername = findViewById(R.id.textUsername);
        editTextUni = findViewById(R.id.textUniversity);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonRegister).setOnClickListener(this);
        findViewById(R.id.texttLogin).setOnClickListener(this);

    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String university = editTextUni.getText().toString().trim();
        final String aoi = editTextAoi.getText().toString().trim();
        final String date = editTextDate.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Minimum length of password should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressSign.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressSign.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    Users users = new Users (
                            username,
                            email,
                            password,
                            university,
                            aoi,
                            date
                    );


                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Intent intent = new Intent(signupActivity.this, loginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                Toast.makeText(getApplicationContext(), "User Registered Successful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Email already Exists",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegister:
                registerUser();
                break;

            case R.id.texttLogin:
                startActivity(new Intent(this,loginActivity.class));
                break;

        }

    }

}
