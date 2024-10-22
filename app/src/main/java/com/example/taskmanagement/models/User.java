package com.example.taskmanagement.models;

import java.util.ArrayList;

public class User {
    private String name;

    private String password;

    private ArrayList<Task> tasks;

    public User(String name, String password, ArrayList<Task> tasks) {
        this.name = name;
        this.password = password;
        this.tasks = tasks;
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
