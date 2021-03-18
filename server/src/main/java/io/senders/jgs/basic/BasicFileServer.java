package io.senders.jgs.basic;

import io.senders.jgs.Server;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.FileRouteHandler;

public class BasicFileServer {

  public static void main(String... args) throws Exception {
    final ServerConfig config = ServerConfig.newBuilder().build();
    new Server(config).run(FileRouteHandler.fromConfig(config.hosts().get(config.hostname())));
  }
}
