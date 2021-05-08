package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private Button logoutButton;
    private TextView nameTextView, emailTextView, totalDevices, activeDevices;
    private ArrayList<Device> devicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        logoutButton = findViewById(R.id.log_out_button);

        nameTextView = findViewById(R.id.profile_name);
        emailTextView = findViewById(R.id.profile_email);
        totalDevices = findViewById(R.id.profile_total_devices);
        activeDevices = findViewById(R.id.profile_active_devices);

        devicesList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        DatabaseReference userInfoRef = db.getReference("users/" + auth.getUid() + "/userInfo");
        DatabaseReference userDevicesRef = db.getReference("users/" + auth.getUid() + "/devices");

        userInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                nameTextView.setText(getResources().getString(R.string.name));
                emailTextView.setText(getResources().getString(R.string.email_ex));
            }
        });

        userDevicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                devicesList.clear();

                int numberOfActiveDevices = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Device device = dataSnapshot.getValue(Device.class);
                    devicesList.add(device);
                }

                for (Device d : devicesList) {
                    if (d.getState() == 1) {
                        numberOfActiveDevices++;
                    }
                }
                totalDevices.setText("" + devicesList.size());
                activeDevices.setText("" + numberOfActiveDevices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                totalDevices.setText(getResources().getString(R.string.zero));
                activeDevices.setText(getResources().getString(R.string.zero));
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onCloseClick(View v) {
        super.onBackPressed();
    }

    public void onAboutAppClick(View v) {
        //Toast.makeText(ProfileActivity.this,"Made by Nishant Mittal",Toast.LENGTH_SHORT).show();
        Toasty.custom(ProfileActivity.this, R.string.made_by, getDrawable(R.drawable.ic_profile), R.color.red, Toasty.LENGTH_SHORT, true, true).show();
    }

}