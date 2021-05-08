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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, phoneNumberEditText,passwordEditText, retypePasswordEditText;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef, sensorInfoRef;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        nameEditText = findViewById(R.id.sign_up_name);
        emailEditText = findViewById(R.id.sign_up_email);
        passwordEditText = findViewById(R.id.sign_up_password);
        retypePasswordEditText = findViewById(R.id.sign_up_retype_password);
        signUpButton = findViewById(R.id.sign_up_button);
        phoneNumberEditText = findViewById(R.id.sign_up_phone_number);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void onSignUpClick(View v) {
        String name, email, password, repassword, phoneNumber;

        name = nameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        repassword = retypePasswordEditText.getText().toString();
        phoneNumber = phoneNumberEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Enter name!");
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Enter email!");
        }

        if(TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Enter phone number!");
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Enter password!");
        }

        if (TextUtils.isEmpty(repassword)) {
            retypePasswordEditText.setError("Enter password again!");
        }

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !repassword.isEmpty() && !phoneNumber.isEmpty()) {
            if (!(email.contains("@") && email.contains("."))) {
                //Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                Toasty.warning(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT, true).show();
            }

            if(phoneNumber.length() != 10) {
                Toasty.warning(SignUpActivity.this, "Enter valid phone number", Toast.LENGTH_SHORT, true).show();
            }

            if (!(password.equals(repassword))) {
                //Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                Toasty.warning(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT, true).show();
            }

            if(password.length() < 6) {
                //Toast.makeText(SignUpActivity.this,"Password should be longer",Toast.LENGTH_SHORT).show();
                Toasty.warning(SignUpActivity.this, "Password should be longer", Toast.LENGTH_SHORT, true).show();
            }

            else if(email.contains("@") && email.contains(".") && password.equals(repassword) && password.length() >= 6) {
                signUpButton.setEnabled(false);
                Toasty.info(SignUpActivity.this,"Creating account",Toasty.LENGTH_SHORT,false).show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(name, email, phoneNumber);
                                    sensorInfoRef = database.getReference("users/" + mAuth.getUid() + "/sensorInfo");
                                    sensorInfoRef.setValue(new SensorInfo(0, 0, 0));
                                    myRef = database.getReference("users/" + mAuth.getUid() + "/userInfo");
                                    myRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toasty.success(SignUpActivity.this, "Account created", Toast.LENGTH_SHORT, true).show();
                                            mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                                    Toasty.info(SignUpActivity.this, "Confirmation email sent to your inbox", Toast.LENGTH_SHORT, true).show();
                                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Toast.makeText(SignUpActivity.this, "Account could not be created", Toast.LENGTH_SHORT).show();
                                                    Toasty.error(SignUpActivity.this, "Account could not be created", Toast.LENGTH_SHORT, true).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Toast.makeText(SignUpActivity.this, "Account could not be created", Toast.LENGTH_SHORT).show();
                                            Toasty.error(SignUpActivity.this, "Account could not be created", Toast.LENGTH_SHORT, true).show();
                                        }
                                    });
                                }
                            }
                        });
                signUpButton.setEnabled(true);
            }
        }
    }
}