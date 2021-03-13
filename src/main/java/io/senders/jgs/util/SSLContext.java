package io.senders.jgs.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.senders.jgs.configs.ServerConfig;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Factory for creating netty's SSLHandler */
public class SSLContext {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static SslContext fromConfig(final ServerConfig config) {
    try {
      logger.info("Creating new SSL Context");
      File cert = Path.of(config.getCertFile()).toFile();
      File key = Path.of(config.getCertKey()).toFile();
      SslContext ctx = SslContextBuilder.forServer(cert, key).build();
      logger.info("Created new SSL Context");
      return ctx;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
