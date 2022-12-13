/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 - 2022 senders <steph (at) senders.io>
 * --
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * -/-/-
 */
package io.senders.jgs.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.senders.jgs.configs.CertConfig;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/** Factory for constructing {@link SslContext} */
public class SslContextFactory {

  /**
   * Creates an {@link SslContext} from the {@link CertConfig} of the {@link
   * io.senders.jgs.configs.HostConfig} from the {@link io.senders.jgs.configs.ServerConfig}
   *
   * @param config {@link CertConfig} of the host of the server
   * @return new SslContext from the cert configs
   */
  public static SslContext fromConfig(final CertConfig config) {
    return buildSslContext(config.file(), config.key());
  }

  /**
   * Create a {@link SslContext} for a given cert and key file
   *
   * @param certPath path to a cert file
   * @param keyPath path to a key file
   * @return new SslContext for the given cert and key pair
   */
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
