package com.example.tvdkmedical.views.appointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Disease;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    private List<Disease> diseaseList;

    public DiseaseAdapter(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease, parent, false);
        return new DiseaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease disease = diseaseList.get(position);
        holder.tvDiseaseName.setText(disease.getName());
        holder.tvDiseaseDescription.setText(disease.getDescription());
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    public static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDiseaseName;
        private TextView tvDiseaseDescription;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiseaseName = itemView.findViewById(R.id.tvDiseaseName);
            tvDiseaseDescription = itemView.findViewById(R.id.tvDiseaseDescription);
        }
    }
}