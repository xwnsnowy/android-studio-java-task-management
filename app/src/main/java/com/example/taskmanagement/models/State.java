package com.example.taskmanagement.models;

import android.content.Context;
import com.example.taskmanagement.R;

public class State {
    private boolean toDo;
    private boolean doing;
    private boolean closed;
    private boolean completed;

    public State() {
        this.toDo = true;
        this.closed = false;
        this.doing = false;
        this.completed = false;
    }

    public void changeState(String newState, Context context) {

        String todoState = context.getString(R.string.to_do);
        String inProgressState = context.getString(R.string.in_progress);
        String closedState = context.getString(R.string.closed);
        String completedState = context.getString(R.string.completed);

        if (newState.equals(todoState)) {
            this.toDo = true;
            this.doing = false;
            this.closed = false;
            this.completed = false;
        } else if (newState.equals(inProgressState)) {
            this.toDo = false;
            this.doing = true;
            this.closed = false;
            this.completed = false;
        } else if (newState.equals(closedState)) {
            this.toDo = false;
            this.doing = false;
            this.closed = true;
            this.completed = false;
        } else if (newState.equals(completedState)) {
            this.toDo = false;
            this.doing = false;
            this.closed = false;
            this.completed = true;
        }
    }

    public String getStatue(Context context) {

        if (toDo) {
            return context.getString(R.string.to_do);
        } else if (doing) {
            return context.getString(R.string.in_progress);
        } else if (completed) {
            return context.getString(R.string.completed);
        } else {
            return context.getString(R.string.closed);
        }
    }
}
