package com.example.tvdkmedical.models;

import org.json.JSONObject;

import java.sql.Timestamp;

public class User {
    private String userId;
    private String name;
    private String email;
    private Timestamp dob;
    private String address;
    private String phone;
    private JSONObject idCard;
    private JSONObject healthCard;
    private String role;

    public User() {
    }

    public User(String userId, String name, String email, Timestamp dob, String address, String phone, JSONObject idCard, JSONObject healthCard, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.idCard = idCard;
        this.healthCard = healthCard;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public JSONObject getIdCard() {
        return idCard;
    }

    public void setIdCard(JSONObject idCard) {
        this.idCard = idCard;
    }

    public JSONObject getHealthCard() {
        return healthCard;
    }

    public void setHealthCard(JSONObject healthCard) {
        this.healthCard = healthCard;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDoctor() {
        return role.equals("doctor");
    }
}
