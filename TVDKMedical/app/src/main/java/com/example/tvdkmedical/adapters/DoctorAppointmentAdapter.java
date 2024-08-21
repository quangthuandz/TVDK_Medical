package com.example.tvdkmedical.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Appointment;
import com.example.tvdkmedical.models.Doctor;
import com.example.tvdkmedical.models.User;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder>{
    private Context context;
    private List<Appointment> appointments;
    private List<User> users;

    public DoctorAppointmentAdapter(Context context, List<Appointment> appointments, List<User> users) {
        this.context = context;
        this.appointments = appointments;
        this.users = users;
    }
    @NonNull
    @Override
    public DoctorAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointment_all, parent, false);
        return new DoctorAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.ViewHolder holder, int position) {
        Appointment p = appointments.get(position);
        User d = findUserById(p.getUserId());
        holder.setData(p,d);
    }

    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
    public void updateData(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtStartTime;
        private TextView txtEndTime;
        private TextView txtDoctorName;
        private TextView txtDoctorInfo;
        private TextView txtDateBooking;

        private void bindingView() {
            txtStartTime = itemView.findViewById(R.id.appointmentStartTime);
            txtEndTime = itemView.findViewById(R.id.appointmentEndTime);
            txtDoctorName = itemView.findViewById(R.id.doctorNameAppointment);
            txtDoctorInfo = itemView.findViewById(R.id.doctorInforAppointment);
            txtDateBooking = itemView.findViewById(R.id.dateBooking);
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();

        }

        public void setData(Appointment p) {
        }
        public void setData(Appointment appointment, User user) {
            txtStartTime.setText(formatTimestampToTime(appointment.getStartTime()));
            txtEndTime.setText(formatTimestampToTime(appointment.getEndTime()));
            txtDateBooking.setText(formatTimestampToDate(appointment.getStartTime()));

            if (user != null) {
                txtDoctorName.setText(user.getName());
                txtDoctorInfo.setText(user.getPhone());
            } else {
                txtDoctorName.setText("Unknown Doctor");
                txtDoctorInfo.setText("");
            }
        }

        private String formatTimestampToTime(Timestamp timestamp) {
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(date);
        }


        private String formatTimestampToDate(Timestamp timestamp) {
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM yyyy", Locale.getDefault());
            return sdf.format(date);
        }

    }
}
