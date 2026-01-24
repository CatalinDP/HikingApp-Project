package threads;

import dao.DaoRoute;
import models.Route;

public class UpdateRoute extends Thread {
    private final Route route;
    private final DaoRoute daoRoute;

    public UpdateRoute(Route route, DaoRoute daoRoute) {
        this.route = route;
        this.daoRoute = daoRoute;
    }

    @Override
    public void run() {
        daoRoute.updateRoute(route);
    }
}
