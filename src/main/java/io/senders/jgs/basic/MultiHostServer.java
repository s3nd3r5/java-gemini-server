package io.senders.jgs.basic;

import io.senders.jgs.Server;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.AbstractRouteHandler;
import io.senders.jgs.request.FileRouteHandler;
import io.senders.jgs.request.HostnameRouteHandler;
import io.senders.jgs.response.ResponseDoc;
import io.senders.jgs.response.ResponseMessage;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MultiHostServer {

  public static void main(String... args) throws Exception {
    final ServerConfig config = ServerConfig.create();
    new Server(config)
        .run(
            new HostnameRouteHandler<>(
                config,
                Map.of(
                    "example.com",
                    FileRouteHandler.fromConfig(config),
                    "test.example.com",
                    new AbstractRouteHandler(config) {
                      @Override
                      public ResponseMessage handle(URI uri) {
                        return new ResponseDoc(
                            "text/gemini",
                            "UTF-8",
                            "en",
                            "# success\r\n\r\nYou did it\r\n".getBytes(StandardCharsets.UTF_8));
                      }
                    })));
  }
}
