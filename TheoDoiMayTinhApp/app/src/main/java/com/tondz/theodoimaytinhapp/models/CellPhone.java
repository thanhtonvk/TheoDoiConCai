package com.tondz.theodoimaytinhapp.models;

public class CellPhone {
    private String time,url;

    public CellPhone() {
    }

    public CellPhone(String time, String url) {
        this.time = time;
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
