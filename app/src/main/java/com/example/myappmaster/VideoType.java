package com.example.myappmaster;

public enum VideoType {
    WORKOUT("workout"),
    DIET("diet");

    private final String name;
    private VideoType(String s) {
        this.name = s;
    }

    public String toString() {
        return this.name;
    }
}
