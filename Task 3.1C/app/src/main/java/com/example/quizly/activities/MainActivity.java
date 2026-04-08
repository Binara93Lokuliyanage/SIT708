package com.example.quizly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizly.R;

import android.content.SharedPreferences;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    EditText nameEditText;
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEditText = findViewById(R.id.nameEditText);
        startBtn = findViewById(R.id.startBtn);

        String username = getIntent().getStringExtra("username");
        if (username != null) {
            nameEditText.setText(username);
        }

        startBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();

            if (!name.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("username", name);
                startActivity(intent);
            } else {
                nameEditText.setError("Enter your name");
            }
        });


        SharedPreferences prefs = getSharedPreferences("theme", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        boolean isDark = prefs.getBoolean("darkMode", false);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        Switch themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(isDark);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("darkMode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("darkMode", false);
            }
            editor.apply();
        });
    }
}