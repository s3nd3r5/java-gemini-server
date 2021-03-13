package io.senders.jgs;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.senders.jgs.configs.ServerConfig;
import io.senders.jgs.request.MessageHandler;
import io.senders.jgs.request.RouteHandler;
import io.senders.jgs.util.SSLContext;

public class Server {

  private final ServerConfig config;

  public Server(final ServerConfig config) {
    this.config = config;
  }

  public void run(final RouteHandler routeHandler) throws Exception {
    final SslContext sslContext = SSLContext.fromConfig(config);

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
                      .addLast("ssl", sslContext.newHandler(ch.alloc()))
                      .addLast(new MessageHandler(routeHandler));
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);
      final ChannelFuture cf = b.bind(config.getHostname(), config.getPort()).sync();
      cf.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      mainGroup.shutdownGracefully();
    }
  }
}
