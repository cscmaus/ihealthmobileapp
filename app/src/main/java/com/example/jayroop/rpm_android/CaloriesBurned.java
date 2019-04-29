package com.example.jayroop.rpm_android;

public class CaloriesBurned {

    private String startDate;
    private String endDate;


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCalorie_count() {
        return calorie_count;
    }

    public void setCalorie_count(String calorie_count) {
        this.calorie_count = calorie_count;
    }

    private String calorie_count;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "CaloriesBurned{" +
            "startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", calorie_count='" + calorie_count + '\'' +
            '}';
    }
}


