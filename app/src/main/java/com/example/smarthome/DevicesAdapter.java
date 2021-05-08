package com.example.smarthome;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    private ArrayList<Device> deviceArrayList;
    private Context context;
    private Listener listener;
    private SwitchListener switchListener;

    public DevicesAdapter(ArrayList<Device> deviceArrayList, Context context) {
        this.deviceArrayList = deviceArrayList;
        this.context = context;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSwitchListener(SwitchListener switchListener) {
        this.switchListener = switchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = deviceArrayList.get(position);

        if (device.getDeviceType().equals("fan")) {
            holder.deviceIcon.setBackgroundResource(R.drawable.fan_large);
            holder.deviceLayout.setBackgroundColor(Color.parseColor("#8854D0"));
        } else if (device.getDeviceType().equals("bulb")) {
            holder.deviceIcon.setBackgroundResource(R.drawable.bulb_large);
            holder.deviceLayout.setBackgroundColor(Color.parseColor("#FA8231"));
        } else if (device.getDeviceType().equals("tv")) {
            holder.deviceIcon.setBackgroundResource(R.drawable.television_large);
            holder.deviceLayout.setBackgroundColor(Color.parseColor("#3867D6"));
        } else {
            holder.deviceIcon.setBackgroundResource(R.drawable.fridge_large);
            holder.deviceLayout.setBackgroundColor(Color.parseColor("#6AB04C"));
        }
        //holder.deviceIcon.setBackgroundResource(R.drawable.fan_large);
        holder.deviceName.setText(device.getDeviceName());

        holder.deviceSwitch.setChecked(device.getState() != 0);


        holder.deviceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

        holder.deviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchListener != null) {
                    switchListener.onSwitchClick(position, b, holder.deviceSwitch);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceArrayList.size();
    }

    interface Listener {
        void onItemClick(int position);
    }

    interface SwitchListener {
        void onSwitchClick(int position, boolean state, SwitchMaterial switchMaterial);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView deviceIcon;
        private TextView deviceName;
        private SwitchMaterial deviceSwitch;
        private CardView deviceCard;
        private LinearLayout deviceLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceCard = itemView.findViewById(R.id.device_card);
            deviceIcon = itemView.findViewById(R.id.device_card_icon);
            deviceName = itemView.findViewById(R.id.device_card_device_name);
            deviceSwitch = itemView.findViewById(R.id.device_card_switch);
            deviceLayout = itemView.findViewById(R.id.device_card_layout);
        }
    }
}
