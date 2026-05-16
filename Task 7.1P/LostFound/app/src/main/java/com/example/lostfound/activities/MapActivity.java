package com.example.lostfound.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.lostfound.R;
import com.example.lostfound.database.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_CODE = 300;

    GoogleMap googleMap;
    DBHelper dbHelper;
    FusedLocationProviderClient fusedLocationClient;

    EditText etRadius;
    Button btnApplyRadius;

    double currentLat = 0.0;
    double currentLng = 0.0;
    double selectedRadiusKm = 10.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper = new DBHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        etRadius = findViewById(R.id.etRadius);
        btnApplyRadius = findViewById(R.id.btnApplyRadius);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnApplyRadius.setOnClickListener(v -> {
            String radiusText = etRadius.getText().toString().trim();

            if (radiusText.isEmpty()) {
                Toast.makeText(this, "Please enter radius in km", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedRadiusKm = Double.parseDouble(radiusText);
            loadMarkersWithinRadius();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        getCurrentLocationForMap();
    }

    private void getCurrentLocationForMap() {
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

        googleMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLng = location.getLongitude();

                        LatLng currentPosition = new LatLng(currentLat, currentLng);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));

                        loadMarkersWithinRadius();
                    } else {
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_LONG).show();

                        LatLng melbourne = new LatLng(-37.8136, 144.9631);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 12));
                        loadAllMarkers();
                    }
                });
    }

    private void loadMarkersWithinRadius() {
        if (googleMap == null) return;

        googleMap.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM adverts", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            String locationText = cursor.getString(6);
            double latitude = cursor.getDouble(10);
            double longitude = cursor.getDouble(11);

            float[] result = new float[1];

            Location.distanceBetween(
                    currentLat,
                    currentLng,
                    latitude,
                    longitude,
                    result
            );

            double distanceKm = result[0] / 1000.0;

            if (distanceKm <= selectedRadiusKm) {
                LatLng advertLocation = new LatLng(latitude, longitude);

                googleMap.addMarker(new MarkerOptions()
                        .position(advertLocation)
                        .title(type + ": " + name)
                        .snippet(locationText + " | " + String.format("%.2f", distanceKm) + " km away"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(advertLocation, 15));
            }
        }

        cursor.close();

        Toast.makeText(this, "Showing items within " + selectedRadiusKm + " km", Toast.LENGTH_SHORT).show();
    }

    private void loadAllMarkers() {
        if (googleMap == null) return;

        googleMap.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM adverts", null);

        while (cursor.moveToNext()) {
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            String locationText = cursor.getString(6);
            double latitude = cursor.getDouble(10);
            double longitude = cursor.getDouble(11);

            LatLng advertLocation = new LatLng(latitude, longitude);

            googleMap.addMarker(new MarkerOptions()
                    .position(advertLocation)
                    .title(type + ": " + name)
                    .snippet(locationText));
        }

        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationForMap();
            } else {
                Toast.makeText(this, "Location permission is required for radius search", Toast.LENGTH_SHORT).show();
            }
        }
    }
}