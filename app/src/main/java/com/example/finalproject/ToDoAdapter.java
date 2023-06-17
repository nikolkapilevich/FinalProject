package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;
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

    public void deleteTask (int position){
        ToDoModel toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.Id).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return checkListFragment.getContext();
    }

    public void editTask (int position){
        ToDoModel toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due", toDoModel.getDue());
        bundle.putString("id", toDoModel.Id);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(checkListFragment.getActivity().getSupportFragmentManager(), addNewTask.getTag());

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
                    firestore.collection("task").document(toDoModel.Id).update("status",1);
                }
                else {
                    firestore.collection("task").document(toDoModel.Id).update("status",0);
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

    public String getTaskId(int position) {
        // Retrieve the task ID from your dataset or list based on the position
        // Replace `yourDataset` with the actual dataset you are using to store the tasks
        ToDoModel task = todoList.get(position);
        return task.Id; // Replace `getId()` with the method to get the task ID
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
