package com.example.tvdkmedical.repositories;

import androidx.annotation.NonNull;

import com.example.tvdkmedical.models.User;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class UserResp {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    // Get user by userId
    public void getUser(String userId, Callback<User> callback) {
        Query query = databaseReference.child("users").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                user.setUserId(userId);
                user.setName(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                user.setEmail(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                user.setPhone(Objects.requireNonNull(snapshot.child("phone").getValue()).toString());
                user.setAddress(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                user.setRole(Objects.requireNonNull(snapshot.child("role").getValue()).toString());
//                user.getDob().setTime(Objects.requireNonNull(snapshot.child("dob").getValue(Integer.class)));

//                String healthCard = Objects.requireNonNull(snapshot.child("healthCard").getValue()).toString();
//                if (!healthCard.equals("null")) {
//                    try {
//                        user.setHealthCard(new JSONObject(healthCard));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                String idCard = Objects.requireNonNull(snapshot.child("idCard").getValue()).toString();
//                if (!idCard.equals("null")) {
//                    try {
//                        user.setIdCard(new JSONObject(idCard));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
                List<User> users = new ArrayList<>();
                users.add(user);
                callback.onCallback(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    public void getUserList(Callback<User> callback) {
        Query query = databaseReference.child("users");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    User user = new User();
                    user.setUserId(childSnapshot.getKey() != null ? childSnapshot.getKey() : "");
                    user.setName(childSnapshot.hasChild("name") && childSnapshot.child("name").getValue() != null ? childSnapshot.child("name").getValue().toString() : "");
                    user.setEmail(childSnapshot.hasChild("email") && childSnapshot.child("email").getValue() != null ? childSnapshot.child("email").getValue().toString() : "");
                    user.setPhone(childSnapshot.hasChild("phone") && childSnapshot.child("phone").getValue() != null ? childSnapshot.child("phone").getValue().toString() : "");
                    user.setAddress(childSnapshot.hasChild("address") && childSnapshot.child("address").getValue() != null ? childSnapshot.child("address").getValue().toString() : "");
                    user.setRole(childSnapshot.hasChild("role") && childSnapshot.child("role").getValue() != null ? childSnapshot.child("role").getValue().toString() : "");

                    String healthCard = childSnapshot.hasChild("healthCard") && childSnapshot.child("healthCard").getValue() != null ? childSnapshot.child("healthCard").getValue().toString() : "null";
                    if (!healthCard.equals("null")) {
                        try {
                            user.setHealthCard(new JSONObject(healthCard));
                        } catch (JSONException e) {
                            user.setHealthCard(new JSONObject()); // Set to empty JSON object if parsing fails or "null"
                        }
                    } else {
                        user.setHealthCard(new JSONObject());
                    }

                    String idCard = childSnapshot.hasChild("idCard") && childSnapshot.child("idCard").getValue() != null ? childSnapshot.child("idCard").getValue().toString() : "null";
                    if (!idCard.equals("null")) {
                        try {
                            user.setIdCard(new JSONObject(idCard));
                        } catch (JSONException e) {
                            user.setIdCard(new JSONObject()); // Set to empty JSON object if parsing fails or "null"
                        }
                    } else {
                        user.setIdCard(new JSONObject());
                    }

                    users.add(user);
                }
                callback.onCallback(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

}
