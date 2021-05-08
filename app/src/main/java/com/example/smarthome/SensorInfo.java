package com.example.smarthome;

public class SensorInfo {
    private int temperature;
    private int humidity;
    private int smoke;

    public SensorInfo(int temperature, int humidity, int smoke) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.smoke = smoke;
    }

    public SensorInfo() {
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getSmoke() {
        return smoke;
    }

    public void setSmoke(int smoke) {
        this.smoke = smoke;
    }
}
