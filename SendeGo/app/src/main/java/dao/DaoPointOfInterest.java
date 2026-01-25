package dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import models.PointOfInterest;

import java.util.List;

@Dao
public interface DaoPointOfInterest {

    @Insert
    void insertPointOfInterest(PointOfInterest poi);

    @Query("SELECT * FROM points_of_interest WHERE routeId = :routeId")
    List<PointOfInterest> getPointsByRouteId(int routeId);
}