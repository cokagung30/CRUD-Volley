package com.example.trainingcrudapi.interfaces;

import com.example.trainingcrudapi.model.Student;

import java.util.ArrayList;

public interface StudentClickListener {

    void onClick(Integer position, ArrayList<Student> students);

    void longClick(Integer position, ArrayList<Student> students);

}
