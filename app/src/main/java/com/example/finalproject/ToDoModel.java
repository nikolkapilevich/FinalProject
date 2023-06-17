package com.example.finalproject;

public class ToDoModel extends com.example.finalproject.TaskId
{
    private String task , due;
    private TaskId taskId;
    private int status;

    public String getTask() {
        return task;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }

    public void setId(TaskId taskId) {
        this.taskId = taskId;
    }

    public String getId() {
        return taskId.Id;
    }
}
