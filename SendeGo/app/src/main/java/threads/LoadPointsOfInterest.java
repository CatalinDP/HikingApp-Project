package threads;

import database.AppDatabase;
import models.PointOfInterest;

import java.util.List;
import java.util.function.Consumer;

public class LoadPointsOfInterest implements Runnable {

    private final AppDatabase db;
    private final int routeId;
    private final Consumer<List<PointOfInterest>> onResult;

    public LoadPointsOfInterest(AppDatabase db, int routeId, Consumer<List<PointOfInterest>> onResult) {
        this.db = db;
        this.routeId = routeId;
        this.onResult = onResult;
    }

    @Override
    public void run() {
        List<PointOfInterest> points = db.daoPointOfInterest().getPointsByRouteId(routeId);
        if (onResult != null) {
            onResult.accept(points);
        }
    }
}