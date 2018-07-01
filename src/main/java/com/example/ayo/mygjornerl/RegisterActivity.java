package com.example.ayo.mygjornerl;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;


public class RegisterActivity extends AppCompatActivity {
    EditText username, password;
    Button login, registerUser;

    FirebaseAuth mAuth;
    ProgressBar progressBarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username = (EditText) findViewById(R.id.emailLog);
        password = (EditText) findViewById(R.id.passLog2);
        mAuth = FirebaseAuth.getInstance();
        progressBarr = (ProgressBar) findViewById(R.id.progressBar_reg);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, loginActivity.class));
            }
        });
        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail, userEpass;
                userEmail = username.getText().toString().trim();
                userEpass = password.getText().toString().trim();


                if (TextUtils.isEmpty(userEmail) & TextUtils.isEmpty(userEpass)) {
                    Toast.makeText(RegisterActivity.this, "email and password empty ", Toast.LENGTH_LONG);
                }
                progressBarr.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(userEmail, userEpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBarr.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account creation failed ", Toast.LENGTH_LONG);

                        } else {
                            startActivity(new Intent(RegisterActivity.this, journalViewActivity.class));
                            finish();
                        }
                    }
                });

            }
        });


    }
}



