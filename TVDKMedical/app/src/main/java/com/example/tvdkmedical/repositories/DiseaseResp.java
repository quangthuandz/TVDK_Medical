package com.example.tvdkmedical.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiseaseResp {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void getDiseaseById(String diseaseId, Callback<Disease> diseaseCallback) {
        Query query = databaseReference.child("diseases");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Disease> diseases = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Disease disease = new Disease();
                    String diseasedId = childSnapshot.getKey();
                    if(diseasedId.equals(diseaseId)) {
                        String name = childSnapshot.child("name").getValue(String.class);
                        String description = childSnapshot.child("description").getValue(String.class);
                        disease.setDiseaseId(diseaseId);
                        disease.setName(name);
                        disease.setDescription(description);
                        diseases.add(disease);
                    }
                }

                Log.d("DoctorResp", "Number of doctors fetched: " + diseases.size());
                diseaseCallback.onCallback(diseases);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DiseaseResp", "Error: " + error.getMessage());
            }
        });
    }

    public void  getDisease(Callback<Disease> callback){
        Query query = databaseReference.child("diseases");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Disease> diseases = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Disease disease = new Disease();
                    String diseaseId = childSnapshot.getKey();
                    String name = childSnapshot.child("name").getValue(String.class);
                    String description = childSnapshot.child("description").getValue(String.class);

                    disease.setDiseaseId(diseaseId);
                    disease.setName(name);
                    disease.setDescription(description);
                    diseases.add(disease);
                }

                Log.d("DoctorResp", "Number of doctors fetched: " + diseases.size());
                callback.onCallback(diseases);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DiseaseResp", "Error: " + error.getMessage());
            }
        });
    }
}