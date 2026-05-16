package com.example.lostfound.activities;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lostfound.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnCreate, btnShow, btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = findViewById(R.id.btnCreate);
        btnShow = findViewById(R.id.btnShow);
        btnMap = findViewById(R.id.btnMap);

        btnCreate.setOnClickListener(v ->
                startActivity(new Intent(this, CreateAdvertActivity.class)));

        btnShow.setOnClickListener(v ->
                startActivity(new Intent(this, ItemListActivity.class)));

        btnMap.setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class)));
    }
}