package com.example.tvdkmedical.views.appointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Record;
import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<Record> recordList;

    public RecordAdapter(List<Record> recordList) throws JSONException {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record, parent, false);
        return new RecordAdapter.RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.tvDiagnosis.setText(record.getDiagnosis());
        holder.tvTreatment.setText(record.getTreatment());

        Date date = record.getDate().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        String formattedDate = sdf.format(date);
        holder.tvDate.setText(formattedDate);
        holder.tvExtraData.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDiagnosis;
        private TextView tvTreatment;
        private TextView tvDate;
        private TextView tvExtraData;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiagnosis = itemView.findViewById(R.id.tvDiagnosis);
            tvTreatment = itemView.findViewById(R.id.tvTreatment);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvExtraData = itemView.findViewById(R.id.tvExtraData);
        }
    }
}
