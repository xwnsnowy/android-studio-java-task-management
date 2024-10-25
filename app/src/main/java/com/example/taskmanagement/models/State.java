package com.example.taskmanagement.models;

import static com.example.taskmanagement.Constants.CLOSED;
import static com.example.taskmanagement.Constants.DOING;
import static com.example.taskmanagement.Constants.TODO;

public class State {

    private boolean toDo;

    private boolean doing;

    private boolean closed;

    public State() {
        this.toDo = true;
        this.closed = false;
        this.doing = false;
    }

    /**
     * This method allows to change teh state of the task.
     * Cette méthode permet de changer l'état de la tache
     * @param newState The new state of the task / Le nouvel état de la tache
     */
    public void changeState(String newState){
        switch (newState) {
            case "toDo":
                this.toDo = true;
                this.doing = false;
                this.closed = false;
                break;
            case "doing":
                this.toDo = false;
                this.doing = true;
                this.closed = false;
                break;
            case "closed":
                this.toDo = false;
                this.doing = false;
                this.closed = true;
                break;
        }
    }

    public String getStatue(){
        if(toDo){
            return TODO;
        }else if(doing){
            return DOING;
        }else{
            return CLOSED;
        }
    }

    public int compareTo(State other){
        if(getStatue().equalsIgnoreCase(other.getStatue())){
            return 0;
        }else{
            if((getStatue().equalsIgnoreCase(TODO) || getStatue().equalsIgnoreCase(DOING)) &&
                    (other.getStatue().equalsIgnoreCase(DOING) || other.getStatue().equalsIgnoreCase(CLOSED))){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
