package com.app.sendego;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import models.Route;

public class RouteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTE_ID = "route_id";

    private Route route;
    private ImageView favoriteIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        int routeId = getIntent().getIntExtra(EXTRA_ROUTE_ID, -1);
        route = Route.routeList.stream()
                .filter(r -> r.getId() == routeId)
                .findFirst()
                .orElse(null);

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
        });

        // Datos técnicos
        TextView distanceText = findViewById(R.id.distanceText);
        TextView difficultyText = findViewById(R.id.difficultyText);
        TextView timeText = findViewById(R.id.timeText);

        distanceText.setText(route.getDistance() + " km");
        difficultyText.setText(route.getDifficulty().toString());

        double speed = route.getDifficulty() == models.Difficulty.DIFICIL ? 3.0 : 4.0;
        double estimatedTime = route.getDistance() / speed;
        timeText.setText(String.format("%.1f h", estimatedTime));

        // Información de la ruta
        TextView descriptionText = findViewById(R.id.descriptionText);
        descriptionText.setText(route.getDescription());

        TextView coordinatesText = findViewById(R.id.coordinatesText);
        coordinatesText.setText(String.format("Lat: %.4f, Lon: %.4f",
                route.getLatitude(), route.getLongitude()));
    }

    private void updateFavoriteIcon() {
        if (route.isFavorite()) {
            favoriteIcon.setImageResource(R.drawable.estrella_rellena);
        } else {
            favoriteIcon.setImageResource(R.drawable.estrella_vacia);
        }
    }
}
