package models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Route {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String name;
    private Difficulty difficulty;
    private RouteType type;
    private int distance;
    private float puntuacion;
    private String description;
    private String notes;
    private boolean favorite;
    private String latitude;
    private String longitude;
    public static List<Route> routeList = new ArrayList<>(); //Lista donde se guardan las rutas!

    public Route(String name, Difficulty difficulty, RouteType type, int distance, float puntuacion, String description, String notes, boolean favorite, String latitude, String longitude) {
        this.name = name;
        this.difficulty = difficulty;
        this.type = type;
        this.distance = distance;
        this.puntuacion = puntuacion;
        this.description = description;
        this.notes = notes;
        this.favorite = favorite;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Route() {
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
