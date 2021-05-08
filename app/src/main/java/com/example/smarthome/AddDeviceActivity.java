package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class AddDeviceActivity extends AppCompatActivity {
    private LinearLayout fanButton, bulbButton, tvButton, fridgeButton;
    private EditText deviceNameEditText, pinNumberEditText;
    private boolean fanStatus, bulbStatus, tvStatus, fridgeStatus;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button addDeviceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        deviceNameEditText = findViewById(R.id.add_device_name);
        pinNumberEditText = findViewById(R.id.add_device_pin_number);

        fanButton = findViewById(R.id.fan_button);
        bulbButton = findViewById(R.id.bulb_button);
        tvButton = findViewById(R.id.tv_button);
        fridgeButton = findViewById(R.id.fridge_button);
        addDeviceButton = findViewById(R.id.add_device_add_button);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        fanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!fanStatus && !bulbStatus && !tvStatus && !fridgeStatus) {
//                    fanButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_fan));
//                    fanButton.setElevation(15f);
//                } else {
//                    fanButton.setBackgroundColor(getResources().getColor(R.color.purple));
//                }
//
//                if (!bulbStatus && !tvStatus && !fridgeStatus) {
//                    fanStatus = !fanStatus;
//                }
                if (bulbStatus || tvStatus || fridgeStatus) {
                    bulbButton.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvButton.setBackgroundColor(getResources().getColor(R.color.blue));
                    fridgeButton.setBackgroundColor(getResources().getColor(R.color.green));
                }

                fanStatus = true;
                bulbStatus = tvStatus = fridgeStatus = false;
                fanButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_fan));

            }
        });

        bulbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!fanStatus && !bulbStatus && !tvStatus && !fridgeStatus) {
//                    bulbButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_bulb));
//                    bulbButton.setElevation(15f);
//                } else {
//                    bulbButton.setBackgroundColor(getResources().getColor(R.color.orange));
//                }
//
//                if (!fanStatus && !tvStatus && !fridgeStatus) {
//                    bulbStatus = !bulbStatus;
//                }
                if (fanStatus || tvStatus || fridgeStatus) {
                    fanButton.setBackgroundColor(getResources().getColor(R.color.purple));
                    tvButton.setBackgroundColor(getResources().getColor(R.color.blue));
                    fridgeButton.setBackgroundColor(getResources().getColor(R.color.green));
                }

                bulbStatus = true;
                fanStatus = tvStatus = fridgeStatus = false;
                bulbButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_bulb));

            }
        });

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!fanStatus && !bulbStatus && !tvStatus && !fridgeStatus) {
//                    tvButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_tv));
//                    tvButton.setElevation(15f);
//                } else {
//                    tvButton.setBackgroundColor(getResources().getColor(R.color.blue));
//                }
//
//                if (!fanStatus && !bulbStatus && !fridgeStatus) {
//                    tvStatus = !tvStatus;
//                }

                if (fanStatus || bulbStatus || fridgeStatus) {
                    fanButton.setBackgroundColor(getResources().getColor(R.color.purple));
                    bulbButton.setBackgroundColor(getResources().getColor(R.color.orange));
                    fridgeButton.setBackgroundColor(getResources().getColor(R.color.green));
                }

                tvStatus = true;
                fanStatus = bulbStatus = fridgeStatus = false;
                tvButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_tv));
            }
        });

        fridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!fanStatus && !bulbStatus && !tvStatus && !fridgeStatus) {
//                    fridgeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_fridge));
//                    fridgeButton.setElevation(15f);
//                } else {
//                    fridgeButton.setBackgroundColor(getResources().getColor(R.color.green));
//                }
//
//                if (!fanStatus && !bulbStatus && !tvStatus) {
//                    fridgeStatus = !fridgeStatus;
//                }

                if (fanStatus || bulbStatus || tvStatus) {
                    fanButton.setBackgroundColor(getResources().getColor(R.color.purple));
                    bulbButton.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvButton.setBackgroundColor(getResources().getColor(R.color.blue));
                }

                fridgeStatus = true;
                fanStatus = bulbStatus = tvStatus = false;
                fridgeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_button_fridge));
            }
        });
    }

    public void onAddDeviceClick(View v) {
        String deviceName, pinNumber;

        deviceName = deviceNameEditText.getText().toString();
        pinNumber = pinNumberEditText.getText().toString();

        if (!TextUtils.isEmpty(deviceName) && !TextUtils.isEmpty(pinNumber) && (fanStatus || bulbStatus || tvStatus || fridgeStatus)) {
            addDeviceButton.setEnabled(false);
            String deviceType = "";
            if (fanStatus)
                deviceType = "fan";
            if (bulbStatus)
                deviceType = "bulb";
            if (tvStatus)
                deviceType = "tv";
            if (fridgeStatus)
                deviceType = "fridge";
            Device device = new Device(deviceName, pinNumber, deviceType, 0);
            myRef = database.getReference("users/" + mAuth.getUid() + "/devices" + "/" + deviceName);
            myRef.setValue(device).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    addDeviceButton.setEnabled(true);
                    //Toast.makeText(AddDeviceActivity.this,"Device added",Toast.LENGTH_SHORT).show();
                    Toasty.success(AddDeviceActivity.this, "Device added", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(AddDeviceActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addDeviceButton.setEnabled(true);
                    //Toast.makeText(AddDeviceActivity.this, "Device could not be added", Toast.LENGTH_SHORT).show();
                    Toasty.error(AddDeviceActivity.this, "Device could not be added", Toast.LENGTH_SHORT, true).show();
                }
            });
        } else {
            if (TextUtils.isEmpty(deviceName)) {
                deviceNameEditText.setError("Enter device name");
            }

            if (TextUtils.isEmpty(pinNumber)) {
                pinNumberEditText.setError("Enter device pin number");
            }
            if (!fanStatus && !bulbStatus && !tvStatus && !fridgeStatus) {
                //Toast.makeText(AddDeviceActivity.this, "Select a device!", Toast.LENGTH_SHORT).show();
                Toasty.warning(AddDeviceActivity.this, "Select a device!", Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    public void onCloseClick(View v) {
        super.onBackPressed();
    }
}