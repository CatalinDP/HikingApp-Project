package com.app.sendego;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.Converters;
import database.AppDatabase;
import models.Difficulty;
import models.Route;

public class RoutesListActivity extends AppCompatActivity {

    AppDatabase appDatabase;
    List<Route> routeList = new ArrayList<>();
    List<Route> filteredList = new ArrayList<>();
    CustomAdapter adapter;
    private void loadRecyclerView() {
        adapter = new CustomAdapter(routeList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routes_list);

        Spinner spinner = findViewById(R.id.spinnerDifficulty);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );

        appDatabase = AppDatabase.getAppDatabase(this);

        loadRecyclerView();

        loadList();

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        loadList();
                        break;
                    }
                    case 1: {
                            loadFilteredList(Difficulty.DIFICIL);
                        break;
                    }
                    case 2: {
                            loadFilteredList(Difficulty.MEDIA);
                        break;
                    }
                    case 3: {
                            loadFilteredList(Difficulty.FACIL);
                        break;
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private void loadList() {
        if (routeList.isEmpty()) {
            executor.execute(() -> {
                routeList = appDatabase.daoRoute().getAllRoutes();
            });
        }
        runOnUiThread(() -> {
            adapter.setRoutes(routeList);
        });
    }

    private void loadFilteredList(Difficulty difficulty) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            filteredList = routeList.stream().filter(route -> route.getDifficulty() == difficulty).toList();
        }
        runOnUiThread(() -> {
            adapter.setRoutes(filteredList);
        });
    }

    private void loadFilteredListFromDb(Difficulty difficulty) {
        executor.execute(() -> {
            filteredList = appDatabase.daoRoute().getRoutesByDifficulty(difficulty.toString());
        });
        runOnUiThread(() -> {
            adapter.setRoutes(filteredList);
        });
    }
}