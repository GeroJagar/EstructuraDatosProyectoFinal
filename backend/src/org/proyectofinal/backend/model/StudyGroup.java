package org.proyectofinal.backend.model;

import org.proyectofinal.backend.dataStructures.DoubleEndedLinkedList.DoubleEndedLinkedList;

import java.io.Serializable;

public class StudyGroup implements Serializable {
    private String name;
    private SUBJECT subject;
    private DoubleEndedLinkedList<Student> students = new DoubleEndedLinkedList<>();

    public void addStudent(Student student) {
        students.add(student);
    }
}
