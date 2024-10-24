package com.example.taskmanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskmanagement.R;
import java.util.Locale;

public class LanguagesActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        // Khởi tạo SharedPreferences và Editor
        mPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        mEditor = mPreferences.edit();

        // Set the saved language
        Locale savedLocale = findLanguagePreferences();
        setLocale(savedLocale);

        ImageView flagVietnam = findViewById(R.id.flag_vietnam);
        ImageView flagUS = findViewById(R.id.flag_us);
        ImageView flagFrance = findViewById(R.id.flag_france);

        flagVietnam.setOnClickListener(v -> setLocaleAndRestart("vi", "VN"));
        flagUS.setOnClickListener(v -> setLocaleAndRestart("en", "US"));
        flagFrance.setOnClickListener(v -> setLocaleAndRestart("fr", "FR"));
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void setLocaleAndRestart(String language, String country) {
        Locale locale = new Locale(language, country);
        setLocale(locale);
        saveLanguage(language);

        // Restart TaskListActivity to apply language change
        Intent intent = new Intent(LanguagesActivity.this, TaskListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void saveLanguage(String language) {
        mEditor.putString(getString(R.string.language), language);
        mEditor.commit();
    }

    private Locale findLanguagePreferences() {
        String language = mPreferences.getString(getString(R.string.language), "en");
        switch (language) {
            case "vi":
                return new Locale("vi", "VN");
            case "fr":
                return new Locale("fr", "FR");
            case "en":
            default:
                return new Locale("en", "US");
        }
    }
}
