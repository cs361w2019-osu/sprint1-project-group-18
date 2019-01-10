package conf;

import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import controllers.ApplicationController;

@SuppressWarnings("unused")
public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {

        router.GET().route("/").with(ApplicationController::index);
        router.GET().route("/game").with(ApplicationController::newGame);
        router.POST().route("/place").with(ApplicationController::placeShip);
        router.POST().route("/attack").with(ApplicationController::attack);

        ///////////////////////////////////////////////////////////////////////
        // Assets (pictures / javascript)
        ///////////////////////////////////////////////////////////////////////    
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController::serveWebJars);
        router.GET().route("/assets/{fileName: .*}").with(AssetsController::serveStatic);
    }

}
