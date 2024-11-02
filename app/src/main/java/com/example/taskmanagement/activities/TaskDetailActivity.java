package com.example.taskmanagement.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskmanagement.R;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.database.DatabaseHelper;

public class TaskDetailActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView taskName, taskDescription, taskDate, taskEstimatedDuration, taskProject, taskState;
    private ImageView taskIcon, btnDelete;
    private Button btnBack, btnUpdate;
    private int userId;

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

    private void bindingView(int taskId) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = preferences.getInt("currentUserId", -1);

        Task task = dbHelper.getTaskById(taskId, userId);
        Context context = taskName.getContext();
        if (task != null) {
            String taskNameLabel = context.getString(R.string.task_name_label);
            String taskNameText = taskNameLabel + ": " + task.getName();
            taskName.setText(createBoldSpannable(taskNameText, taskNameLabel.length()));

            String taskDescriptionLabel = context.getString(R.string.task_description_label);
            String taskDescriptionText = taskDescriptionLabel + ": " + task.getDescription();
            taskDescription.setText(createBoldSpannable(taskDescriptionText, taskDescriptionLabel.length()));

            String taskStateLabel = context.getString(R.string.task_state_label);
            String taskStateText = taskStateLabel + ": " + task.getStateTask().getStatue(this);
            taskState.setText(createBoldSpannable(taskStateText, taskStateLabel.length()));

            String taskDateLabel = context.getString(R.string.task_date_label);
            String taskDateText = taskDateLabel + ": " + task.getMaxEndDate();
            taskDate.setText(createBoldSpannable(taskDateText, taskDateLabel.length()));

            String taskEstimatedDurationLabel = context.getString(R.string.task_estimated_duration_label);
            String taskEstimatedDurationText = taskEstimatedDurationLabel + ": " + task.getEstimateDuration();
            taskEstimatedDuration.setText(createBoldSpannable(taskEstimatedDurationText, taskEstimatedDurationLabel.length()));

            String taskProjectLabel = context.getString(R.string.task_project_label);
            String taskProjectText = taskProjectLabel + ": " + task.getProjectName();
            taskProject.setText(createBoldSpannable(taskProjectText, taskProjectLabel.length()));
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
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = preferences.getInt("currentUserId", -1);
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    dbHelper.deleteTask(taskId, userId);

                    Toast.makeText(TaskDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(TaskDetailActivity.this, TaskListActivity.class);
                    intent.putExtra("refreshTasks", true);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private SpannableString createBoldSpannable(String text, int labelLength) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, labelLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        dbHelper = new DatabaseHelper(this);
        int taskId = getIntent().getIntExtra("taskId", -1);

        bindingView();
        bindingAction();

        bindingView(taskId);
    }
}
