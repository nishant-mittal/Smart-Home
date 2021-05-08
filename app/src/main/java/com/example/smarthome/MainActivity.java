package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    public static ArrayList<Device> deviceArrayList;
    private TextView usernameTextView, temperatureTextView, humidityTextView;
    private RecyclerView recyclerView;
    private DevicesAdapter devicesAdapter;
    private ProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        deviceArrayList = new ArrayList<>();
        usernameTextView = findViewById(R.id.main_user_name);
        temperatureTextView = findViewById(R.id.main_temperature);
        humidityTextView = findViewById(R.id.main_humidity);
        recyclerView = findViewById(R.id.main_devices_recycler_view);
        circularProgressBar = findViewById(R.id.main_circular_progress_bar);


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("users/" + auth.getUid() + "/userInfo");
        DatabaseReference mySensorInfoRef = db.getReference("users/" + auth.getUid() + "/sensorInfo");

        mySensorInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SensorInfo sensorInfo = snapshot.getValue(SensorInfo.class);
                temperatureTextView.setText(sensorInfo.getTemperature() + "°c");
                humidityTextView.setText(sensorInfo.getHumidity() + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        devicesAdapter = new DevicesAdapter(deviceArrayList, this);

        devicesAdapter.setListener(new DevicesAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DeviceDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        devicesAdapter.setSwitchListener(new DevicesAdapter.SwitchListener() {
            @Override
            public void onSwitchClick(int position, boolean state, SwitchMaterial switchMaterial) {
                DatabaseReference deviceRef = db.getReference("users/" + auth.getUid() + "/devices/" + deviceArrayList.get(position).getDeviceName() + "/state");
                //deviceRef.setValue(state);
                if(deviceArrayList.get(position).isStatus()) {
                    if(state) {
                        deviceRef.setValue(1);
                    }
                    else {
                        deviceRef.setValue(0);
                    }
                }
                else {
                    switchMaterial.setChecked(false);
                    Toasty.info(MainActivity.this,"Device is disabled",Toasty.LENGTH_SHORT,false).show();
                }
            }
        });
        recyclerView.setAdapter(devicesAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User username = snapshot.getValue(User.class);
                String[] name = username.getName().split(" ");
                usernameTextView.setText("Hello " + name[0] + " " + getResources().getString(R.string.waving_hand_emoji));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                usernameTextView.setText("Hello user");
            }
        });

        DatabaseReference myDevicesRef = db.getReference("users/" + auth.getUid() + "/devices");

        myDevicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                circularProgressBar.setVisibility(View.GONE);
                deviceArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Device device = dataSnapshot.getValue(Device.class);
                    deviceArrayList.add(device);
                }

                Collections.reverse(deviceArrayList);
                devicesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(MainActivity.this, "Could not load devices", Toast.LENGTH_SHORT, false).show();
            }
        });


    }

    public void onAddADeviceClick(View v) {
        Intent intent = new Intent(MainActivity.this, AddDeviceActivity.class);
        startActivity(intent);
    }

    public void onProfileClick(View v) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}