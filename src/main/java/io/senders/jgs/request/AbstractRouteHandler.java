package io.senders.jgs.request;

import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.response.ResponseMessage;
import java.net.URI;
import java.util.Objects;

public abstract class AbstractRouteHandler implements RouteHandler {
  protected final ServerConfig config;

  public AbstractRouteHandler(ServerConfig config) {
    this.config = Objects.requireNonNull(config, "config");
  }

  public abstract ResponseMessage handle(final URI uri);
}
