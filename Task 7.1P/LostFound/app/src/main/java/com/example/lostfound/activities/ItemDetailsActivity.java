package com.example.lostfound.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lostfound.R;
import com.example.lostfound.database.DBHelper;

public class ItemDetailsActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView tvDetails;
    Button btnRemove;
    DBHelper dbHelper;
    int advertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        detailImage = findViewById(R.id.detailImage);
        tvDetails = findViewById(R.id.tvDetails);
        btnRemove = findViewById(R.id.btnRemove);
        dbHelper = new DBHelper(this);

        advertId = getIntent().getIntExtra("id", -1);

        loadDetails();

        btnRemove.setOnClickListener(v -> removeAdvert());
    }

    private void loadDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM adverts WHERE id = ?",
                new String[]{String.valueOf(advertId)});

        if (cursor.moveToFirst()) {
            String details =
                    "Type: " + cursor.getString(1) + "\n\n" +
                            "Name: " + cursor.getString(2) + "\n\n" +
                            "Phone: " + cursor.getString(3) + "\n\n" +
                            "Description: " + cursor.getString(4) + "\n\n" +
                            "Date: " + cursor.getString(5) + "\n\n" +
                            "Location: " + cursor.getString(6) + "\n\n" +
                            "Category: " + cursor.getString(7) + "\n\n" +
                            "Posted: " + cursor.getString(9);

            detailImage.setImageURI(Uri.parse(cursor.getString(8)));
            tvDetails.setText(details);
        }

        cursor.close();
    }

    private void removeAdvert() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete("adverts", "id = ?",
                new String[]{String.valueOf(advertId)});

        if (result > 0) {
            Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to remove advert", Toast.LENGTH_SHORT).show();
        }
    }
}