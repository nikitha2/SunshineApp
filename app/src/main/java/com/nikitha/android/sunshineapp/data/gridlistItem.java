package com.nikitha.android.sunshineapp.data;

public class gridlistItem {

    String title;
    String value;

    public gridlistItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
