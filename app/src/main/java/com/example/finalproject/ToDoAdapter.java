package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> todoList;
    private CheckListFragment checkListFragment;
    private FirebaseFirestore firestore;

    public ToDoAdapter (CheckListFragment fragment, List<ToDoModel> todoList){
        this.todoList = todoList;
        checkListFragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(checkListFragment.getContext()).inflate(R.layout.each_task,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = todoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());
        holder.tvDueDate.setText("Due On " + toDoModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    firestore.collection("task").document(toDoModel.TaskId).update("status",1);
                }
                else {
                    firestore.collection("task").document(toDoModel.TaskId).update("status",0);
                }
            }
        });
    }

    private boolean toBoolean(int status) {
        return status !=0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDueDate;
        CheckBox mCheckBox;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
