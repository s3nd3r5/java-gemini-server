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
package io.senders.jgs.request;

import io.netty.handler.ssl.SslContext;
import io.netty.util.AsyncMapping;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.exceptions.ServerBaseException;
import io.senders.jgs.status.GeminiStatus;
import io.senders.jgs.util.SslContextFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of Netty's {@link AsyncMapping}. Provides a mapping of hostname to {@link
 * SslContext}. Used by {@link io.senders.jgs.server.Server} to handle the ssl handshake when using
 * SNI.
 */
public class AsyncSniMapping implements AsyncMapping<String, SslContext> {

  private final Map<String, SslContext> mapping;
  private final SslContext defaultContext;

  /**
   * Create a new AsyncSniMapping instance. Providing the mapping to be used by {@link
   * io.senders.jgs.server.Server}
   *
   * @param sslContextMap map of hostnames to {@link SslContext}
   * @param defaultContext optional default context to be used if hostname not in map
   * @see #fromConfig(ServerConfig)
   */
  public AsyncSniMapping(
      final Map<String, SslContext> sslContextMap, final SslContext defaultContext) {
    this.mapping = sslContextMap;
    this.defaultContext = defaultContext;
  }

  /**
   * Returns a future of the SslContext for the hostname input
   *
   * @param input hostname requested
   * @param promise promise to be fulfilled when the result is available
   * @return future of the result of the mapping
   */
  @Override
  public Future<SslContext> map(String input, Promise<SslContext> promise) {
    var context = mapping.getOrDefault(input, defaultContext);
    if (context == null) {
      return promise.setFailure(
          // TODO figure out how to better handle this error. As this is for the host provider
          new ServerBaseException(
              GeminiStatus.PERMANENT_FAILURE, "Host " + input + " does not exist on this server?"));
    } else {
      return promise.setSuccess(context);
    }
  }

  /**
   * Create an SNI mapping from your server's configuration
   *
   * @param serverConfig configuration for your server
   * @return a new mapping implementation
   */
  public static AsyncSniMapping fromConfig(final ServerConfig serverConfig) {
    final Map<String, SslContext> mapping = new ConcurrentHashMap<>();
    serverConfig
        .hosts()
        .forEach(
            (host, hostConfig) ->
                mapping.put(host, SslContextFactory.fromConfig(hostConfig.cert())));
    // TODO handle nulls
    final SslContext defaultContext =
        SslContextFactory.fromConfig(serverConfig.hosts().get(serverConfig.hostname()).cert());
    return new AsyncSniMapping(mapping, defaultContext);
  }
}
