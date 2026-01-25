package threads;

import database.AppDatabase;
import models.PointOfInterest;

public class InsertPointOfInterest implements Runnable {

    private final AppDatabase db;
    private final PointOfInterest poi;

    public InsertPointOfInterest(AppDatabase db, PointOfInterest poi) {
        this.db = db;
        this.poi = poi;
    }

    @Override
    public void run() {
        db.daoPointOfInterest().insertPointOfInterest(poi);
    }
}