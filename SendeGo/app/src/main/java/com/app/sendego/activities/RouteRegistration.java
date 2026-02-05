package com.app.sendego.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.app.sendego.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.AppDatabase;
import models.Difficulty;
import models.Route;
import models.RouteType;

public class RouteRegistration extends AppCompatActivity {

    private AppDatabase appDatabase;
    private RouteType routeType = RouteType.CIRCULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_route_registration);

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
            EditText etLatitude = findViewById(R.id.etLatitude);
            EditText etLongitude = findViewById(R.id.etLongitude);
            Switch swFavorito = findViewById(R.id.swFavorite);
            RatingBar ratingBar = findViewById(R.id.ratingBarRegistration);

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

            String lat = etLatitude.getText().toString().trim();
            String lon = etLongitude.getText().toString().trim();
            if (lat.isEmpty()) lat = null;
            if (lon.isEmpty()) lon = null;

            // Obtenemos la dificultad directamente del spinner seleccionado
            int difficultyPosition = spDificultad.getSelectedItemPosition();
            Difficulty selectedDifficulty = Difficulty.values()[difficultyPosition];

            // Obtenemos el tipo de ruta del spinner (por consistencia)
            int typePosition = spTipoRuta.getSelectedItemPosition();
            RouteType selectedRouteType = RouteType.values()[typePosition];
            float puntos = ratingBar.getRating();

            Route route = new Route(
                    name,
                    selectedDifficulty,
                    selectedRouteType,
                    distance,
                    puntos,
                    etDescription.getText().toString().trim(),
                    etNotes.getText().toString().trim(),
                    swFavorito.isChecked(),
                    lat,
                    lon
            );

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
}