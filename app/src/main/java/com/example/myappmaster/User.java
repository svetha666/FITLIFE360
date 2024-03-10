package com.example.myappmaster;

public class User {
   String name;
    String email;
    String username;
    String password;

    String birthdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public User(String name, String email, String username, String password, String birthdate) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
    }

    public User() {
    }
}
