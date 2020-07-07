package com.nikitha.android.sunshineapp.database;


import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "task")
public class TaskEntry implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Weatherid")
    String weatherid;
    @ColumnInfo(name = "Date_txt")
    String date_txt;
    @ColumnInfo(name = "Time")
    String time;
    @ColumnInfo(name = "temperature")
    String temp;
    @ColumnInfo(name = "temperatureF")
    String temp_kf;
    @ColumnInfo(name = "temperatureMax")
    String temp_max;
    @ColumnInfo(name = "temperatureMin")
    String temp_min;
    @ColumnInfo(name = "seaLevel")
    String sea_level;
    @ColumnInfo(name = "Pressure")
    String pressure;
    @ColumnInfo(name = "Humidity")
    String humidity;
    @ColumnInfo(name = "GroundLevel")
    String grnd_level;
    @ColumnInfo(name = "FeelsLike")
    String feels_like;
    @ColumnInfo(name = "WindSpeed")
    String windSpeed;
    @ColumnInfo(name = "Description")
    String description;


    public TaskEntry(String date_txt, String time, String temp, String temp_kf, String temp_max, String temp_min, String sea_level, String pressure, String humidity, String grnd_level, String feels_like, String windSpeed, String description,String weatherid) {
       // this.id = id;
        this.time = time;
        this.date_txt = date_txt;
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
        this.weatherid=weatherid;
    }

    @Ignore
    public TaskEntry(String date_txt,String temp_max, String temp_min) {
        this.date_txt = date_txt;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

        public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_txt() {
        return date_txt;
    }
    public String getWeatherid() {
        return weatherid;
    }

    public void setWeatherid(String weatherid) {
        this.weatherid = weatherid;
    }

    public void setDate_txt(String date_txt) {
        this.date_txt = date_txt;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    /*@Ignore
    public TaskEntry(String description, int priority, Date updatedAt) {
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;
    }*/




}
