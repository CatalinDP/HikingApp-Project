package com.app.sendego.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.sendego.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.AppDatabase;
import models.Difficulty;
import models.Route;
import models.RouteType;

public class RouteRegistration extends AppCompatActivity {

    private AppDatabase appDatabase;
    private RouteType routeType = RouteType.CIRCULAR;
    private PreviewView myPreview;
    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_route_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.routeRegistration),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        myPreview = findViewById(R.id.viewFinder);
        Button btnCapture = findViewById(R.id.btnCapture);

        //Comprobación de permisos
        if (ContextCompat.checkSelfPermission(RouteRegistration.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos el permiso, lo pedimos
            ActivityCompat.requestPermissions(RouteRegistration.this, new String[]{Manifest.permission.CAMERA}, 101);
        } else {
            // Si ya lo tenemos, arrancamos la cámara
            configurarCameraX();
        }
        configurarCameraX();
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
            }
        });


        appDatabase = AppDatabase.getAppDatabase(this);

        // Spinner Dificultad
        Spinner spDificultad = findViewById(R.id.spDifficulty);
        ArrayAdapter<CharSequence> adapterDificultad = ArrayAdapter.createFromResource(
                this,
                R.array.difficultyRegistration,
                android.R.layout.simple_spinner_item
        );
        adapterDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDificultad.setAdapter(adapterDificultad);

        // Spinner Tipo de ruta
        Spinner spTipoRuta = findViewById(R.id.spRouteType);
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(
                this,
                R.array.route_type,
                android.R.layout.simple_spinner_item
        );
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoRuta.setAdapter(adapterTipo);

        spTipoRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routeType = RouteType.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Botón guardar
        Button btnGuardar = findViewById(R.id.btnSave);

        btnGuardar.setOnClickListener(v -> {
            EditText etNombreRuta = findViewById(R.id.etRouteName);
            EditText etDistance = findViewById(R.id.etDistance);
            EditText etDescription = findViewById(R.id.etDescription);
            EditText etNotes = findViewById(R.id.etNotes);

            EditText etLatDeg = findViewById(R.id.etLatDeg);
            EditText etLatMin = findViewById(R.id.etLatMin);
            EditText etLatSec = findViewById(R.id.etLatSec);

            EditText etLonDeg = findViewById(R.id.etLonDeg);
            EditText etLonMin = findViewById(R.id.etLonMin);
            EditText etLonSec = findViewById(R.id.etLonSec);

            SwitchCompat swFavorito = findViewById(R.id.swFavorite);
            RatingBar ratingBar = findViewById(R.id.ratingBarRegistration);

            // Validaciones básicas
            String name = etNombreRuta.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "El nombre de la ruta es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            String distanceStr = etDistance.getText().toString().trim();
            if (distanceStr.isEmpty()) {
                Toast.makeText(this, "La distancia es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            double distance;
            try {
                distance = Double.parseDouble(distanceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La distancia debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener campos de coordenadas
            String latDeg = etLatDeg.getText().toString().trim();
            String latMin = etLatMin.getText().toString().trim();
            String latSec = etLatSec.getText().toString().trim();
            String lonDeg = etLonDeg.getText().toString().trim();
            String lonMin = etLonMin.getText().toString().trim();
            String lonSec = etLonSec.getText().toString().trim();

            // Validar que estén todos completos
            if (latDeg.isEmpty() || latMin.isEmpty() || latSec.isEmpty() ||
                    lonDeg.isEmpty() || lonMin.isEmpty() || lonSec.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos de latitud y longitud", Toast.LENGTH_SHORT).show();
                return;
            }

            // Concatenamos tal cual (sin puntos, sin conversión)
            String finalLat = latDeg + latMin + latSec;   // ej: "380317"
            String finalLon = lonDeg + lonMin + lonSec;   // ej: "-11247"

            // Spinner Dificultad
            int difficultyPosition = spDificultad.getSelectedItemPosition();
            Difficulty selectedDifficulty = Difficulty.values()[difficultyPosition];

            // Tipo de ruta
            int typePosition = spTipoRuta.getSelectedItemPosition();
            RouteType selectedRouteType = RouteType.values()[typePosition];

            float puntos = ratingBar.getRating();
            Route route;
            if (photoFile != null) {
                route = new Route(
                        name,
                        selectedDifficulty,
                        selectedRouteType,
                        distance,
                        puntos,
                        etDescription.getText().toString().trim(),
                        etNotes.getText().toString().trim(),
                        swFavorito.isChecked(),
                        finalLat,
                        finalLon,
                        photoFile.getAbsolutePath()
                );
            } else {
                route = new Route(
                        name,
                        selectedDifficulty,
                        selectedRouteType,
                        distance,
                        puntos,
                        etDescription.getText().toString().trim(),
                        etNotes.getText().toString().trim(),
                        swFavorito.isChecked(),
                        finalLat,
                        finalLon
                );
            }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                appDatabase.daoRoute().insertRoute(route);
                runOnUiThread(() -> {
                    Toast.makeText(RouteRegistration.this, "Ruta guardada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configurarCameraX();
            } else {
                Toast.makeText(this, "Necesitas aceptar el permiso para usar la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void configurarCameraX() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        // Creamos el "aviso" para cuando esté lista
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    enlazarCasosDeUso();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void enlazarCasosDeUso() {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(myPreview.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();
        // Selección de cámara trasera
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        try {
            cameraProvider.unbindAll();
            // Se vincula todo al ciclo de vida de la actividad
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);


        } catch (Exception e) {
            Log.e("CameraX", "Error al vincular casos de uso", e);
        }
    }
    private void tomarFoto() {
        if (imageCapture == null) return;
        Toast.makeText(this, "BotonTomar", Toast.LENGTH_SHORT).show();
        photoFile = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(RouteRegistration.this, "Foto guardada en: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(RouteRegistration.this, "Error con la foto, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}