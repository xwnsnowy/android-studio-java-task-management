package com.example.taskmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskmanagement.R;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.database.DatabaseHelper;

public class EditTaskActivity extends AppCompatActivity {

    private EditText taskName, taskDesc, taskBeginDate, taskEndDate, taskProject;
    private Spinner taskStateSpinner;
    private Button updateButton;
    private DatabaseHelper dbHelper;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Initialize fields
        taskName = findViewById(R.id.task_name);
        taskDesc = findViewById(R.id.task_desc);
        taskBeginDate = findViewById(R.id.task_begin_date);
        taskEndDate = findViewById(R.id.task_end_date);
        taskProject = findViewById(R.id.task_project);
        taskStateSpinner = findViewById(R.id.task_state_spinner);
        updateButton = findViewById(R.id.update_task_button);

        dbHelper = new DatabaseHelper(this);

        // Retrieve task ID passed from intent
        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId != -1) {
            currentTask = dbHelper.getTaskById(taskId);
            loadTaskData(currentTask);
        }

        // Set up spinner for task states
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.task_states,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskStateSpinner.setAdapter(adapter);

        // Update task on button click
        updateButton.setOnClickListener(v -> updateTask());
    }

    private void loadTaskData(Task task) {
        if (task != null) {
            taskName.setText(task.getName());
            taskDesc.setText(task.getDescription());
            taskBeginDate.setText(task.getBeginDate());
            taskEndDate.setText(task.getMaxEndDate());
            taskProject.setText(task.getProjectName());
            setSpinnerSelection(task.getStateTask().getStatue(this)); // Use 'this' for context
        }
    }

    private void setSpinnerSelection(String state) {
        String[] states = getResources().getStringArray(R.array.task_states);
        for (int i = 0; i < states.length; i++) {
            if (states[i].equals(state)) {
                taskStateSpinner.setSelection(i);
                break;
            }
        }
    }

    private void updateTask() {
        String name = taskName.getText().toString();
        String desc = taskDesc.getText().toString();
        String beginDate = taskBeginDate.getText().toString();
        String endDate = taskEndDate.getText().toString();
        String project = taskProject.getText().toString();
        String state = taskStateSpinner.getSelectedItem().toString();

        currentTask.setName(name);
        currentTask.setDescription(desc);
        currentTask.setBeginDate(beginDate);
        currentTask.setMaxEndDate(endDate);
        currentTask.setProjectName(project);
        currentTask.getStateTask().changeState(state, this);

        String closedState = getString(R.string.closed);
        if (state.equals(closedState)) {
            dbHelper.deleteTask(currentTask.getId());
            Toast.makeText(this, "Task deleted as it is marked " + closedState, Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateTask(currentTask);
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, TaskListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
