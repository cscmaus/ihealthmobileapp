package com.example.jayroop.rpm_android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Day {

    private Date date;
    private String calories;
    private String steps;
    private String heartMinutes;
    private String heartRate;
    private ArrayList<Exercises> ex = new ArrayList<>();
    private String moveMinutes;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCalories() {
        if(this.calories ==null)
        {
            return "0";
        }
        else
        {
            return calories;
        }

    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getSteps() {
        if(this.steps ==null)
        {
            return "0";
        }
        else{
            return steps;
        }


    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getHeartMinutes() {
        if(heartMinutes == null)
        {
            return "0";
        }
        else
        {
            return heartMinutes;
        }

    }

    public void setHeartMinutes(String heartMinutes) {
        this.heartMinutes = heartMinutes;
    }

    public String getHeartRate() {
        if(heartRate == null)
        {
            return "0";
        }
        else
        {
            return heartRate;
        }


    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public ArrayList<Exercises> getEx() {
        return ex;
    }

    public void setEx(ArrayList<Exercises> ex) {
        this.ex = ex;
    }
    public int ex_size()
    {
        return ex.size();
    }
    public void addEx(Exercises newEx) {

        this.ex.add(newEx);

    }

    public String getMoveMinutes() {

        if(moveMinutes == null)
        {
            return "0";
        }
        else
        {
            return moveMinutes;
        }


    }

    public void setMoveMinutes(String moveMinutes) {
        this.moveMinutes = moveMinutes;
    }


    @Override
    public String toString() {
        return "Day{" +
            "date=" + date +
            ", calories='" + calories + '\'' +
            ", steps='" + steps + '\'' +
            ", heartMinutes='" + heartMinutes + '\'' +
            ", heartRate='" + heartRate + '\'' +
            ", ex=" + ex +
            ", moveMinutes='" + moveMinutes + '\'' +
            '}';
    }
}
