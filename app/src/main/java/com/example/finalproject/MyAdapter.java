package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<DataClass> dataList;
    private Context context;
    private String currentUserId; // Add a field to store the current user ID

    public MyAdapter(ArrayList<DataClass> dataList, Context context, String currentUserId) {
        this.dataList = dataList;
        this.context = context;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass data = dataList.get(position);

        // Check if the image belongs to the current user
        if (data.getUserId().equals(currentUserId)) {
            Glide.with(context).load(data.getImageURL()).into(holder.recyclerImage);
            holder.recyclerCaption.setText(data.getCaption());
        } else {
            // Hide or handle images that do not belong to the current user
            // For example, you can hide the image view and caption or display a placeholder
            holder.recyclerImage.setVisibility(View.GONE);
            holder.recyclerCaption.setText("Not your image");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recyclerImage;
        TextView recyclerCaption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            recyclerCaption = itemView.findViewById(R.id.recyclerCaption);
        }
    }
}
