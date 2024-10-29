package com.example.taskmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanagement.R;
import com.example.taskmanagement.adapter.TaskAdapter;
import com.example.taskmanagement.adapter.ViewPagerAdapter;
import com.example.taskmanagement.models.User;
import com.example.taskmanagement.database.DatabaseHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TaskListActivity extends AppCompatActivity {

    private SwitchCompat switchMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences userPrefs;
    private DatabaseHelper databaseHelper;
    private ImageButton btnMenu, btnProfile;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button btnNewTask;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText searchContent;
    private TaskAdapter taskAdapter;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the saved mode or system-wide mode before layout inflation
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        applyNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = preferences.edit();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        bindingView();
        bindingAction();

        String username = getIntent().getStringExtra("username");
        if (username == null) {
            username = userPrefs.getString("currentUser", "Guest");
        }

        databaseHelper = new DatabaseHelper(this);
        User user = databaseHelper.getUserByUsername(username);
        TextView tvWelcome = findViewById(R.id.tv_name);
        tvWelcome.setText(user != null ? user.getName() + "!" : "Guest!");

        bindingAction();

        searchContent.clearFocus();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.my_tasks);
                    break;
                case 1:
                    tab.setText(R.string.in_progress_2);
                    break;
                case 2:
                    tab.setText(R.string.completed_2);
                    break;
            }
        }).attach();

        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();

                Fragment currentFragment = viewPagerAdapter.getFragment(viewPager.getCurrentItem());
                if (currentFragment instanceof TaskSearchListener) {
                    ((TaskSearchListener) currentFragment).onSearchQuery(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public interface TaskSearchListener {
        void onSearchQuery(String query);
    }

    private void bindingView() {
        switchMode = findViewById(R.id.switchMode);
        userPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        btnMenu = findViewById(R.id.menu_button);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        btnNewTask = findViewById(R.id.btn_new_task);
        btnProfile = findViewById(R.id.profile_icon);
        searchContent = findViewById(R.id.search_content);
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
        btnProfile.setOnClickListener(this::showPopupMenuProfile);
        btnNewTask.setOnClickListener(this::onBtnNewTaskClick);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.view.setContentDescription("Tab " + tab.getPosition() + " selected");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setContentDescription("Tab " + tab.getPosition() + " unselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.view.setContentDescription("Tab " + tab.getPosition() + " reselected");
            }
        });
    }

    private void onBtnNewTaskClick(View view) {
        Intent intent = new Intent(TaskListActivity.this, CreateTaskActivity.class);
        startActivity(intent);
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

    private void showPopupMenuProfile(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_item1) {
                // Logout logic
                editor.remove("currentUser");
                editor.apply();
                // Redirect to HomeActivity
                Intent intent = new Intent(TaskListActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("refreshTasks", false)) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof MyTasksFragment) {
                    ((MyTasksFragment) fragment).refreshTasks();
                    break;
                }
            }
        }
    }

}
