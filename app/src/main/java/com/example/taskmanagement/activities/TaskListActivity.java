package com.example.taskmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanagement.R;

import java.util.Locale;

public class TaskListActivity extends AppCompatActivity {
    SwitchCompat switchMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        switchMode = findViewById(R.id.switchMode);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (nightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        saveMode("LIGHT_MODE");
                        editor = sharedPreferences.edit();
                        editor.putBoolean("nightMode", false);
                        editor.apply();
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveMode("DARK_MODE");
                        editor = sharedPreferences.edit();
                        editor.putBoolean("nightMode", true);
                        editor.apply();
                        break;
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        String text = getResources().getString(R.string.brithness_mode_impossible);
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        break;
                }

                Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveMode(String mode) {
        editor = sharedPreferences.edit();
        editor.putString("mode", mode);
        editor.apply();
    }
}
