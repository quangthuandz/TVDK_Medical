package com.example.tvdkmedical.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.AddAppointment;
import com.example.tvdkmedical.LoginActivity;
import com.example.tvdkmedical.R;
import com.example.tvdkmedical.adapters.DiseaseAdapter;
import com.example.tvdkmedical.adapters.DoctorAdapter;
import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.repositories.DiseaseResp;
import com.example.tvdkmedical.repositories.DoctorResp;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements DiseaseAdapter.OnDiseaseClickListener,DoctorAdapter.OnDoctorClickListener  {
    RecyclerView recyclerView;
    RecyclerView doctorRecyclerView;
    EditText searchEditText;

    Button buttonDisease;
    FirebaseAuth mAuth;
    FirebaseUser userDetails;
    DiseaseAdapter diseaseAdapter;
    DoctorAdapter doctorAdapter;
    DiseaseResp diseaseResp;
    DoctorResp doctorResp;

    List<Disease> data;
    List<Doctor> doctordata;

    private String currentDiseaseId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mAuth = FirebaseAuth.getInstance();
        userDetails = mAuth.getCurrentUser();
        recyclerView = rootView.findViewById(R.id.rcDiseased);
        doctorRecyclerView = rootView.findViewById(R.id.rcDoctors);
        searchEditText = rootView.findViewById(R.id.search_input);
        data = new ArrayList<>();
        doctordata= new ArrayList<>();
        diseaseResp = new DiseaseResp();
        doctorResp = new DoctorResp();

        if (userDetails == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            initDoctorRecyclerView();

            initDiseaseButton();
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchQuery = s.toString();
                    if (currentDiseaseId != null) {
                        loadDoctorsByDiseaseIdAndName(currentDiseaseId, searchQuery);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return rootView;
    }

    private void initDoctorRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        doctorRecyclerView.setLayoutManager(layoutManager);
        doctorAdapter = new DoctorAdapter(doctordata,getActivity(), (DoctorAdapter.OnDoctorClickListener) this);
        doctorRecyclerView.setAdapter(doctorAdapter);
        loadDoctorsFromFirebase(new Callback<Doctor>() {
            @Override
            public void onCallback(List<Doctor> objects) {
                doctordata.clear();
                doctordata.addAll(objects);
                doctorAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initDiseaseButton() {
        // Set the FlexboxLayoutManager for the RecyclerView
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        recyclerView.setLayoutManager(layoutManager);

        // Initialize the adapter with the data
        diseaseAdapter = new DiseaseAdapter(data, getActivity(),this);
        recyclerView.setAdapter(diseaseAdapter);

        loadDiseasesFromFirebase(new Callback<Disease>() {
            @Override
            public void onCallback(List<Disease> list) {
                data.clear();
                data.addAll(list);
                diseaseAdapter.notifyDataSetChanged();
            }
        });



    }

    private void loadDoctorsFromFirebase(Callback<Doctor> callback) {
        doctorResp.getDoctors(callback);

    }

    private void loadDiseasesFromFirebase(Callback<Disease> callback) {
        diseaseResp.getDisease(callback);

    }

    @Override
    public void onDiseaseClicked(Disease disease) {
        currentDiseaseId = disease.getDiseaseId();
        String searchQuery = searchEditText.getText().toString();
        loadDoctorsByDiseaseIdAndName(currentDiseaseId, searchQuery);
    }

    private void loadDoctorsByDiseaseIdAndName(String diseaseId, String searchQuery) {
        doctorResp.getDoctorsByDiseaseIdAndName(searchQuery, diseaseId, new Callback<Doctor>() {
            @Override
            public void onCallback(List<Doctor> objects) {
                doctordata.clear();
                doctordata.addAll(objects);
                doctorAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onDoctorClick(Doctor doctor) {
        Intent intent = new Intent(getActivity(), AddAppointment.class);
        intent.putExtra("selectedDoctor",doctor); // Ensure Doctor class implements Serializable or Parcelable
        Log.d("DOCTOR",doctor.getDoctorId()+doctor.getName());
        startActivity(intent);
    }
}