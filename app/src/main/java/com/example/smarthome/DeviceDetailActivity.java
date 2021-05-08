package com.example.smarthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class DeviceDetailActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private ImageView deviceIcon;
    private TextView deviceStatus, deviceName, devicePinNumber;
    private Button deviceToggle;
    private ImageButton deviceDelete;
    private LinearLayout deviceIconLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        int position = getIntent().getExtras().getInt("position");

        Device device = MainActivity.deviceArrayList.get(position);

        deviceIcon = findViewById(R.id.device_detail_icon);
        deviceStatus = findViewById(R.id.device_detail_status);
        deviceName = findViewById(R.id.device_detail_name);
        devicePinNumber = findViewById(R.id.device_detail_pin_number);
        deviceToggle = findViewById(R.id.device_detail_toggle);
        deviceDelete = findViewById(R.id.device_detail_delete);
        deviceIconLayout = findViewById(R.id.device_detail_icon_layout);

        if(device.isStatus()) {
            deviceStatus.setText(R.string.status_enabled);
            deviceToggle.setText(R.string.disable_device);
        }
        if(!device.isStatus()) {
            deviceStatus.setText(R.string.status_disabled);
            deviceToggle.setBackgroundColor(getResources().getColor(R.color.green));
            deviceToggle.setText(R.string.enable_device);
        }

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("users/" + auth.getUid() + "/devices/" + device.getDeviceName() + "/status");
        DatabaseReference myRefState = db.getReference("users/" + auth.getUid() + "/devices/" + device.getDeviceName() + "/state");

        if (device.getDeviceType().equals("fan")) {
            deviceIcon.setBackgroundResource(R.drawable.fan_large);
            deviceIconLayout.setBackgroundColor(Color.parseColor("#8854D0"));
        }

        if (device.getDeviceType().equals("bulb")) {
            deviceIcon.setBackgroundResource(R.drawable.bulb_large);
            deviceIconLayout.setBackgroundColor(Color.parseColor("#FA8231"));
        }

        if (device.getDeviceType().equals("tv")) {
            deviceIcon.setBackgroundResource(R.drawable.television_large);
            deviceIconLayout.setBackgroundColor(Color.parseColor("#3867D6"));
        }

        if (device.getDeviceType().equals("fridge")) {
            deviceIcon.setBackgroundResource(R.drawable.fridge_large);
            deviceIconLayout.setBackgroundColor(Color.parseColor("#6AB04C"));
        }

//        if (device.getState() == 1) {
//            deviceStatus.setText(R.string.status_on);
//            deviceToggle.setText(R.string.disable_device);
//        }
//
//        if (device.getState() == 0) {
//            deviceStatus.setText(R.string.status_off);
//            deviceToggle.setText(R.string.enable_device);
//        }

        deviceName.setText(device.getDeviceName());
        devicePinNumber.setText(device.getDevicePinNumber());

        deviceToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue(!device.isStatus());

                if (!device.isStatus()) {
                    device.setStatus(true);
                    deviceStatus.setText(R.string.status_enabled);
                    //deviceToggle.setText(R.string.turn_off);
                    deviceToggle.setBackgroundColor(getResources().getColor(R.color.red));
                    deviceToggle.setText(R.string.disable_device);
                } else {
                    device.setStatus(false);
                    deviceStatus.setText(R.string.status_disabled);
                    //deviceToggle.setText(R.string.turn_on);
                    myRefState.setValue(0);
                    deviceToggle.setBackgroundColor(getResources().getColor(R.color.green));
                    deviceToggle.setText(R.string.enable_device);
                }
            }
        });

        deviceDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myDeviceRef = db.getReference("users/" + auth.getUid() + "/devices/" + device.getDeviceName());
                myDeviceRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(DeviceDetailActivity.this, "Device deleted", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(DeviceDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(DeviceDetailActivity.this, "Device could not be deleted", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(DeviceDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }

    public void onCloseClick(View v) {
        super.onBackPressed();
    }
}