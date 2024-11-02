package com.example.taskmanagement.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.database.DatabaseHelper;

import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity {

    private Button btnBack, createTaskButton;
    private EditText taskName, taskDesc, taskContext, taskBeginDate, taskEndDate, createTaskUrl;
    private AutoCompleteTextView taskProject;
    private NumberPicker taskDurationHour, taskDurationMinutes;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_task);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_task_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        userId = getIntent().getIntExtra("userId", -1);
        bindingView();
        bindingAction();
    }

    private void bindingView() {
        btnBack = findViewById(R.id.back_button);
        createTaskButton = findViewById(R.id.create_task_button);

        taskName = findViewById(R.id.task_name);
        taskDesc = findViewById(R.id.task_desc);
        taskContext = findViewById(R.id.task_context);
        taskBeginDate = findViewById(R.id.task_begin_date);
        taskEndDate = findViewById(R.id.task_end_date);
        createTaskUrl = findViewById(R.id.create_task_url);

        taskProject = findViewById(R.id.task_project);

        taskDurationHour = findViewById(R.id.task_duration_hour);
        taskDurationHour.setMinValue(0);
        taskDurationHour.setMaxValue(23);

        taskDurationMinutes = findViewById(R.id.task_duration_minutes);
        taskDurationMinutes.setMinValue(0);
        taskDurationMinutes.setMaxValue(59);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(v -> finish());

        taskBeginDate.setOnClickListener(v -> showDatePicker(taskBeginDate));
        taskEndDate.setOnClickListener(v -> showDatePicker(taskEndDate));

        createTaskButton.setOnClickListener(v -> createTask());
    }

    private void showDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    editText.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void createTask() {
        String name = taskName.getText().toString().trim();
        String description = taskDesc.getText().toString().trim();
        String project = taskProject.getText().toString().trim();
        String beginDate = taskBeginDate.getText().toString().trim();
        String endDate = taskEndDate.getText().toString().trim();
        String context = taskContext.getText().toString().trim();
        String projectUrl = createTaskUrl.getText().toString().trim();
        int durationHours = taskDurationHour.getValue();
        int durationMinutes = taskDurationMinutes.getValue();
        String duration = durationHours + "h " + durationMinutes + "m";

        Task task = new Task(
                this,
                name,
                description,
                duration,
                beginDate,
                endDate,
                context,
                project,
                projectUrl
        );

        DatabaseHelper db = new DatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = preferences.getInt("currentUserId", -1);

        db.addTask(task, userId);
        Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CreateTaskActivity.this, TaskListActivity.class);
        intent.putExtra("refreshTasks", true);
        startActivity(intent);
        finish();
    }


}
