/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 senders <stephen (at) senders.io>
 * --
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * -/-/-
 */
package io.senders.jgs.server;

import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.handlers.NotFoundHandler;
import io.senders.jgs.request.handlers.RequestHandler;
import io.senders.jgs.request.routers.Host;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ServerBuilder {
  private ServerConfig config;
  private Collection<Host> hosts = new ArrayList<>();
  private RequestHandler fallbackRouteHandler = new NotFoundHandler();

  private ServerBuilder() {}

  public static ServerBuilder newBuilder() {
    return new ServerBuilder();
  }

  public ServerBuilder withConfig(final ServerConfig config) {
    this.config = config;
    return this;
  }

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
