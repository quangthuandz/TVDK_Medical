package com.example.tvdkmedical.models;

public class Disease {
    private String diseaseId;
    private String name;
    private String description;

    public Disease() {
    }

    public Disease(String diseaseId, String description, String name) {
        this.diseaseId = diseaseId;
        this.description = description;
        this.name = name;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}