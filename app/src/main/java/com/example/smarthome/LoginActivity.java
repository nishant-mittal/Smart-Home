package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.sign_in_email);
        passwordEditText = findViewById(R.id.sign_in_password);
        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void onSignInClick(View v) {
        String email, password;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Enter email!");
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Enter password");
        }

        if (!email.isEmpty() && !password.isEmpty()) {
            if (!(email.contains("@") && email.contains("."))) {
                //Toast.makeText(LoginActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                Toasty.warning(LoginActivity.this, "Enter valid email", Toast.LENGTH_SHORT, true).show();
            }

            if (password.length() < 6) {
                //Toast.makeText(LoginActivity.this, "Password should be longer", Toast.LENGTH_SHORT).show();
                Toasty.warning(LoginActivity.this, "Password should be longer", Toast.LENGTH_SHORT, true).show();
            } else if (email.contains("@") && email.contains(".") && password.length() >= 6) {
                signInButton.setEnabled(false);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user.isEmailVerified()) {
                                        //Toast.makeText(LoginActivity.this, "Logging you in", Toast.LENGTH_SHORT).show();
                                        Toasty.info(LoginActivity.this, "Logging you in", Toast.LENGTH_SHORT, false).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        //Toast.makeText(LoginActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                        Toasty.info(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT, true).show();
                                    }
                                } else {
                                    if (task.getException().getLocalizedMessage().toString().contains("password")) {
                                        //Toast.makeText(LoginActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                                        Toasty.error(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            }
                        });

                //Toasty.info(LoginActivity.this,"Email not verified",Toast.LENGTH_SHORT,true).show();
                signInButton.setEnabled(true);
            }
        }
    }

    public void onCreateAccountClick(View v) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}