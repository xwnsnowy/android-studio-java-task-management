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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.User;
import com.example.taskmanagement.sql.DatabaseHelper;

import java.util.Locale;

public class TaskListActivity extends AppCompatActivity {
    SwitchCompat switchMode;
    SharedPreferences sharedPreferences;
    SharedPreferences userPrefs;
    SharedPreferences.Editor editor;
    DatabaseHelper databaseHelper;

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
        userPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

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

                // Relaunch the activity
                Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Get current user info from intent
        String username = getIntent().getStringExtra("username");

        // If the username is not passed in the intent, get it from SharedPreferences
        if (username == null) {
            username = userPrefs.getString("currentUser", "Guest");
        }

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        databaseHelper = new DatabaseHelper(this);
        User user = databaseHelper.getUserByUsername(username);
        TextView tvWelcome = findViewById(R.id.tv_welcome);
        if (user != null) {
            tvWelcome.setText("Hello, " + user.getName() + "!");
        } else {
            tvWelcome.setText("Hello, Guest!");
        }

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_item1:
//                        Toast.makeText(TaskListActivity.this, "Item 1 clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.menu_item2:
//                        Toast.makeText(TaskListActivity.this, "Item 2 clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.menu_item3:
//                        Toast.makeText(TaskListActivity.this, "Item 3 clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
        popupMenu.show();
    }

    private void saveMode(String mode) {
        editor = sharedPreferences.edit();
        editor.putString("mode", mode);
        editor.apply();
    }
}
