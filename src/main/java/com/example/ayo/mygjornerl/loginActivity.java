package com.example.ayo.mygjornerl;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class loginActivity extends AppCompatActivity {

    EditText userLmail, userLpass;
    Button logMeIn;
    FirebaseAuth mAuthL;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userLmail = (EditText) findViewById(R.id.userLmail);
        userLpass = (EditText) findViewById(R.id.passw);
        logMeIn = (Button) findViewById(R.id.log_user);
        mAuthL = FirebaseAuth.getInstance();
        progress = (ProgressBar) findViewById(R.id.progressBar_login);

        logMeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail, password;
                mail = userLmail.getText().toString().trim();
                password = userLpass.getText().toString().trim();

                if (TextUtils.isEmpty(mail) & TextUtils.isEmpty(password)) {
                    Toast.makeText(loginActivity.this, "email and password empty ", Toast.LENGTH_LONG);

                }
                progress.setVisibility(View.VISIBLE);
                mAuthL.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, " login failed ", Toast.LENGTH_LONG);

                        } else {
                            startActivity(new Intent(loginActivity.this, journalViewActivity.class));
                            finish();
                        }
                    }

                });
            }

        });

    }
}

