package com.app.sendego.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sendego.PointsOfInterestAdapter;
import com.app.sendego.R;

import database.AppDatabase;
import models.Route;
import threads.LoadPointsOfInterest;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTE_ID = "route_id";
    private static final int REQUEST_ADD_POI = 1001;

    private Route route;
    private ImageView favoriteIcon;
    private AppDatabase db;
    private PointsOfInterestAdapter poiAdapter;
    private RecyclerView recyclerPoi;
    private int routeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        db = AppDatabase.getAppDatabase(this);

        routeId = getIntent().getIntExtra(EXTRA_ROUTE_ID, -1);
        if (routeId == -1) {
            Toast.makeText(this, "ID de ruta inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerPoi = findViewById(R.id.recyclerPoi);
        recyclerPoi.setLayoutManager(new LinearLayoutManager(this));
        poiAdapter = new PointsOfInterestAdapter(new ArrayList<>());
        recyclerPoi.setAdapter(poiAdapter);

        Button btnAddPoi = findViewById(R.id.btnAddPoi);
        btnAddPoi.setOnClickListener(v -> {
            Intent intent = new Intent(this, PointOfInterestRegistration.class);
            intent.putExtra(EXTRA_ROUTE_ID, routeId);
            startActivityForResult(intent, REQUEST_ADD_POI);
        });

        loadRouteAndPois();
    }

    private void loadRouteAndPois() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Cargar ruta
            route = db.daoRoute().getRouteById(routeId);

            runOnUiThread(() -> {
                if (route == null) {
                    Toast.makeText(this, "Ruta no encontrada", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                // Cabecera
                TextView routeTitle = findViewById(R.id.routeTitle);
                favoriteIcon = findViewById(R.id.favoriteIcon);
                routeTitle.setText(route.getName());
                updateFavoriteIcon();

                favoriteIcon.setOnClickListener(v -> {
                    route.setFavorite(!route.isFavorite());
                    updateFavoriteIcon();
                    executor.execute(() -> db.daoRoute().updateRoute(route));
                });

                // Datos técnicos
                TextView distanceText = findViewById(R.id.distanceText);
                TextView difficultyText = findViewById(R.id.difficultyText);
                TextView timeText = findViewById(R.id.timeText);

                distanceText.setText(route.getDistance() + " km");
                difficultyText.setText(route.getDifficulty().name());

                double speed = route.getDifficulty() == models.Difficulty.DIFICIL ? 3.0 : 4.0;
                double estimatedTime = route.getDistance() / speed;
                timeText.setText(String.format("%.1f h", estimatedTime));

                // Descripción
                TextView descriptionText = findViewById(R.id.descriptionText);
                descriptionText.setText(route.getDescription() != null ? route.getDescription() : "");

                // Coordenadas
                TextView coordinatesText = findViewById(R.id.coordinatesText);
                String lat = route.getLatitude() != null ? route.getLatitude() : "—";
                String lon = route.getLongitude() != null ? route.getLongitude() : "—";
                coordinatesText.setText("Lat: " + lat + "\nLon: " + lon);
            });

            // Cargar puntos de interés
            new LoadPointsOfInterest(db, routeId, pois ->
                    runOnUiThread(() -> poiAdapter.setPois(pois))
            ).run();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_POI && resultCode == RESULT_OK) {
            loadRouteAndPois(); // recargar lista de POI
        }
    }

    private void updateFavoriteIcon() {
        if (route != null) {
            favoriteIcon.setImageResource(
                    route.isFavorite() ? R.drawable.estrella_rellena : R.drawable.estrella_vacia
            );
        }
    }
}