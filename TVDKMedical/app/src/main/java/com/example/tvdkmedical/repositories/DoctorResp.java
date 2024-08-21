package com.example.tvdkmedical.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorResp {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    //get list appointment
    public void getDoctors(Callback<Doctor> callback) {
        Query query = databaseReference.child("doctors").orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Doctor> doctors = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Doctor doctor = new Doctor();
                    String doctorId = childSnapshot.getKey();
                    String bio = childSnapshot.child("bio").getValue(String.class);
                    Iterable<DataSnapshot> diseaseIdSnapshots = childSnapshot.child("diseaseId").getChildren();
                    List<String> diseaseIdsList = new ArrayList<>();
                    for (DataSnapshot diseaseIdSnapshot : diseaseIdSnapshots) {
                        String diseaseId = diseaseIdSnapshot.getValue(String.class);
                        diseaseIdsList.add(diseaseId);
                    }
                    String[] diseaseIds = diseaseIdsList.toArray(new String[0]);
                    String email = childSnapshot.child("email").getValue(String.class);
                    String name = childSnapshot.child("name").getValue(String.class);
                    String office = childSnapshot.child("office").getValue(String.class);
                    Long phoneNumber = childSnapshot.child("phoneNumber").getValue(Long.class);
                    String imageUrl = childSnapshot.child("imageurl").getValue(String.class);
                    doctor.setDoctorId(doctorId);
                    doctor.setBio(bio);
                    doctor.setDiseaseIds(diseaseIds);
                    //doctor.setEmail(email);
                    doctor.setName(name);
                    //doctor.setOffice(office);
                    //doctor.setPhoneNumber(phoneNumber);
                    doctors.add(doctor);
                }

                Log.d("DoctorResp", "Number of doctors fetched: " + doctors.size());
                callback.onCallback(doctors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DoctorResp", "Error: " + error.getMessage());
            }
        });
    }

    // Get doctor by id
    public void getDoctorById(String doctorId, Callback<Doctor> callback) {
        databaseReference.child("doctors").child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = new Doctor();
                String bio = snapshot.child("bio").getValue(String.class);
                Iterable<DataSnapshot> diseaseIdSnapshots = snapshot.child("diseaseId").getChildren();
                List<String> diseaseIdsList = new ArrayList<>();
                for (DataSnapshot diseaseIdSnapshot : diseaseIdSnapshots) {
                    String diseaseId = diseaseIdSnapshot.getValue(String.class);
                    diseaseIdsList.add(diseaseId);
                }
                String[] diseaseIds = diseaseIdsList.toArray(new String[0]);
                String email = snapshot.child("email").getValue(String.class);
                String name = snapshot.child("name").getValue(String.class);
                String office = snapshot.child("office").getValue(String.class);
                Long phoneNumber = snapshot.child("phoneNumber").getValue(Long.class);
                String imageUrl = snapshot.child("imageurl").getValue(String.class);
                doctor.setDoctorId(doctorId);
                doctor.setBio(bio);
                doctor.setDiseaseIds(diseaseIds);
                doctor.setName(name);

                List<Doctor> doctors = new ArrayList<>();
                doctors.add(doctor);
                callback.onCallback(doctors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DoctorResp", "Error: " + error.getMessage());
            }
        });
    }

    public void getDoctorsByDiseaseIdAndName(String searchName, String diseaseId, Callback<Doctor> callback) {
        Query query = databaseReference.child("doctors").orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Doctor> doctors = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String doctorName = childSnapshot.child("name").getValue(String.class);
                    if (doctorName != null && doctorName.contains(searchName)) {
                        Doctor doctor = new Doctor();
                        String doctorId = childSnapshot.getKey();
                        String bio = childSnapshot.child("bio").getValue(String.class);
                        List<String> diseaseIdsList = new ArrayList<>();
                        if (childSnapshot.hasChild("diseaseId")) {
                            for (DataSnapshot diseaseIdSnapshot : childSnapshot.child("diseaseId").getChildren()) {
                                String dId = diseaseIdSnapshot.getValue(String.class);
                                if (dId != null) {
                                    diseaseIdsList.add(dId);
                                }
                            }
                        }
                        String[] diseaseIds = diseaseIdsList.toArray(new String[0]);

                        if (Arrays.asList(diseaseIds).contains(diseaseId)) {

                            doctor.setDoctorId(doctorId);
                            doctor.setBio(bio);
                            doctor.setDiseaseIds(diseaseIds);
                            //doctor.setEmail(email);
                            doctor.setName(doctorName);
                            //doctor.setImageUrl(imageUrl);
                            doctors.add(doctor);
                        }
                    }
                }

                Log.d("DoctorResp", "Number of doctors fetched: " + doctors.size());
                callback.onCallback(doctors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DoctorResp", "Error: " + error.getMessage());
            }
        });
    }

    public void getDoctorByUserId(String userId, Callback<Doctor> callback) {
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference().child("doctors");

        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Doctor> data = new ArrayList<>();
                Doctor doctor = null;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String doctorId = childSnapshot.getKey(); // Assuming doctorId is the key of each doctor node
                    String currentUserId = childSnapshot.child("userId").getValue(String.class);
                    if (userId.equals(currentUserId)) {
                        String bio = childSnapshot.child("bio").getValue(String.class);
                        Iterable<DataSnapshot> diseaseIdSnapshots = childSnapshot.child("diseaseId").getChildren();
                        List<String> diseaseIdsList = new ArrayList<>();
                        for (DataSnapshot diseaseIdSnapshot : diseaseIdSnapshots) {
                            String diseaseId = diseaseIdSnapshot.getValue(String.class);
                            diseaseIdsList.add(diseaseId);
                        }
                        String[] diseaseIds = diseaseIdsList.toArray(new String[0]);
                        String email = childSnapshot.child("email").getValue(String.class);
                        String name = childSnapshot.child("name").getValue(String.class);
                        String office = childSnapshot.child("office").getValue(String.class);
                        Long phoneNumber = childSnapshot.child("phoneNumber").getValue(Long.class);
                        String imageUrl = childSnapshot.child("imageurl").getValue(String.class);

                        doctor = new Doctor();
                        doctor.setDoctorId(doctorId);
                        doctor.setUserId(userId); // Assuming you want to set userId in Doctor object
                        doctor.setBio(bio);
                        doctor.setDiseaseIds(diseaseIds);
                        doctor.setName(name);
                        data.add(doctor);
                        break; // Exit loop after finding the matching doctor
                    }
                }
                callback.onCallback(data); // Callback with found doctor or null if not found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DoctorResp", "Error: " + error.getMessage());
                callback.onCallback(null); // Handle cancellation by returning null
            }
        });
    }
}