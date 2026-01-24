package threads;

import android.util.Log;

import dao.DaoRoute;
import models.Route;

public class InsertRoute implements Runnable {
    private final Route route;
    private final DaoRoute daoRoute;

    public InsertRoute(Route route, DaoRoute daoRoute) {
        this.route = route;
        this.daoRoute = daoRoute;
    }

    @Override
    public void run() {
        try {
            long resultado = daoRoute.insertRoute(route);
            Log.d("ROOM_DB", "ID generado: " + resultado);
        } catch (Exception e) {
            Log.e("ROOM_DB", "ERROR AL INSERTAR: " + e.getMessage());
        }
    }
}
