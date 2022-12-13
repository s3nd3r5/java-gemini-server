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

/** Main server class for Java Gemini Server */
public class Server {

  private final ServerConfig config;
  private final Collection<Host> hosts;
  private final RequestHandler defaultRouteHandler;

  /**
   * Create a new server
   *
   * @param config {@link ServerConfig} for the server
   * @param hosts Collection of {@link Host} this server handles
   * @param defaultRouteHandler default {@link RequestHandler} which handles any request that
   *     doesn't match any router configured by any host.
   * @see #newBuilder()
   * @see ServerBuilder
   */
  public Server(
      final ServerConfig config,
      final Collection<Host> hosts,
      final RequestHandler defaultRouteHandler) {
    this.config = config;
    this.hosts = hosts;
    this.defaultRouteHandler = defaultRouteHandler;
  }

  /**
   * Create a new builder for the server
   *
   * @return a new {@link ServerBuilder}
   */
  public static ServerBuilder newBuilder() {
    return ServerBuilder.newBuilder();
  }

  /**
   * Runs the server. This is the main blocking server function that runs your gemini server.
   *
   * <p>The logic in this function should be configured by the {@link ServerConfig}.
   */
  public void run() {
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
      final ChannelFuture cf = b.bind(config.port()).syncUninterruptibly();
      cf.channel().closeFuture().syncUninterruptibly();
    } finally {
      workerGroup.shutdownGracefully();
      mainGroup.shutdownGracefully();
    }
  }
}
