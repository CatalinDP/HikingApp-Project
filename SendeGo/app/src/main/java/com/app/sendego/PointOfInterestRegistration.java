package com.app.sendego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import database.AppDatabase;
import models.PointOfInterest;
import threads.InsertPointOfInterest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PointOfInterestRegistration extends AppCompatActivity {

    private AppDatabase appDatabase;
    private int routeId;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_point_of_interest_registration);

        appDatabase = AppDatabase.getAppDatabase(this);
        routeId = getIntent().getIntExtra(RouteDetailActivity.EXTRA_ROUTE_ID, -1);
        if (routeId == -1) {
            Toast.makeText(this, "Ruta no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EditText etName = findViewById(R.id.etPoiName);
        EditText etLatitude = findViewById(R.id.etPoiLatitude);
        EditText etLongitude = findViewById(R.id.etPoiLongitude);
        ImageView imgPreview = findViewById(R.id.imgPoiPreview);
        Button btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        Button btnSavePoi = findViewById(R.id.btnSavePoi);

        // Selector de imagen
        ActivityResultLauncher<String> pickImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imgPreview.setImageURI(uri);

                        try {
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(uri, takeFlags);
                        } catch (SecurityException e) {
                            Toast.makeText(this, "No se pudo mantener el acceso a la foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnSelectPhoto.setOnClickListener(v -> pickImage.launch("image/*"));

        btnSavePoi.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            String lat = etLatitude.getText().toString().trim();
            String lon = etLongitude.getText().toString().trim();

            if (lat.isEmpty()) lat = null;
            if (lon.isEmpty()) lon = null;

            String photoUri = (selectedImageUri != null) ? selectedImageUri.toString() : null;

            PointOfInterest poi = new PointOfInterest(name, lat, lon, photoUri, routeId);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new InsertPointOfInterest(appDatabase, poi));

            Toast.makeText(this, "Punto añadido", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }
}