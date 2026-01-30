package com.app.sendego;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import models.PointOfInterest;

import java.util.List;

public class PointsOfInterestAdapter extends RecyclerView.Adapter<PointsOfInterestAdapter.ViewHolder> {

    private List<PointOfInterest> poiList;

    public PointsOfInterestAdapter() {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poiImage;
        private final TextView poiName;
        private final TextView poiCoords;

        public ViewHolder(View view) {
            super(view);
            poiImage = view.findViewById(R.id.poiImage);
            poiName = view.findViewById(R.id.poiName);
            poiCoords = view.findViewById(R.id.poiCoords);
        }
    }

    public PointsOfInterestAdapter(List<PointOfInterest> dataSet) {
        poiList = dataSet;
    }

    public void setPois(List<PointOfInterest> pois) {
        poiList = pois;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_point_of_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PointOfInterest poi = poiList.get(position);

        holder.poiName.setText(poi.getName());
        holder.poiCoords.setText("Lat: " + (poi.getLatitude() != null ? poi.getLatitude() : "—") +
                " | Lon: " + (poi.getLongitude() != null ? poi.getLongitude() : "—"));

        if (poi.getPhoto() != null && !poi.getPhoto().isEmpty()) {
            try {
                holder.poiImage.setImageURI(Uri.parse(poi.getPhoto()));
            } catch (Exception e) {
                holder.poiImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.poiImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }
}