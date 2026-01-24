package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import models.Route;

@Dao
public interface DaoRoute {
    @Insert
    long insertRoute(Route route);

    @Update
    void updateRoute(Route route);

    @Delete
    void deleteRoute(Route route);

    @Query("SELECT * FROM Route")
    List<Route> getAllRoutes();

    @Query("SELECT * FROM Route WHERE id = :id")
    Route getRouteById(int id);

    @Query("SELECT * FROM Route WHERE difficulty = :difficulty")
    List<Route> getRoutesByDifficulty(String difficulty);

}
