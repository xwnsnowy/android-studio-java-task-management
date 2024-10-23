package com.example.taskmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskmanagement.R;
import com.example.taskmanagement.models.User;
import com.example.taskmanagement.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister, btnBack;
    private TextView tvRegisterPrompt;
    private DatabaseHelper databaseHelper;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvRegisterPrompt = findViewById(R.id.tv_register_prompt);
        btnBack = findViewById(R.id.btn_back);

        // SQLite Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize SharedPreferences using the new approach
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = preferences.edit();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up the login button click handler
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (databaseHelper.isLoginValid(username, password)) {
                    // Save to SharedPreferences
                    editor.putString("currentUser", username);
                    editor.apply();
                    // Redirect to TaskListActivity
                    Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                    intent.putExtra("username", username); // Truyền thêm thông tin username
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Redirect to RegisterActivity if user clicks "Register" button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
