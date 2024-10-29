package com.example.taskmanagement.models;

import android.content.Context;

import com.example.taskmanagement.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {
    private int id;
    private String name;
    private String description;
    private String estimateDuration;
    private String beginDate;
    private String maxEndDate;
    private State stateTask;
    private String context;
    private String projectName;
    private String url;

    public Task(
            Context context,
            String name,
            String description,
            String duration,
            String beginDate,
            String maxEndDate,
            String taskContext,
            String projectName,
            String url)
    {
        this.name = name;
        this.description = description;
        this.estimateDuration = duration;
        this.beginDate = beginDate;
        this.maxEndDate = maxEndDate;
        this.context = taskContext;
        this.projectName = projectName;
        this.url = url;
        this.stateTask = new State();
        this.stateTask.changeState(context.getString(com.example.taskmanagement.R.string.to_do), context);
    }

    public Task(int id, String name, String description, String estimateDuration, String beginDate, String maxEndDate, State stateTask, String context, String projectName, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.estimateDuration = estimateDuration;
        this.beginDate = beginDate;
        this.maxEndDate = maxEndDate;
        this.stateTask = stateTask;
        this.context = context;
        this.projectName = projectName;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public State getStateTask() {
        return stateTask;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getEstimateDuration() {
        return estimateDuration;
    }

    public void setEstimateDuration(String estimateDuration) {
        this.estimateDuration = estimateDuration;
    }

    public String getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(String maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public void setStateTask(State stateTask) {
        this.stateTask = stateTask;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int compareByName(Task other){
        return name.compareTo(other.getName());
    }

    public int compareByDate(Task other){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int res = 0;
        try {
            // Parse in Date object to compare it.
            Date dateBegin = sdf.parse(maxEndDate.toString());
            Date dateEnd = sdf.parse(other.maxEndDate.toString());
            res = dateBegin.compareTo(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


}
