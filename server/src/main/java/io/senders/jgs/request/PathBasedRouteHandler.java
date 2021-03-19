package io.senders.jgs.request;

import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import java.net.URI;
import java.util.Map;

public class PathBasedRouteHandler implements RouteHandler {

  private static final RouteHandler DEFAULT_DEFAULT_HANDLER =
      uri -> new ResponseMessage(GeminiStatus.NOT_FOUND, uri.toString());
  private final Map<String, RouteHandler> pathHandlers;
  private final RouteHandler defaultHandler;

  public PathBasedRouteHandler(Map<String, RouteHandler> pathHandlers) {
    this.pathHandlers = pathHandlers;
    this.defaultHandler = DEFAULT_DEFAULT_HANDLER;
  }

  public PathBasedRouteHandler(
      Map<String, RouteHandler> pathHandlers, RouteHandler defaultHandler) {
    this.pathHandlers = pathHandlers;
    this.defaultHandler = defaultHandler;
  }

  @Override
  public ResponseMessage handle(final URI uri) {
    return pathHandlers.getOrDefault(uri.getPath(), defaultHandler).handle(uri);
  }
}
