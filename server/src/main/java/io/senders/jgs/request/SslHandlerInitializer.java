package io.senders.jgs.request;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class SslHandlerInitializer {

  private final SslContext context;

  public SslHandlerInitializer(SslContext context) {
    this.context = context;
  }

  public SslHandler init(SocketChannel ch) {
    return context.newHandler(ch.alloc());
  }
}
