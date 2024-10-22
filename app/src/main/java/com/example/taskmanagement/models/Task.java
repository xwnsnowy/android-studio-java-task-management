package com.example.taskmanagement.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private int id;

    private String name;

    private String memonic;

    private String description;

    private String estimateDuration;

    private String beginDate;

    private String maxEndDate;

    private State stateTask;

    private String context;

    private String projectName;

    private String url;

    /**
     * The constructor of the task object / Le constructeur de l'objet task
     * @param id The id of the task
     * @param name The name of the task
     * @param memonic The memonic of the task which is link to an image.
     * @param description The description of the task.
     * @param duration The estimate duration of the task.
     * @param beginDate The begin date of the task
     * @param maxEndDate The max end date of the task.
     * @param context The context of the task.
     * @param projectName The name of the project which is link to the task.
     * @param url An url link to the task / Une url liée à la tâche.
     */
    public Task(int id, String name, String memonic, String description, String duration, String beginDate, String maxEndDate, String context, String projectName, String url) {
        this.name = name;
        this.description = description;
        this.memonic = memonic;
        this.estimateDuration = duration;
        this.beginDate = beginDate;
        this.maxEndDate = maxEndDate;
        this.id = id;
        this.stateTask = new State();
        this.stateTask.changeState(memonic);
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

    public String getMemonic() {
        return memonic;
    }

    public void setMemonic(String memonic) {
        this.memonic = memonic;
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

    public int compareByState(Task other){
        return stateTask.compareTo(other.getStateTask());
    }

    public int compareByDuration(Task other){
        String[] values = estimateDuration.split("h");
        Integer currentDuration = Integer.parseInt(values[0])*60+Integer.parseInt(values[1].split("m")[0]);

        values = other.getEstimateDuration().split("h");
        Integer otherDuration = Integer.parseInt(values[0])*60+Integer.parseInt(values[1].split("m")[0]);

        return currentDuration.compareTo(otherDuration);
    }
}
