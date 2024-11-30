package com.tondz.theodoimaytinhapp.models;

public class History {

    private String url, title, visitCount, lastVisitTime;

    public History() {
    }

    public History(String url, String title, String visitCount, String lastVisitTime) {
        this.url = url;
        this.title = title;
        this.visitCount = visitCount;
        this.lastVisitTime = lastVisitTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(String lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }
}
