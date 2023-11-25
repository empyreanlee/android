package com.example.administrator;

public class Student {
    private String name;
    private String regNo;

    public Student() {
        // Default constructor required for Firestore
    }

    public Student(String name, String regNo) {
        this.name = name;
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}
