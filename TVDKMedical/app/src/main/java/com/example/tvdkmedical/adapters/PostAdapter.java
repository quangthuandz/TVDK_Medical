package com.example.tvdkmedical.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;
import com.example.tvdkmedical.models.Post;
import android.content.Context;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.VH> {
    private List<Post> data;
    private Context context;

    public PostAdapter(List<Post> data, Context context){
        this.data=data;
        this.context=context;
    }
    int count = 0;
    @NonNull
    @Override
    public PostAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post_card,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.VH holder, int position) {
        Post p = data.get(position);
        holder.setData(p);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvInfo;
        private void bindingView(){
            tvTitle =itemView.findViewById(R.id.tvTitle);
            tvInfo=itemView.findViewById(R.id.tvInfo);
        }
        private void bindingAction(){

        }

        private void onItemViewClick(View view) {
        }

        private void onTitleClick(View view) {
        }

        public VH(@NonNull View itemView) {
            super(itemView);
            bindingView();
            bindingAction();
        }

        public void setData(Post p) {
            tvTitle.setText(p.getTitle());
            tvInfo.setText(p.getContent());
        }
    }
}
