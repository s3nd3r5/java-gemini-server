package io.senders.jgs.standalone;

import io.senders.jgs.Server;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.configs.typesafe.TypesafeServerConfigFactory;
import io.senders.jgs.request.PathBasedRouteHandler;
import io.senders.jgs.response.InputResponseMessage;
import io.senders.jgs.response.ResponseDoc;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class ComplexMain {

  public static void main(String... args) throws Exception {
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

    new Server(config)
        .run(
            new PathBasedRouteHandler(
                Map.of(
                    "/ping",
                    uri -> {
                      if (uri.getQuery() == null) return new InputResponseMessage("Hello?");
                      else
                        return new ResponseDoc(
                            "text/plain",
                            "UTF-8",
                            null,
                            uri.getQuery().getBytes(StandardCharsets.UTF_8));
                    })));
  }
}
