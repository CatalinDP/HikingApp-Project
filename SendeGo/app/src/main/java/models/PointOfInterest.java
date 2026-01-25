package models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import models.Route;

@Entity(
        tableName = "points_of_interest",
        foreignKeys = @ForeignKey(
                entity = Route.class,
                parentColumns = "id",
                childColumns = "routeId",
                onDelete = ForeignKey.CASCADE
        )
)
public class PointOfInterest {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;
    private String latitude;
    private String longitude;
    private String photo;
    private int routeId;

    public PointOfInterest() {
    }

    public PointOfInterest(String name, String latitude, String longitude, String photo, int routeId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
        this.routeId = routeId;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }
}