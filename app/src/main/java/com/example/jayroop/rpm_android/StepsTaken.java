package com.example.jayroop.rpm_android;

public class StepsTaken {

    private String startDate;
    private String endDate;

    private String step_count;

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

    public String getSteps() {
        return step_count;
    }

    public void setSteps(String steps) {
        this.step_count = steps;
    }

    @Override
    public String toString() {
        return "StepsTaken{" +
            "startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", steps='" + step_count + '\'' +
            '}';
    }
}
