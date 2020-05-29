package com.nikitha.android.sunshineapp;

import java.io.Serializable;

public class ListItems  implements Serializable {
    String date;

    String temp;
    String temp_kf;
    String temp_max;
    String temp_min;
    String sea_level;
    String pressure;
    String humidity;
    String grnd_level;
    String feels_like;
    String windSpeed;
    String description;

    public ListItems(String date, String temp, String temp_kf, String temp_max, String temp_min, String sea_level, String pressure, String humidity, String grnd_level, String feels_like, String windSpeed, String description) {
        this.date = date;
        this.temp = temp;
        this.temp_kf = temp_kf;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.sea_level = sea_level;
        this.pressure = pressure;
        this.humidity = humidity;
        this.grnd_level = grnd_level;
        this.feels_like = feels_like;
        this.windSpeed = windSpeed;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp_kf() {
        return temp_kf;
    }

    public void setTemp_kf(String temp_kf) {
        this.temp_kf = temp_kf;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getSea_level() {
        return sea_level;
    }

    public void setSea_level(String sea_level) {
        this.sea_level = sea_level;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getGrnd_level() {
        return grnd_level;
    }

    public void setGrnd_level(String grnd_level) {
        this.grnd_level = grnd_level;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
