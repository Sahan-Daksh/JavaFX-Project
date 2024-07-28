package com.insitute.iimanage.model.Tm;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;

public class IntakeTm {
    private String id;
    private String name;
    private String dob;
    private String course;
    private boolean status;
    private Button button;

    public IntakeTm() {
    }

    public IntakeTm(String id, String name, String dob, String course, boolean status, Button button) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.course = course;
        this.status = status;
        this.button = button;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
