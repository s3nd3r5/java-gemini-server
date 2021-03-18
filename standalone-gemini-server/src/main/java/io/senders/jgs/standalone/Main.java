package io.senders.jgs.standalone;

import io.senders.jgs.Server;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.configs.typesafe.TypesafeServerConfigFactory;
import io.senders.jgs.request.FileRouteHandler;

public class Main {

  public static void main(String... args) throws Exception {
    var configPath = System.getProperty("config");
    final ServerConfig config;
    if (configPath != null) {
      config = TypesafeServerConfigFactory.create(configPath);
    } else {
      config = TypesafeServerConfigFactory.create();
    }

    if (config.sni()) {
      throw new IllegalStateException("Standalone server does not support SNI");
    }

    new Server(config).run(FileRouteHandler.fromConfig(config.defaultHostConfig()));
  }
}
