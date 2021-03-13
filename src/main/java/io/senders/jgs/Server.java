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
import io.senders.jgs.util.SSLContext;

public class Server {

  public Server() {}

  public void run() throws Exception {
    final ServerConfig config = ServerConfig.create();
    final FileManager fileManager = FileManager.fromConfig(config);
    final SslContext sslContext = SSLContext.fromConfig(config);

    EventLoopGroup mainGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

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
                      .addLast(new MessageHandler(fileManager));
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);
      ChannelFuture cf = b.bind(config.getHostname(), config.getPort()).sync();
      cf.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      mainGroup.shutdownGracefully();
    }
  }

  public static void main(String... args) throws Exception {
    new Server().run();
  }
}
