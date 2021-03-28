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
