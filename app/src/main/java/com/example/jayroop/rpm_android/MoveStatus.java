package com.example.jayroop.rpm_android;

public class MoveStatus {

    private String startDate;
    private String endDate;

    private String moveMinutes;

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

    public String getMoveMinutes() {
        return moveMinutes;
    }

    public void setMoveMinutes(String moveMinutes) {
        this.moveMinutes = moveMinutes;
    }

    @Override
    public String

    toString() {
        return "MoveStatus{" +
            "startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", moveMinutes='" + moveMinutes + '\'' +
            '}';
    }
}

