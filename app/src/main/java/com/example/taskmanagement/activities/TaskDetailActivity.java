package com.example.taskmanagement.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.sql.DatabaseHelper;

public class TaskDetailActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView taskName, taskDescription, taskDate, taskEstimatedDuration, taskProject, taskState;
    private ImageView taskIcon;
    private Button btnBack;

    private void bindingView() {
        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.task_description);
        taskDate = findViewById(R.id.task_date);
        taskEstimatedDuration = findViewById(R.id.task_estimated_duration);
        taskProject = findViewById(R.id.task_project);
        taskState = findViewById(R.id.task_state);
        taskIcon = findViewById(R.id.task_icon);
        btnBack = findViewById(R.id.btn_back);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(this::onBtnBackClick);
    }

    private void bindingData(int taskId) {
        Task task = dbHelper.getTaskById(taskId);
        if (task != null) {
            taskName.setText(task.getName());
            taskDescription.setText(task.getDescription());
            taskDate.setText(task.getMaxEndDate());
            taskEstimatedDuration.setText(task.getEstimateDuration());
            taskProject.setText(task.getProjectName());
            taskState.setText(task.getStateTask().getStatue());
            // Load task icon if applicable
        }
    }

    private void onBtnBackClick(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        dbHelper = new DatabaseHelper(this);
        int taskId = getIntent().getIntExtra("taskId", -1);

        // Bind views and actions
        bindingView();
        bindingAction();

        // Load and display task data
        bindingData(taskId);
    }
}
