package com.example.jayroop.rpm_android;

public class UserBasic {


    private String email;
    private String height;
    private String weight;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "UserBasic{" +
            "email='" + email + '\'' +
            ", height='" + height + '\'' +
            ", weight='" + weight + '\'' +
            '}';
    }
}
