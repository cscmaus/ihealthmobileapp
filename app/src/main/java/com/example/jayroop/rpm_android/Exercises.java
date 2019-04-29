package com.example.jayroop.rpm_android;

public class Exercises {

    private String startDate;
    private String endDate;
    private String value;
    private String duration;
    private String activity_type;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {

        this.value = activity_type;
        if(activity_type.equals("9"))
        {
            this.activity_type = "Aerobics";
        }
        else if(activity_type.equals("2"))
        {
            this.activity_type = "On Foot";
        }
        else if(activity_type.equals("8"))
        {
            this.activity_type = "Running";
        }
        else if(activity_type.equals("56"))
        {
            this.activity_type = "Jogging";
        }
        else if(activity_type.equals("7"))
        {
            this.activity_type = "Walking";
        }
        else if(activity_type.equals("3"))
        {
            this.activity_type = "Idle";
        }
        else
        {
            this.activity_type = "Light Stroll";
        }

    }

    @Override
    public String toString() {
        return "Exercises{" +
            "startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", value='" + value + '\'' +
            ", duration='" + duration + '\'' +
            ", activity_type='" + activity_type + '\'' +
            '}';
    }
}
