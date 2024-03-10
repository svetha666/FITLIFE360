package com.example.myappmaster;

public class UserProfile {
    private String weight;
    private String height;
    private String name;
    private String age;

    // Empty constructor required for Firebase
    public UserProfile() {
    }

    public UserProfile(String weight, String height, String name, String age) {
        this.weight = weight;
        this.height = height;
        this.name = name;
        this.age = age;
    }

    // Getters and setters
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
