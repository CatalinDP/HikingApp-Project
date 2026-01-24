package com.app.sendego;

import static models.Difficulty.DIFICIL;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.Converters;
import database.AppDatabase;
import models.Difficulty;
import models.Route;
import models.RouteType;
import threads.InsertRoute;

public class RouteRegistration extends AppCompatActivity {
    AppDatabase appDatabase;
    Difficulty difficulty = Difficulty.FACIL;
    RouteType routeType = RouteType.CIRCULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_route_registration);

        appDatabase = AppDatabase.getAppDatabase(this);

        Spinner spDificultad = findViewById(R.id.spDifficulty);
        ArrayAdapter<CharSequence> adapterDificultad = ArrayAdapter.createFromResource(
                this,
                R.array.difficultyRegistration,
                android.R.layout.simple_spinner_item
        );
        adapterDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDificultad.setAdapter(adapterDificultad);

        Spinner spTipoRuta = findViewById(R.id.spRouteType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.route_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoRuta.setAdapter(adapter);

        Button btnGuardar = findViewById(R.id.btnSave);
        EditText etNombreRuta = findViewById(R.id.etRouteName);

        spDificultad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                difficulty = Difficulty.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTipoRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routeType = RouteType.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardar.setOnClickListener(v -> {
            String name = etNombreRuta.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "El nombre de la ruta es obligatorio", Toast.LENGTH_SHORT).show();
            } else {
                EditText location = findViewById(R.id.etLocation);
                EditText distance = findViewById(R.id.etDistance);
                EditText description = findViewById(R.id.etDescription);
                EditText notes = findViewById(R.id.etNotes);
                Switch swFavorito = findViewById(R.id.swFavorite);

                ///  TODO: Mejorar seguridad de campos

                insertRoute(
                        new Route(
                                name,
                                difficulty,
                                routeType,
                                Integer.parseInt(distance.getText().toString()),
                                5,
                                description.getText().toString(),
                                notes.getText().toString(),
                                swFavorito.isChecked(),
                                0,
                                0
                        ));
                Toast.makeText(this, "Ruta guardada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void insertRoute(Route route) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.daoRoute().insertRoute(route);
        });
    }
}