package com.example.tvdkmedical.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.LoginActivity;
import com.example.tvdkmedical.R;
import com.example.tvdkmedical.adapters.DayAdapter;
import com.example.tvdkmedical.adapters.DiseaseAdapter;
import com.example.tvdkmedical.adapters.DoctorAdapter;
import com.example.tvdkmedical.adapters.DoctorAppointmentAdapter;
import com.example.tvdkmedical.adapters.StatusAdapter;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Day;
import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.models.User;
import com.example.tvdkmedical.repositories.AppointmentResp;
import com.example.tvdkmedical.repositories.DoctorResp;
import com.example.tvdkmedical.repositories.UserResp;
import com.example.tvdkmedical.repositories.callbacks.Callback;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentListDoctorFragment extends Fragment implements StatusAdapter.OnStatusClickListener{
    private Spinner monthSpinner;
    private RecyclerView rcDay;
    private RecyclerView rcStatusList;
    private RecyclerView rcAppointment;
    private List<Day> dayList;
    private List<Appointment> appointmentList;
    private List<String> statusList;
    private List<User> userList;
    private DayAdapter dayAdapter;
    private StatusAdapter statusAdapter;
    private DoctorAppointmentAdapter doctorAppointmentAdapter;
    private AppointmentResp appointmentResp;
    private UserResp userResp;
    private DoctorResp doctorResp;
    private String selectedDate = null;
    private String selectedStatus = null;
    FirebaseAuth mAuth;
    FirebaseUser userDetails;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment_list_doctor, container, false);
        mAuth = FirebaseAuth.getInstance();
        userDetails = mAuth.getCurrentUser();
        monthSpinner = rootView.findViewById(R.id.spinnerMonth);
        rcDay = rootView.findViewById(R.id.rcDay);
        rcStatusList = rootView.findViewById(R.id.rcStatus);
        rcAppointment = rootView.findViewById(R.id.rcAppointment);
        dayList = new ArrayList<>();
        appointmentList = new ArrayList<>();
        userList = new ArrayList<>();
        statusList = new ArrayList<>();
        appointmentResp = new AppointmentResp();
        doctorResp = new DoctorResp();
        userResp = new UserResp();
        if (userDetails == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else {
            initSpinner();
            initRecyclerView();
            initStatusButton();
            initAppointmentRecyclerView();
        }
        return rootView;
    }

    private void initAppointmentRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcAppointment.setLayoutManager(layoutManager);

        DoctorAppointmentAdapter appointmentAdapter = new DoctorAppointmentAdapter(getActivity(), appointmentList, userList);
        rcAppointment.setAdapter(appointmentAdapter);

        loadUserList(appointmentAdapter);
        loadAppointmentsForDoctor(userDetails.getUid(),appointmentAdapter);

    }

    private void loadUserList(DoctorAppointmentAdapter appointmentAdapter) {
        userResp.getUserList(new Callback<User>() {
            @Override
            public void onCallback(List<User> objects) {
                userList.clear();
                userList.addAll(objects);
                appointmentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadAppointmentsForDoctor(String userId,DoctorAppointmentAdapter appointmentAdapter) {

        doctorResp.getDoctorByUserId(userId, new Callback<Doctor>() {
            @Override
            public void onCallback(List<Doctor> doctors) {
                Log.d("abcdef",doctors.get(0).getDoctorId());

                if (doctors != null) {
                    long currentTimestamp = Calendar.getInstance().getTimeInMillis() / 1000;
                    Log.d("abcdefg", String.valueOf(currentTimestamp));

                    appointmentResp.getAppointmentsByDoctor(doctors.get(0).getDoctorId(), new Callback<Appointment>() {
                        @Override
                        public void onCallback(List<Appointment> appointments) {
                            appointmentList.clear();
                            appointmentList.addAll(appointments);
                            Log.d("ab", String.valueOf(appointments.size()));
                            appointmentAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }


        });
    }


    private void initStatusButton() {
        // Set the FlexboxLayoutManager for the RecyclerView
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        rcStatusList.setLayoutManager(layoutManager);
        statusList.add("unconfirmed");
        statusList.add("confirmed");
        statusList.add("canceled");
        statusList.add("in progress");
        statusList.add("finished");
        statusAdapter = new StatusAdapter(statusList, getActivity(),this);
        rcStatusList.setAdapter(statusAdapter);





    }
    private void initRecyclerView() {
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;  // 0-based index
        dayAdapter = new DayAdapter(getContext(), dayList, this::onDayClick, currentDay);
        rcDay.setAdapter(dayAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rcDay.setLayoutManager(layoutManager);
    }

    private void onDayClick(int position, Day day) {
        int selectedMonthIndex = monthSpinner.getSelectedItemPosition();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR); // Get the current year

        // Convert month index to month number (1-12)
        int month = selectedMonthIndex + 1;
        String formattedMonth = String.format(Locale.getDefault(), "%02d", month);

        // Assuming day.getDay() returns a day number as String
        String formattedDay = String.format(Locale.getDefault(), "%02d", Integer.parseInt(day.getDay()));

        // Combine year, month, and day to form a complete date string
        String newSelectedDate = year + "-" + formattedMonth + "-" + formattedDay;

        // Check if the new selected date is the same as the previously selected date
        if (newSelectedDate.equals(selectedDate)) {
            // If the same date is selected again, reset the selected date and show all appointments
            selectedDate = null;
        } else {
            // Otherwise, update the selected date
            selectedDate = newSelectedDate;
        }
        filterAndUpdateAppointments();
    }

    private void initSpinner() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        monthSpinner.setSelection(currentMonth);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDaysOfMonth(position);
                if (dayAdapter != null) {
                    dayAdapter.clearSelection(); // Clear the selection when a new month is selected
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void updateDaysOfMonth(int selectedMonth) {
        dayList.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < numDaysInMonth; i++) {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            String shortDayOfWeek = dayOfWeek.substring(0, 2);
            dayList.add(new Day(String.valueOf(day), shortDayOfWeek));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (dayAdapter != null) {
            dayAdapter.notifyDataSetChanged();
        }
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;  // 0-based index
        if (selectedMonth == currentMonth) {
            rcDay.scrollToPosition(currentDay);

        }
    }

    @Override
    public void onStatusClicked(String status) {
        // Check if the new selected status is the same as the previously selected status
        if (status.equals(selectedStatus)) {
            // If the same status is selected again, reset the selected status and show all appointments
            selectedStatus = null;
        } else {
            // Otherwise, update the selected status
            selectedStatus = status;
        }
        filterAndUpdateAppointments();
    }

    private void filterAndUpdateAppointments() {

        if (doctorAppointmentAdapter == null) {
            doctorAppointmentAdapter = new DoctorAppointmentAdapter(getActivity(), appointmentList, userList);
            rcAppointment.setAdapter(doctorAppointmentAdapter);
        }
        List<Appointment> filteredList = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            if (matchesFilter(appointment)) {
                filteredList.add(appointment);
            }
        }
        doctorAppointmentAdapter.updateData(filteredList);
    }

    private boolean matchesFilter(Appointment appointment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            String appointmentDate = dateFormat.format(appointment.getStartTime().toDate()); // Ensure conversion from Timestamp to Date before formatting
        // If both selectedDate and selectedStatus are null, show all appointments
        if (selectedDate == null && selectedStatus == null) {
            return true;
        }

        // If only selectedDate is non-null, filter by date
        if (selectedDate != null && selectedStatus == null) {
            return appointmentDate.equals(selectedDate);
        }

        // If only selectedStatus is non-null, filter by status
        if (selectedDate == null && selectedStatus != null) {
            return appointment.getStatus().equals(selectedStatus);
        }

        // If both selectedDate and selectedStatus are non-null, filter by both
        boolean dateMatches = appointmentDate.equals(selectedDate);
        boolean statusMatches = appointment.getStatus().equals(selectedStatus);

        return dateMatches && statusMatches;
    }
    private String formatAppointmentDate(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
