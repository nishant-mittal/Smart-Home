package com.example.smarthome;

public class Device {
    private String deviceName;
    private String devicePinNumber;
    private String deviceType;
    private boolean status;
    private int state;

    public Device(String deviceName, String devicePinNumber, String deviceType, int state) {
        this.deviceName = deviceName;
        this.devicePinNumber = devicePinNumber;
        this.deviceType = deviceType;
        this.status = true;
        this.state = state;
    }

    public Device() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevicePinNumber() {
        return devicePinNumber;
    }

    public void setDevicePinNumber(String devicePinNumber) {
        this.devicePinNumber = devicePinNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}