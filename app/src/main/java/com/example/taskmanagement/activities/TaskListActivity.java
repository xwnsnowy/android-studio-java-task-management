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
    private SwitchCompat switchMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences userPrefs;
    private SharedPreferences.Editor editor;
    private DatabaseHelper databaseHelper;
    private ImageButton btnMenu;

    private void bindingView() {
        switchMode = findViewById(R.id.switchMode);
        userPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        btnMenu = findViewById(R.id.menu_button);
    }

    private void bindingAction() {
        switchMode.setOnClickListener(v -> {
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
        });
        btnMenu.setOnClickListener(this::showPopupMenu);
    }

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

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        bindingView();
        bindingAction();

        // Get current user info from intent
        String username = getIntent().getStringExtra("username");

        // If the username is not passed in the intent, get it from SharedPreferences
        if (username == null) {
            username = userPrefs.getString("currentUser", "Guest");
        }

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        databaseHelper = new DatabaseHelper(this);
        User user = databaseHelper.getUserByUsername(username);
        TextView tvWelcome = findViewById(R.id.tv_name);
        if (user != null) {
            tvWelcome.setText(" " + user.getName() + "!");
        } else {
            tvWelcome.setText(" Guest!");
        }
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

    private void saveMode(String mode) {
        editor = sharedPreferences.edit();
        editor.putString("mode", mode);
        editor.apply();
    }
}
