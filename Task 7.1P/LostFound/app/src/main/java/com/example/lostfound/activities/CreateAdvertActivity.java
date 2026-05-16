package com.example.lostfound.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.lostfound.R;
import com.example.lostfound.database.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 100;
    private static final int LOCATION_PERMISSION_CODE = 200;

    RadioGroup radioGroup;
    EditText etName, etPhone, etDescription, etDate, etLocation;
    Spinner spinnerCategory;
    ImageView imagePreview;
    Button btnUpload, btnSave, btnCurrentLocation;

    Uri selectedImageUri;
    DBHelper dbHelper;
    FusedLocationProviderClient fusedLocationClient;

    double selectedLatitude = 0.0;
    double selectedLongitude = 0.0;

    String[] categories = {"Select Category", "Electronics", "Pets", "Wallets", "Documents", "Keys", "Bags", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        dbHelper = new DBHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        radioGroup = findViewById(R.id.radioGroup);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imagePreview = findViewById(R.id.imagePreview);
        btnUpload = findViewById(R.id.btnUpload);
        btnSave = findViewById(R.id.btnSave);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);

        etDate.setFocusable(false);
        etDate.setClickable(true);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateAdvertActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDate.setText(selectedDate);
                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        spinnerCategory.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        ));

        btnUpload.setOnClickListener(v -> openImagePicker());
        btnCurrentLocation.setOnClickListener(v -> getCurrentLocation());
        btnSave.setOnClickListener(v -> saveAdvert());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_CODE
            );
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        selectedLatitude = location.getLatitude();
                        selectedLongitude = location.getLongitude();

                        String addressText = getAddressFromCoordinates(selectedLatitude, selectedLongitude);
                        etLocation.setText(addressText);

                        Toast.makeText(this, "Current location selected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unable to get current location. Set emulator location and try again.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to get current location", Toast.LENGTH_SHORT).show()
                );
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return latitude + ", " + longitude;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            getContentResolver().takePersistableUriPermission(
                    selectedImageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            imagePreview.setImageURI(selectedImageUri);
        }
    }

    private void saveAdvert() {
        int selectedRadioId = radioGroup.getCheckedRadioButtonId();

        if (selectedRadioId == -1) {
            Toast.makeText(this, "Please select Lost or Found", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadio = findViewById(selectedRadioId);
        String type = selectedRadio.getText().toString();

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() ||
                date.isEmpty() || location.isEmpty() || category.equals("Select Category")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedLatitude == 0.0 && selectedLongitude == 0.0) {
            Toast.makeText(this, "Please click Get Current Location", Toast.LENGTH_SHORT).show();
            return;
        }

        String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("type", type);
        values.put("name", name);
        values.put("phone", phone);
        values.put("description", description);
        values.put("date", date);
        values.put("location", location);
        values.put("category", category);
        values.put("imageUri", selectedImageUri.toString());
        values.put("createdAt", createdAt);
        values.put("latitude", selectedLatitude);
        values.put("longitude", selectedLongitude);

        long result = db.insert("adverts", null, values);

        if (result != -1) {
            Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save advert", Toast.LENGTH_SHORT).show();
        }
    }
}