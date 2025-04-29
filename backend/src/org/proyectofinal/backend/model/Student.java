package org.proyectofinal.backend.model;

import org.proyectofinal.backend.dataStructures.DoubleEndedLinkedList.DoubleEndedLinkedList;

public class Student extends User{

    private DoubleEndedLinkedList<StudyGroup> studyGroups = new DoubleEndedLinkedList<>();
    private DoubleEndedLinkedList<Student> friends = new DoubleEndedLinkedList<>();

    public void addStudyGroup(StudyGroup studyGroup) {
        studyGroups.add(studyGroup);
    }

    public void addFriend(Student friend) {
        friends.add(friend);
    }
}
