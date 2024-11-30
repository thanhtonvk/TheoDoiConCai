package com.tondz.theodoimaytinhapp.models;

public class Download {
    private String targetPath, totalBytes, time;

    public Download(String targetPath, String totalBytes, String time) {
        this.targetPath = targetPath;
        this.totalBytes = totalBytes;
        this.time = time;
    }

    public Download() {
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(String totalBytes) {
        this.totalBytes = totalBytes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
