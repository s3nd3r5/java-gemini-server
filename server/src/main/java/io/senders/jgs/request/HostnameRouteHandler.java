package io.senders.jgs.request;

import io.senders.jgs.response.ResponseMessage;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class HostnameRouteHandler implements RouteHandler {

  private final Map<String, RouteHandler> hostnameHandlerMap;

  public HostnameRouteHandler(final Map<String, RouteHandler> hostnameHandlerMap) {
    this.hostnameHandlerMap = Objects.requireNonNull(hostnameHandlerMap, "hostnameHandlerMap");
  }

  @Override
  public ResponseMessage handle(URI uri) {
    return hostnameHandlerMap.get(uri.getHost()).handle(uri);
  }
}
