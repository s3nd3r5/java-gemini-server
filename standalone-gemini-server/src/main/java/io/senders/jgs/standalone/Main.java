package io.senders.jgs.standalone;

import io.senders.jgs.Server;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.configs.typesafe.TypesafeServerConfigFactory;
import io.senders.jgs.request.FileRouteHandler;
import io.senders.jgs.request.HostnameRouteHandler;
import io.senders.jgs.request.RouteHandler;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Runs a standalone gemini file server. */
public class Main {

  public static void main(String... args) throws Exception {
    /*
     Initialize the configuration. Being a standalone implementation I tried to give a few options.
     But ideally you would just put the gemini-server.conf in your working directory and not need
     to pass in any overrides. But the point of this is you don't actually provide any additional
     code just config changes, so you could pass in your local env version that binds to localhost
     and keep your production one being gemini-server.conf for example.
    */
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
      final Map<String, RouteHandler> sniFileHandlerMap = new HashMap<>();
      config
          .hosts()
          .forEach(
              (hostname, hostConf) ->
                  sniFileHandlerMap.put(hostname, FileRouteHandler.fromConfig(hostConf)));

      new Server(config).run(new HostnameRouteHandler(sniFileHandlerMap));
    } else {
      new Server(config).run(FileRouteHandler.fromConfig(config.defaultHostConfig()));
    }
  }
}
