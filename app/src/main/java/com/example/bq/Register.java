package com.example.bq;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bq.profiletest.DataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.loginBtn);
        mLoginBtn = findViewById(R.id.createText);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = mFullName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                //Check if the entered data is valid
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must contain at least 6 characters");
                    return;
                }
                if (fullName.length() < 6) {
                    mFullName.setError("Full name must contain at least 6 characters");
                    return;
                }
                if (password.length() > 16) {
                    mPassword.setError("Password must contain at most 16 characters");
                    return;
                }
                if (fullName.length() > 20) {
                    mFullName.setError("Full name must contain at most 20 characters");
                    return;
                }
                mRegisterBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                //register the user

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String userID = user.getUid();

                            DataManager.getInstance().createUserInDatabase(userID, fullName);
                            FirebaseAuth.getInstance().getCurrentUser()
                                    .updateProfile(new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullName).build());

                            // Sign in success, display message, redirect to Main activity
                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            progressBar.setVisibility(View.GONE);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Registration failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            mRegisterBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

    }
}
