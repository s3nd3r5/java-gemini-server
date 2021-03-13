package io.senders.jgs.request;

import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.response.ResponseMessage;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class HostnameRouteHandler<T extends AbstractRouteHandler> extends AbstractRouteHandler {

  private final Map<String, T> hostnameHandlerMap;

  public HostnameRouteHandler(final ServerConfig config, final Map<String, T> hostnameHandlerMap) {
    super(config);
    this.hostnameHandlerMap = Objects.requireNonNull(hostnameHandlerMap, "hostnameHandlerMap");
  }

  @Override
  public ResponseMessage handle(URI uri) {
    return hostnameHandlerMap.get(uri.getHost()).handle(uri);
  }
}
