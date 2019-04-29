package com.example.jayroop.rpm_android;

import java.sql.Date;
import java.text.DateFormat;

public class HeartStatus {

    private String startDate;
    private String endDate;

    private String heartMinutes;


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHeartMinutes() {
        return heartMinutes;
    }

    public void setHeartMinutes(String heartMinutes) {
        this.heartMinutes = heartMinutes;
    }

    @Override
    public String toString() {
        return "HeartStatus{" +
            "startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", heartMinutes='" + heartMinutes + '\'' +
            '}';
    }
}
