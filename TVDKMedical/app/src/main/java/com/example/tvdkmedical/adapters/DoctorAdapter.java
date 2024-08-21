package com.example.tvdkmedical.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.models.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.VH>{
    private List<Doctor> data;
    private Context context;
    private OnDoctorClickListener listener;


    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }
    public DoctorAdapter(List<Doctor> data,Context context, OnDoctorClickListener listener){
        this.data =data;
        this.context=context;
        this.listener = listener;

    }
    @NonNull
    @Override
    public DoctorAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post_card,parent,false);
        return new DoctorAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.VH holder, int position) {
        Doctor p = data.get(position);
        holder.setData(p);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDoctorClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvInfo;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvTitle =itemView.findViewById(R.id.tvTitle);
            tvInfo=itemView.findViewById(R.id.tvInfo);
        }

        public void setData(Doctor p) {
            tvTitle.setText(p.getName());
            tvInfo.setText(p.getBio());
        }
    }
}
