package com.app.sendego.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class RouteDetailFragment extends Fragment {

    public static final String EXTRA_ROUTE_ID = "route_id";

    private Route route;
    private ImageView favoriteIcon;
    private AppDatabase db;
    private PointsOfInterestAdapter poiAdapter;
    private RecyclerView recyclerPoi;
    private WebView webViewMap;
    private long routeId;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<Intent> addPoiLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    loadRouteAndPois();
                }
            });

    public RouteDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getAppDatabase(requireContext());

        // Obtener ID desde argumentos
        Bundle args = getArguments();
        if (args != null) {
            long id = args.getLong("route_id", -1L);
            if (id != -1L) {
                routeId = id;
            } else {
                Toast.makeText(requireContext(), "ID de ruta inválido", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
                return;
            }
        } else {
            Toast.makeText(requireContext(), "No se recibió ID de ruta", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        favoriteIcon = view.findViewById(R.id.favoriteIcon);
        webViewMap = view.findViewById(R.id.webViewMap);
        Button btnOpenMaps = view.findViewById(R.id.btnOpenMaps);

        recyclerPoi = view.findViewById(R.id.recyclerPoi);
        recyclerPoi.setLayoutManager(new LinearLayoutManager(requireContext()));
        poiAdapter = new PointsOfInterestAdapter(new ArrayList<>());
        recyclerPoi.setAdapter(poiAdapter);

        Button btnAddPoi = view.findViewById(R.id.btnAddPoi);
        btnAddPoi.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PointOfInterestRegistration.class);
            intent.putExtra("route_id", (int) routeId);
            addPoiLauncher.launch(intent);
        });

        btnOpenMaps.setOnClickListener(v -> {
            if (route == null || route.getLatitude() == null || route.getLongitude() == null) {
                Toast.makeText(requireContext(), "No hay coordenadas disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            double lat;
            double lon;
            try {
                lat = Double.parseDouble(route.getLatitude());
                lon = Double.parseDouble(route.getLongitude());
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Coordenadas inválidas", Toast.LENGTH_SHORT).show();
                return;
            }

            String etiqueta = route.getName() != null ? route.getName() : "Inicio de la ruta";

            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(" + Uri.encode(etiqueta) + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(requireContext(), "Google Maps no está instalado", Toast.LENGTH_SHORT).show();
            }
        });

        loadRouteAndPois();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadRouteAndPois() {
        executor.execute(() -> {
            route = db.daoRoute().getRouteById((int) routeId);

            requireActivity().runOnUiThread(() -> {
                if (route == null) {
                    Toast.makeText(requireContext(), "Ruta no encontrada", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    return;
                }

                // Cabecera
                TextView routeTitle = requireView().findViewById(R.id.routeTitle);
                routeTitle.setText(route.getName());
                updateFavoriteIcon();

                favoriteIcon.setOnClickListener(v -> {
                    route.setFavorite(!route.isFavorite());
                    updateFavoriteIcon();
                    executor.execute(() -> db.daoRoute().updateRoute(route));
                });

                // Datos técnicos
                TextView distanceText = requireView().findViewById(R.id.distanceText);
                TextView difficultyText = requireView().findViewById(R.id.difficultyText);
                TextView timeText = requireView().findViewById(R.id.timeText);

                distanceText.setText(route.getDistance() + " km");
                difficultyText.setText(route.getDifficulty().name());

                double speed = route.getDifficulty() == models.Difficulty.DIFICIL ? 3.0 : 4.0;
                double estimatedTime = route.getDistance() / speed;
                timeText.setText(String.format("%.1f h", estimatedTime));

                // Descripción
                TextView descriptionText = requireView().findViewById(R.id.descriptionText);
                descriptionText.setText(route.getDescription() != null ? route.getDescription() : "");

                // Coordenadas
                TextView coordinatesText = requireView().findViewById(R.id.coordinatesText);
                String lat = route.getLatitude() != null ? route.getLatitude() : "—";
                String lon = route.getLongitude() != null ? route.getLongitude() : "—";
                coordinatesText.setText("Lat: " + lat + "\nLon: " + lon);

                // Cargar mapa integrado en WebView
                if (route.getLatitude() != null && route.getLongitude() != null) {
                    try {
                        double latDouble = Double.parseDouble(route.getLatitude());
                        double lonDouble = Double.parseDouble(route.getLongitude());

                        String mapUrl = "https://maps.google.com/maps?q=" + latDouble + "," + lonDouble + "&hl=es&z=15&t=m&output=embed";

                        WebSettings webSettings = webViewMap.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setDomStorageEnabled(true);
                        webSettings.setLoadWithOverviewMode(true);
                        webSettings.setUseWideViewPort(true);
                        webSettings.setBuiltInZoomControls(true);
                        webSettings.setDisplayZoomControls(false);

                        webViewMap.loadUrl(mapUrl);
                        webViewMap.setVisibility(View.VISIBLE);
                    } catch (NumberFormatException e) {
                        webViewMap.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Coordenadas inválidas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    webViewMap.setVisibility(View.GONE);
                }
            });

            new LoadPointsOfInterest(db, (int) routeId, pois ->
                    requireActivity().runOnUiThread(() -> poiAdapter.setPois(pois))
            ).run();
        });
    }

    private void updateFavoriteIcon() {
        if (route != null && favoriteIcon != null) {
            favoriteIcon.setImageResource(
                    route.isFavorite() ? R.drawable.estrella_rellena : R.drawable.estrella_vacia
            );
        }
    }
}