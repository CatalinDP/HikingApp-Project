package com.app.sendego.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.sendego.CustomAdapter;
import com.app.sendego.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.AppDatabase;
import models.Route;

public class FavoriteRoutesFragment extends Fragment {

    private AppDatabase appDatabase;
    private List<Route> routeList = new ArrayList<>();
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmptyFavorites;

    public FavoriteRoutesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fav_recycler_view);
        tvEmptyFavorites = view.findViewById(R.id.tvEmptyFavorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CustomAdapter(routeList);
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

        appDatabase = AppDatabase.getAppDatabase(requireContext());

        loadFavList();
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private void loadFavList() {
        executor.execute(() -> {
            List<Route> favorites = appDatabase.daoRoute().getFavoriteRoutes();

            requireActivity().runOnUiThread(() -> {
                routeList.clear();
                if (favorites != null) {
                    routeList.addAll(favorites);
                }

                adapter.setRoutes(routeList);

                if (routeList.isEmpty()) {
                    tvEmptyFavorites.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmptyFavorites.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}