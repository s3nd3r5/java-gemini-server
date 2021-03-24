package io.senders.jgs;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SniHandler;
import io.netty.handler.ssl.SslContext;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.AsyncSniMapping;
import io.senders.jgs.request.handlers.MessageHandler;
import io.senders.jgs.request.handlers.RouteHandler;
import io.senders.jgs.util.SslContextFactory;
import io.senders.jgs.util.SslHandlerProvider;

public class Server {

  private final ServerConfig config;

  public Server(final ServerConfig config) {
    this.config = config;
  }

  public void run(final RouteHandler routeHandler) throws Exception {
    // use default SslHandler or SniHandler based on SNI configuration
    final SslHandlerProvider sslHandlerProvider;
    if (config.sni()) {
      sslHandlerProvider = (_unused) -> new SniHandler(AsyncSniMapping.fromConfig(config));
    } else {
      final SslContext sslContext =
          SslContextFactory.fromConfig(config.hosts().get(config.hostname()).cert());
      sslHandlerProvider = (ch) -> sslContext.newHandler(ch.alloc());
    }

    // main netty setup and loop
    final EventLoopGroup mainGroup = new NioEventLoopGroup();
    final EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(mainGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                  ch.pipeline()
                      .addLast("ssl", sslHandlerProvider.apply(ch))
                      .addLast(new MessageHandler(routeHandler));
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);
      final ChannelFuture cf = b.bind(config.port()).sync();
      cf.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      mainGroup.shutdownGracefully();
    }
  }
}
