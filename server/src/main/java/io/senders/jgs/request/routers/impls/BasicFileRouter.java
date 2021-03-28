package io.senders.jgs.request.routers.impls;

import io.senders.jgs.configs.HostConfig;
import io.senders.jgs.request.handlers.FileRouteHandler;
import io.senders.jgs.request.handlers.RequestHandler;
import io.senders.jgs.request.routers.Route;
import io.senders.jgs.request.routers.Router;
import java.util.Collection;
import java.util.Collections;

public class BasicFileRouter implements Router {

  private final RequestHandler fileHandler;

  public BasicFileRouter(final HostConfig config) {
    this.fileHandler = FileRouteHandler.fromConfig(config);
  }

  @Override
  public Collection<Route> routes() {
    return Collections.singleton(new Route("/*", fileHandler::handle));
  }
}
