package com.app.sendego.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.sendego.CustomAdapter;
import com.app.sendego.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.AppDatabase;
import models.Difficulty;
import models.Route;


public class FavoriteRoutesFragment extends Fragment {

    AppDatabase appDatabase;
    List<Route> routeList = new ArrayList<>();
    CustomAdapter adapter;
    public FavoriteRoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.spinnerDifficulty);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );
        adapter = new CustomAdapter(routeList);
        appDatabase = AppDatabase.getAppDatabase(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.fav_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        loadFavList();
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();
    private void loadFavList() {
        executor.execute(() -> {
            routeList = appDatabase.daoRoute().getFavoriteRoutes();
            requireActivity().runOnUiThread(() -> {
                adapter.setRoutes(routeList);
            });
        });
    }
}