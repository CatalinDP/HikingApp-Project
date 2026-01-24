package threads;

import dao.DaoRoute;
import models.Route;

public class DeleteRoute extends Thread {
    private final Route route;
    private final DaoRoute daoRoute;

    public DeleteRoute(Route route, DaoRoute daoRoute) {
        this.route = route;
        this.daoRoute = daoRoute;
    }

    @Override
    public void run() {
        daoRoute.deleteRoute(route);
    }
}
