package com.example.taskmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagement.R;
import com.example.taskmanagement.database.DatabaseHelper;
import com.example.taskmanagement.models.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister, btnBack;
    private DatabaseHelper databaseHelper;

    private void bindingView() {
        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        btnBack = findViewById(R.id.btn_back);
    }

    private void bindingAction() {
        btnRegister.setOnClickListener(this::onBtnRegisterClick);
        btnBack.setOnClickListener(this::onBtnBackClick);
    }

    private void onBtnRegisterClick(View view) {
        registerUser();
    }

    private void onBtnBackClick(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        bindingView();
        bindingAction();
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etConfirmPassword.setText("");
        } else if (databaseHelper.checkUser(username)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User(name, username, password);
            databaseHelper.addUser(user);
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
