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
import io.senders.jgs.mime.MimeTypes;
import io.senders.jgs.util.SSLContext;
import java.util.Map;

public class Server {
  private int port;

  public Server(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    FileManager fileManager = new FileManager(new MimeTypes(Map.of()));
    EventLoopGroup mainGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(mainGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  SslContext ctx = SSLContext.singleton();
                  ch.pipeline()
                      .addLast("ssl", ctx.newHandler(ch.alloc()))
                      .addLast(new MessageHandler(fileManager));
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);
      ChannelFuture cf = b.bind(port).sync();
      cf.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      mainGroup.shutdownGracefully();
    }
  }

  public static void main(String... args) throws Exception {
    new Server(1965).run();
  }
}
