package io.senders.jgs.server;

import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.handlers.RequestHandler;
import io.senders.jgs.request.routers.Host;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ServerBuilder {
  private ServerConfig config;
  private Collection<Host> hosts = new ArrayList<>();
  private RequestHandler fallbackRouteHandler;

  private ServerBuilder() {}

  public static ServerBuilder newBuilder() {
    return new ServerBuilder();
  }

  public ServerBuilder withConfig(final ServerConfig config) {
    this.config = config;
    return this;
  }

  /**
   * If none of the routers match the given URI this handler will handle the route. By default this
   * just returns a 51 response code (not found).
   *
   * @param fallbackRouteHandler
   * @return builder for chaining
   */
  public ServerBuilder setFallbackRouteHandler(final RequestHandler fallbackRouteHandler) {
    this.fallbackRouteHandler = fallbackRouteHandler;
    return this;
  }

  public ServerBuilder setHosts(final Collection<Host> hosts) {
    this.hosts = hosts;
    return this;
  }

  public ServerBuilder addHost(final Host host) {
    this.hosts.add(host);
    return this;
  }

  public ServerBuilder addRouters(final Host... hosts) {
    this.hosts.addAll(Arrays.asList(hosts));
    return this;
  }

  public Server build() {
    return new Server(config, hosts, fallbackRouteHandler);
  }
}
