package com.example.taskmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.taskmanagement.R;

public class HomeActivity extends AppCompatActivity {
    Button btnLogin;
    Button btnRegister;

    private void bindingView() {
        btnLogin = findViewById(R.id.btn_login_home);
        btnRegister = findViewById(R.id.btn_signup_home);
    }

    private void bindingAction() {
        btnLogin.setOnClickListener(this::onBtnLoginClick);
        btnRegister.setOnClickListener(this::onBtnRegisterClick);
    }

    private void onBtnLoginClick(View view) {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void onBtnRegisterClick(View view) {
        Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        bindingAction();
    }
}
