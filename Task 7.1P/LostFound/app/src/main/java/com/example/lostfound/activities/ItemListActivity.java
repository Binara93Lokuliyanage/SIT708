package com.example.lostfound.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfound.R;
import com.example.lostfound.adaptors.AdvertAdapter;
import com.example.lostfound.database.Advert;
import com.example.lostfound.database.DBHelper;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Spinner spinnerFilter;
    DBHelper dbHelper;
    ArrayList<Advert> advertList;
    AdvertAdapter adapter;

    String[] categories = {"All", "Electronics", "Pets", "Wallets", "Documents", "Keys", "Bags", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recyclerView = findViewById(R.id.recyclerView);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerFilter.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories));

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                loadAdverts(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdverts(spinnerFilter.getSelectedItem().toString());
    }

    private void loadAdverts(String categoryFilter) {
        advertList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;

        if (categoryFilter.equals("All")) {
            cursor = db.rawQuery("SELECT * FROM adverts ORDER BY id DESC", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM adverts WHERE category = ? ORDER BY id DESC",
                    new String[]{categoryFilter});
        }

        while (cursor.moveToNext()) {
            advertList.add(new Advert(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
            ));
        }

        cursor.close();

        adapter = new AdvertAdapter(advertList, advert -> {
            Intent intent = new Intent(this, ItemDetailsActivity.class);
            intent.putExtra("id", advert.id);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}