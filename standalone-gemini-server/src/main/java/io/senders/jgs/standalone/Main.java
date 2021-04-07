/*-
 * -\-\-
 * Standalone Java Gemini Server
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
package io.senders.jgs.standalone;

import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.configs.typesafe.TypesafeServerConfigFactory;
import io.senders.jgs.request.handlers.FileRouteHandler;
import io.senders.jgs.request.routers.Host;
import io.senders.jgs.request.routers.impls.BasicFileRouter;
import io.senders.jgs.server.ServerBuilder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

  public static void main(String... args) {
    ServerConfig config = null;
    if (args.length == 1 && args[0].endsWith(".conf")) {
      var file = Path.of(args[0]).toFile();
      if (file.exists() && file.isFile() && file.canRead()) {
        config = TypesafeServerConfigFactory.create(file.getPath());
      }
    } else if (System.getProperty("config") != null) {
      config = TypesafeServerConfigFactory.create(System.getProperty("config"));
    }

    if (config == null) {
      config = TypesafeServerConfigFactory.create();
    }

    if (config.sni()) {

      Collection<Host> hosts = new ArrayList<>();
      config
          .hosts()
          .forEach(
              (host, hostConfig) ->
                  hosts.add(
                      Host.newBuilder(host).withRouter(new BasicFileRouter(hostConfig)).build()));

      ServerBuilder.newBuilder().withConfig(config).setHosts(hosts).build().run();

    } else {
      ServerBuilder.newBuilder()
          .withConfig(config)
          .addHost(
              Host.newBuilder(config.hostname())
                  .withRouter(new BasicFileRouter(config.defaultHostConfig()))
                  .build())
          .setFallbackRouteHandler(FileRouteHandler.fromConfig(config.defaultHostConfig()))
          .build()
          .run();
    }
  }
}
