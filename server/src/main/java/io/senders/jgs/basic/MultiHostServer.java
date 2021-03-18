package io.senders.jgs.basic;

import io.senders.jgs.Server;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.FileRouteHandler;
import io.senders.jgs.request.HostnameRouteHandler;
import io.senders.jgs.response.ResponseDoc;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MultiHostServer {

  public static void main(String... args) throws Exception {
    final ServerConfig config = ServerConfig.newBuilder().build();
    new Server(config)
        .run(
            new HostnameRouteHandler(
                Map.of(
                    "localhost",
                    uri ->
                        new ResponseDoc(
                            "text/plain", null, null, "ok".getBytes(StandardCharsets.UTF_8)),
                    "example.com",
                    FileRouteHandler.fromConfig(config.defaultHostConfig()),
                    "test.example.com",
                    uri ->
                        new ResponseDoc(
                            "text/gemini",
                            "UTF-8",
                            "en",
                            "# success\r\n\r\nYou did it\r\n".getBytes(StandardCharsets.UTF_8)))));
  }
}
