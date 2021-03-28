package io.senders.jgs.server;

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
import io.senders.jgs.request.RequestMessageAdapter;
import io.senders.jgs.request.handlers.RequestHandler;
import io.senders.jgs.request.routers.Host;
import io.senders.jgs.util.SslContextFactory;
import io.senders.jgs.util.SslHandlerProvider;
import java.util.Collection;

public class Server {

  private final ServerConfig config;
  private final Collection<Host> hosts;
  private final RequestHandler defaultRouteHandler;

  public Server(
      final ServerConfig config,
      final Collection<Host> hosts,
      final RequestHandler defaultRouteHandler) {
    this.config = config;
    this.hosts = hosts;
    this.defaultRouteHandler = defaultRouteHandler;
  }

  /**
   * Create a new builder to construct a server instance.
   *
   * @return a new ServerBuilder
   */
  public static ServerBuilder newBuilder() {
    return ServerBuilder.newBuilder();
  }

  public void run() throws Exception {
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
                      .addLast(new RequestMessageAdapter(defaultRouteHandler, hosts));
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
