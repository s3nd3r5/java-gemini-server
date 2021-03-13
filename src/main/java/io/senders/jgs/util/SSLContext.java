package io.senders.jgs.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Factory for creating netty's SSLHandler */
public class SSLContext {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static SslContext ctx;

  public static SslContext singleton() throws Exception {
    if (ctx == null) {
      logger.info("Creating new SSL Context");
      File cert = Path.of("./certs/server.crt").toFile();
      File key = Path.of("./certs/encKey.pem").toFile();
      ctx = SslContextBuilder.forServer(cert, key).build();
      logger.info("Created new SSL Context");
    }
    return ctx;
  }
}
