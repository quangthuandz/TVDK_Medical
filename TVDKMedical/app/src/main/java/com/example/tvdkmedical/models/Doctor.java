package com.example.tvdkmedical.models;

import java.io.Serializable;
import java.util.Date;

public class Doctor implements Serializable {
    private String doctorId;
    private String userId;
    private String bio;
    private String[] diseaseIds;
    private String name;

    public Doctor() {
    }

    public Doctor(String doctorId, String userId, String bio, String[] diseaseIds, String name) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.bio = bio;
        this.diseaseIds = diseaseIds;
        this.name = name;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String[] getDiseaseIds() {
        return diseaseIds;
    }

    public void setDiseaseIds(String[] diseaseIds) {
        this.diseaseIds = diseaseIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

