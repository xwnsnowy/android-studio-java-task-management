package com.example.taskmanagement.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.sql.DatabaseHelper;

public class TaskDetailActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView taskName, taskDescription, taskDate, taskEstimatedDuration, taskProject, taskState;
    private ImageView taskIcon, btnDelete;
    private Button btnBack, btnUpdate;

    private void bindingView() {
        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.task_description);
        taskDate = findViewById(R.id.task_date);
        taskEstimatedDuration = findViewById(R.id.task_estimated_duration);
        taskProject = findViewById(R.id.task_project);
        taskState = findViewById(R.id.task_state);
        taskIcon = findViewById(R.id.task_icon);
        btnBack = findViewById(R.id.btn_back);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_update);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(this::onBtnBackClick);
        btnDelete.setOnClickListener(this::onBtnDeleteClick);
        btnUpdate.setOnClickListener(this::onBtnUpdateClick);
    }

    private void bindingData(int taskId) {
        Task task = dbHelper.getTaskById(taskId);
        Context context = taskName.getContext();

        if (task != null) {
            taskName.setText(context.getString(R.string.task_name_label) + ": " + task.getName());
            taskDescription.setText(context.getString(R.string.task_description_label) + ": " + task.getDescription());
            taskState.setText(context.getString(R.string.task_state_label) + ": " + task.getStateTask().getStatue());
            taskDate.setText(context.getString(R.string.task_date_label) + ": " + task.getMaxEndDate());
            taskEstimatedDuration.setText(context.getString(R.string.task_estimated_duration_label) + ": " + task.getEstimateDuration());
            taskProject.setText(context.getString(R.string.task_project_label) + ": " + task.getProjectName());
        }
    }

    private void onBtnBackClick(View view) {
        finish();
    }

    private void onBtnUpdateClick(View view) {
        int taskId = getIntent().getIntExtra("taskId", -1);
        Intent intent = new Intent(TaskDetailActivity.this, EditTaskActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }


    private void onBtnDeleteClick(View view) {
        int taskId = getIntent().getIntExtra("taskId", -1);

        // Tạo hộp thoại xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Thực hiện xóa trong DatabaseHelper
                    dbHelper.deleteTask(taskId);

                    // Hiển thị toast thông báo xóa thành công
                    Toast.makeText(TaskDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                    // Chuyển sang TaskListActivity và làm mới danh sách
                    Intent intent = new Intent(TaskDetailActivity.this, TaskListActivity.class);
                    intent.putExtra("refreshTasks", true);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();

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
