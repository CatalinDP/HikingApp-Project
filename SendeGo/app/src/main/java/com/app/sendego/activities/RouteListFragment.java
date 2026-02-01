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
import android.widget.Toast;

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

public class RouteListFragment extends Fragment {
    AppDatabase appDatabase;
    List<Route> routeList = new ArrayList<>();
    List<Route> filteredList = new ArrayList<>();
    CustomAdapter adapter;
    RecyclerView recyclerView;

    public RouteListFragment() {
    }

    private void loadRecyclerView() {
        adapter = new CustomAdapter(routeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnRouteClickListener(route -> {
            RouteDetailFragment detailFragment = new RouteDetailFragment();

            Bundle args = new Bundle();
            args.putLong("route_id", route.getId());
            detailFragment.setArguments(args);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_list, container, false);
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
        appDatabase = AppDatabase.getAppDatabase(getContext());
        recyclerView = view.findViewById(R.id.recycler_view);
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
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private void loadList() {
        executor.execute(() -> {
            routeList = appDatabase.daoRoute().getAllRoutes();
            requireActivity().runOnUiThread(() -> {
                adapter.setRoutes(routeList);
            });
        });
    }

    private void loadFilteredList(Difficulty difficulty) {
        executor.execute(() -> {
            filteredList = appDatabase.daoRoute().getRoutesByDifficulty(difficulty.toString());
            requireActivity().runOnUiThread(() -> {
                adapter.setRoutes(filteredList);
            });
        });
    }
}