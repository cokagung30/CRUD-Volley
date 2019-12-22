package com.example.trainingcrudapi.model;

public class Student {

    private String nim, name, classes;

    public Student() {
    }

    public Student(String nim, String name, String classes) {
        this.nim = nim;
        this.name = name;
        this.classes = classes;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
