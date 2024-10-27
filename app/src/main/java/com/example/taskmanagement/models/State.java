package com.example.taskmanagement.models;

import static com.example.taskmanagement.Constants.CLOSED;
import static com.example.taskmanagement.Constants.DOING;
import static com.example.taskmanagement.Constants.TODO;
import static com.example.taskmanagement.Constants.COMPLETED;

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

    /**
     * This method allows to change the state of the task.
     * Cette méthode permet de changer l'état de la tâche
     * @param newState The new state of the task / Le nouvel état de la tâche
     */
    public void changeState(String newState) {
        switch (newState) {
            case "To Do":
                this.toDo = true;
                this.doing = false;
                this.closed = false;
                this.completed = false;
                break;
            case "In Progress":
                this.toDo = false;
                this.doing = true;
                this.closed = false;
                this.completed = false;
                break;
            case "Closed":
                this.toDo = false;
                this.doing = false;
                this.closed = true;
                this.completed = false;
                break;
            case "Completed":
                this.toDo = false;
                this.doing = false;
                this.closed = false;
                this.completed = true;
                break;
        }
    }

    public String getStatue() {
        if (toDo) {
            return TODO;
        } else if (doing) {
            return DOING;
        } else if (completed) {
            return COMPLETED;
        } else {
            return CLOSED;
        }
    }

    public int compareTo(State other) {
        if (getStatue().equalsIgnoreCase(other.getStatue())) {
            return 0;
        } else {
            if ((getStatue().equalsIgnoreCase(TODO) || getStatue().equalsIgnoreCase(DOING) || getStatue().equalsIgnoreCase(COMPLETED)) &&
                    (other.getStatue().equalsIgnoreCase(DOING) || other.getStatue().equalsIgnoreCase(CLOSED))) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
