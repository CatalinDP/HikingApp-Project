package com.app.sendego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Route;
import models.RouteType;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Route> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView routeName;
        private final TextView routeDifficulty;
        private final TextView routeDistance;
        private final RatingBar routeRating;
        private final ImageView routeImage;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            routeName = view.findViewById(R.id.RouteName);
            routeDifficulty = view.findViewById(R.id.cardDifficulty);
            routeRating = view.findViewById(R.id.cardRating);
            routeDistance = view.findViewById(R.id.cardDistance);
            routeImage = view.findViewById(R.id.cardImage);
        }
    }
    public CustomAdapter(List<Route> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        int color = 1;

        Route route = localDataSet.get(position);
        String difficulty = "";
        Context context = viewHolder.itemView.getContext();


        switch (route.getDifficulty()) {
            case FACIL: {
                color = R.color.color_easy;
                difficulty = context.getString(R.string.difficultyEasy);
                break;
            }
            case MEDIA: {
                color = R.color.color_mid;
                difficulty = context.getString(R.string.difficultyMid);
                break;
            }
            case DIFICIL: {
                color = R.color.color_hard;
                difficulty = context.getString(R.string.difficultyHard);
                break;
            }
        }
        viewHolder.routeName.setText(route.getName());
        viewHolder.routeDifficulty.setText(difficulty);
        viewHolder.routeDifficulty.setTextColor(context.getColor(color));
        viewHolder.routeRating.setRating(route.getPuntuacion());
        viewHolder.routeDistance.setText(route.getDistance() + " km");
        viewHolder.routeImage.setImageResource(route.getType() == RouteType.LINEAL ? R.drawable.linear_path : R.drawable.circular_path);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Ruta seleccionada" + route.getName(), Toast.LENGTH_SHORT).show();
                ///  TODO: Aquí implementar la Ficha tecnica para que te lleve a la otra vista
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}