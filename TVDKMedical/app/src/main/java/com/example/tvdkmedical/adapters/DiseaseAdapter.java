package com.example.tvdkmedical.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.fragments.SearchFragment;
import com.example.tvdkmedical.models.Day;
import com.example.tvdkmedical.models.Disease;
import com.example.tvdkmedical.models.Post;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.VH>{
    private List<Disease> data;
    private Context context;
    private int selectedItem = RecyclerView.NO_POSITION;
    private DiseaseAdapter.OnDiseaseClickListener onDiseaseClickListener;
    public DiseaseAdapter(List<Disease> data, Context context,OnDiseaseClickListener onDiseaseClickListener){
        this.data=data;
        this.context=context;
        this.onDiseaseClickListener = onDiseaseClickListener;
    }
    int count = 0;
    @NonNull
    @Override
    public DiseaseAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_disease,parent,false);
        return new DiseaseAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseAdapter.VH holder, int position) {
        Disease p = data.get(position);
        holder.bind(p, position);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private Button buttonDisease;

        public VH(@NonNull View itemView) {
            super(itemView);
            buttonDisease = itemView.findViewById(R.id.btnDisease);
            buttonDisease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Set selected item
                        selectedItem = position;
                        notifyDataSetChanged(); // Refresh adapter to update colors
                        Disease disease = data.get(position);
                        onDiseaseClickListener.onDiseaseClicked(disease);
                    }
                }
            });

        }




        public void bind(Disease p, int position) {
            buttonDisease.setText(p.getName());

            if (position == selectedItem) {
                buttonDisease.setBackgroundColor(context.getResources().getColor(R.color.black));
            } else {
                buttonDisease.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }
        }
    }
    public interface OnDiseaseClickListener {
        void onDiseaseClicked(Disease disease);
    }
}
