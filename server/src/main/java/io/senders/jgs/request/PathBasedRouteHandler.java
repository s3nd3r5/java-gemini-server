package io.senders.jgs.request;

import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import java.net.URI;
import java.util.Map;

public class PathBasedRouteHandler implements RouteHandler {

  private final Map<String, RouteHandler> pathHandler;

  public PathBasedRouteHandler(Map<String, RouteHandler> routeHandler) {
    this.pathHandler = routeHandler;
  }

  @Override
  public ResponseMessage handle(final URI uri) {
    return pathHandler
        .getOrDefault(
            uri.getPath(), _uri -> new ResponseMessage(GeminiStatus.NOT_FOUND, _uri.toString()))
        .handle(uri);
  }
}
