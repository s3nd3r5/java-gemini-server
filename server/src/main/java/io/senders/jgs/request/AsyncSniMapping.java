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

public class AsyncSniMapping implements AsyncMapping<String, SslContext> {

  private final Map<String, SslContext> mapping;
  private final SslContext defaultContext;

  public AsyncSniMapping(final Map<String, SslContext> sslContextMap, SslContext defaultContext) {
    this.mapping = sslContextMap;
    this.defaultContext = defaultContext;
  }

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
