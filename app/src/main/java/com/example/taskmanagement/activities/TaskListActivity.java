package com.example.taskmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.User;
import com.example.taskmanagement.sql.DatabaseHelper;

public class TaskListActivity extends AppCompatActivity {

    private SwitchCompat switchMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences userPrefs;
    private DatabaseHelper databaseHelper;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the saved mode or system-wide mode before layout inflation
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        applyNightMode();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);

        bindingView();
        bindingAction();

        // Get current user info from intent or SharedPreferences
        String username = getIntent().getStringExtra("username");
        if (username == null) {
            username = userPrefs.getString("currentUser", "Guest");
        }

        databaseHelper = new DatabaseHelper(this);
        User user = databaseHelper.getUserByUsername(username);
        TextView tvWelcome = findViewById(R.id.tv_name);
        tvWelcome.setText(user != null ? user.getName() + "!" : "Guest!");
    }

    private void bindingView() {
        switchMode = findViewById(R.id.switchMode);
        userPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        btnMenu = findViewById(R.id.menu_button);
    }

    private void bindingAction() {
        // Set switch state according to current mode
        boolean nightMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        switchMode.setChecked(nightMode);

        switchMode.setOnClickListener(v -> {
            boolean isNightModeEnabled = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
            if (isNightModeEnabled) {
                // Switch to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveMode(false);
            } else {
                // Switch to dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveMode(true);
            }
        });

        // Set action for the menu button
        btnMenu.setOnClickListener(this::showPopupMenu);
    }

    private void applyNightMode() {
        boolean nightMode = sharedPreferences.getBoolean("nightMode", false);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_item1) {
                // Change Language
                Intent intent = new Intent(TaskListActivity.this, LanguagesActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.menu_item2) {
                // Handle menu_item2 action here
                return true;
            } else if (itemId == R.id.menu_item3) {
                // Handle menu_item3 action here
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void saveMode(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightMode", nightMode);
        editor.apply();
    }
}
