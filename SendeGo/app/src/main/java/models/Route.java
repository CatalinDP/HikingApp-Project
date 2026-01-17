package models;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private int id;
    private String name;
    private Difficulty difficulty;
    private RouteType type;
    private int distance;
    private int puntuacion;
    private String description;
    private String notes;
    private boolean favorite;
    public static List<Route> routeList = new ArrayList<>(); //Lista donde se guardan las rutas!

    public Route(int id, String name, Difficulty difficulty, RouteType type, int distance, int puntuacion, String description, String notes, boolean favorite) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.type = type;
        this.distance = distance;
        this.puntuacion = puntuacion;
        this.description = description;
        this.notes = notes;
        this.favorite = favorite;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
