package io.senders.jgs.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.senders.jgs.configs.ServerConfig;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/** Factory for creating netty's SSLHandler */
public class SslContextFactory {

  public static SslContext fromConfig(final ServerConfig config) {
    return buildSslContext(config.getCertFile(), config.getCertKey());
  }

  public static SslContext of(final String certPath, final String keyPath) {
    return buildSslContext(certPath, keyPath);
  }

  private static SslContext buildSslContext(final String certPath, final String keyPath) {
    try {
      File cert = Path.of(certPath).toFile();
      File key = Path.of(keyPath).toFile();
      return SslContextBuilder.forServer(cert, key).build();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
